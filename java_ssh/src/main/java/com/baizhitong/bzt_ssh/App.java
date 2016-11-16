package com.baizhitong.bzt_ssh;

import java.io.OutputStream;
import java.util.Scanner;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Hello world!
 *
 */
public class App {

	private static ChannelExec lastExecChannel;

	private static void sendCommand(Session session, String command) {
		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.connect();

			lastExecChannel = (ChannelExec) channel;

			lastExecChannel.setInputStream(System.in, true);
			lastExecChannel.setOutputStream(System.out, true);
			lastExecChannel.setExtOutputStream(System.out, true);
			lastExecChannel.setErrStream(System.out, true);

			OutputStream os = lastExecChannel.getOutputStream();
			if(os != null){
				os.write("PS1=\"MY_PROMPT>\"".getBytes());
				os.write("\n".getBytes());
				os.write("TERM=ansi".getBytes());
				os.write("\n".getBytes());
				os.write(command.getBytes());
				os.write("\n".getBytes());
			}
			
		} catch (JSchException jschX) {
			jschX.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private static void interruptChannel() {
		try {
			if (lastExecChannel != null) {
				if (lastExecChannel.isConnected()) {
					lastExecChannel.sendSignal("2");
					lastExecChannel.disconnect();
				}
			}
			lastExecChannel = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		String host = args[0];
		String port = args[1]; 
		String user = args[2]; 
		String password = args[3]; 
		
//		String host = "192.168.0.29";
//		String port = "22"; 
//		String user = "root"; 
//		String password = "QKO7=Ziul0"; 
		

		JSch jsch = new JSch();

		final Session session = jsch.getSession(user, host, Integer.parseInt(port));
		session.setPassword(password);
		session.setUserInfo(new MyUserInfo());
		session.connect(30000);

		Scanner scan = new Scanner(System.in) ;
		while (true) {
			String cmd = scan.nextLine();
			// 空命令，直接结束
			if (cmd == null) {
				continue;
			}
			String trimCmd = cmd.replaceAll("\r", "").replaceAll("\n", "");
			if ("".equals(trimCmd.trim())) {
				continue;
			}

			// 中断当前的一个命令
			if ("ctrlc".equals(cmd)) { // ctrlc 这种情况
				interruptChannel();
			} else {
				sendCommand(session, cmd);
			}
		}

		// SSHExecuter.ISTANCE.shell(); //开启shell
		// Scanner scan=new Scanner(System.in);
		// while(true){
		// String cmd=scan.nextLine();
		// if(SSHExecuter.ISTANCE.CTRLC.equals(cmd)){ //ctrlc 这种情况
		// SSHExecuter.ISTANCE.exec(cmd);
		// }else{
		// SSHExecuter.ISTANCE.shell(cmd);
		// }
		// }
	}
}
