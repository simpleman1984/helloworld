package logback_test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.net.ServerSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class LogbackServer extends Thread {

	Logger logger = LoggerFactory.getLogger(LogbackServer.class);

	private final int port;
	private final LoggerContext lc;
	private boolean closed = false;
	private ServerSocket serverSocket;
	private List<LogbackSocketNode> socketNodeList = new ArrayList<LogbackSocketNode>();

	// used for testing purposes
	private CountDownLatch latch;

	public static void main(String argv[]) throws Exception {
		argv = new String[]{
				"33333",
				"D:\\workspace\\workspace_bzt_lab\\logback_test\\src\\logback_server.xml"
		};
		doMain(LogbackServer.class, argv);
	}

	protected static void doMain(Class<? extends LogbackServer> serverClass, String argv[]) throws Exception {
		int port = -1;
		if (argv.length == 2) {
			port = parsePortNumber(argv[0]);
		} else {
			usage("Wrong number of arguments.");
		}

		String configFile = argv[1];
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		configureLC(lc, configFile);

		LogbackServer sss = new LogbackServer(lc, port);
		sss.start();
	}

	public LogbackServer(LoggerContext lc, int port) {
		this.lc = lc;
		this.port = port;
	}

	public void run() {

		final String oldThreadName = Thread.currentThread().getName();

		try {

			final String newThreadName = getServerThreadName();
			Thread.currentThread().setName(newThreadName);

			logger.info("Listening on port " + port);
			serverSocket = getServerSocketFactory().createServerSocket(port);
			while (!closed) {
				logger.info("Waiting to accept a new client.");
				signalAlmostReadiness();
				Socket socket = serverSocket.accept();
				logger.info("Connected to client at " + socket.getInetAddress());
				logger.info("Starting new socket node.");
				LogbackSocketNode newSocketNode = new LogbackSocketNode(this, socket, lc);
				synchronized (socketNodeList) {
					socketNodeList.add(newSocketNode);
				}
				final String clientThreadName = getClientThreadName(socket);
				new Thread(newSocketNode, clientThreadName).start();
			}
		} catch (Exception e) {
			if (closed) {
				logger.info("Exception in run method for a closed server. This is normal.");
			} else {
				logger.error("Unexpected failure in run method", e);
			}
		}

		finally {
			Thread.currentThread().setName(oldThreadName);
		}
	}

	/**
	 * Returns the name given to the server thread.
	 */
	protected String getServerThreadName() {
		return String.format("Logback %s (port %d)", getClass().getSimpleName(), port);
	}

	/**
	 * Returns a name to identify each client thread.
	 */
	protected String getClientThreadName(Socket socket) {
		return String.format("Logback SocketNode (client: %s)", socket.getRemoteSocketAddress());
	}

	/**
	 * Gets the platform default {@link ServerSocketFactory}.
	 * <p>
	 * Subclasses may override to provide a custom server socket factory.
	 */
	protected ServerSocketFactory getServerSocketFactory() {
		return ServerSocketFactory.getDefault();
	}

	/**
	 * Signal another thread that we have established a connection This is
	 * useful for testing purposes.
	 */
	void signalAlmostReadiness() {
		if (latch != null && latch.getCount() != 0) {
			// System.out.println("signalAlmostReadiness() with latch "+latch);
			latch.countDown();
		}
	}

	/**
	 * Used for testing purposes
	 * 
	 * @param latch
	 */
	void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	/**
	 * Used for testing purposes
	 */
	public CountDownLatch getLatch() {
		return latch;
	}

	public boolean isClosed() {
		return closed;
	}

	public void close() {
		closed = true;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				logger.error("Failed to close serverSocket", e);
			} finally {
				serverSocket = null;
			}
		}

		logger.info("closing this server");
		synchronized (socketNodeList) {
			for (LogbackSocketNode sn : socketNodeList) {
				sn.close();
			}
		}
		if (socketNodeList.size() != 0) {
			logger.warn("Was expecting a 0-sized socketNodeList after server shutdown");
		}

	}

	public void socketNodeClosing(LogbackSocketNode sn) {
		logger.debug("Removing {}", sn);

		// don't allow simultaneous access to the socketNodeList
		// (e.g. removal whole iterating on the list causes
		// java.util.ConcurrentModificationException
		synchronized (socketNodeList) {
			socketNodeList.remove(sn);
		}
	}

	static void usage(String msg) {
		System.err.println(msg);
		System.err.println("Usage: java " + LogbackServer.class.getName() + " port configFile");
		System.exit(1);
	}

	static int parsePortNumber(String portStr) {
		try {
			return Integer.parseInt(portStr);
		} catch (java.lang.NumberFormatException e) {
			e.printStackTrace();
			usage("Could not interpret port number [" + portStr + "].");
			// we won't get here
			return -1;
		}
	}

	static public void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}
}
