package com.baizhitong.bzt_ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public enum SSHExecuter {

	ISTANCE;

	private static long interval = 1000L;
	private static int timeout = 3000;
	private SshInfo sshInfo = null;
	private JSch jsch = null;
	private Session session = null;
	private Logger logger=LoggerFactory.getLogger(getClass());
	PipedInputStream pipeIn = new PipedInputStream();
	PipedOutputStream pipeOut;
	
	public static final String CTRLC="ctrlc";
	
	private SSHExecuter(){

		String host = "";
		Integer port = 22;
		String user = "";
		String key = "";
		String passPhrase = "";
		String password="";
		
		try{
			pipeOut=new PipedOutputStream(pipeIn);
			Properties prop=new Properties();  //读配置文件
			prop.load(SSHExecuter.class.getClassLoader().getResourceAsStream("settings.properties"));
			host=prop.getProperty("host");
			port=Integer.parseInt(prop.getProperty("port"));
			user=prop.getProperty("user");
			password=prop.getProperty("password");
		}catch(Exception e){
			logger.error("",e);
		}
		
		sshInfo = new SshInfo(host, port, user, key, passPhrase,password); //将ssh信息存在内存中
		jsch = new JSch();
		try {
			connectServer(user, host, port,password);  //连接服务器
		} catch (Exception e) {
			logger.error("",e);
		}
		
	}

	public void shell(String cmd) throws Exception{
		if(!session.isConnected()){
			connectServer(sshInfo.getUser(),sshInfo.getHost(),sshInfo.getPort(),sshInfo.getPassword());
			shell();
			Thread.sleep(interval);
		}
		cmd+="\n";
		pipeOut.write(cmd.getBytes());
	}
	
	private void connectServer(String user, String host, Integer port, String password) throws JSchException {
		session = jsch.getSession(user, host, port);
		session.setPassword(password);
		session.setUserInfo(new MyUserInfo());
		session.connect(timeout);
	}

	public long shell() throws Exception {
		long start = System.currentTimeMillis();
		ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
		OutputStream out=System.out;
		channelShell.setInputStream(pipeIn,true);
		channelShell.setOutputStream(out,true);
		channelShell.connect(timeout);
		return System.currentTimeMillis() - start;
	}

	public int exec(String cmd) throws Exception {
		PrintStream defaultOut = System.out;
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		if(CTRLC.equals(cmd)){
			if(session.isConnected()){
				channelExec.sendSignal("KILL");
			}
			return 0;
		}else{
			channelExec.setCommand(cmd);
		}
		channelExec.setInputStream(null);
		channelExec.setErrStream(System.err,true);
		InputStream in = channelExec.getInputStream();
		//这个true很关键
		channelExec.setOutputStream(defaultOut,true);
		channelExec.connect();
		int res = -1;
		StringBuffer buf = new StringBuffer(1024);
		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				buf.append(new String(tmp, 0, i));
			}
			if (channelExec.isClosed()) {
				res = channelExec.getExitStatus();
				//System.out.println("Exit-status:" + res);
				break;
			}
		}
		//System.out.println(buf.toString());
		channelExec.disconnect();
		return res;
	}

	public Session getSession() {
		return session;
	}

	public void close() throws IOException {
		getSession().disconnect();
	}

	public static class SshInfo {
		String host = null;
		Integer port = 22;
		String user = null;
		String key = null;
		String passPhrase = null;
		String password=null;

		public SshInfo(String host, Integer port, String user, String key, String passPhrase,String password) {
			super();
			this.host = host;
			this.port = port;
			this.user = user;
			this.key = key;
			this.passPhrase = passPhrase;
			this.password=password;
		}

		public String getHost() {
			return host;
		}

		public Integer getPort() {
			return port;
		}

		public String getUser() {
			return user;
		}

		public String getKey() {
			return key;
		}

		public String getPassPhrase() {
			return passPhrase;
		}
		
		public String getPassword(){
			return password;
		}
		
	}

}
