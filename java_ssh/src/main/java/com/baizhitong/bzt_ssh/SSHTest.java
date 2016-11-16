package com.baizhitong.bzt_ssh;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHTest {

	public static void main(String[] args) throws Exception {

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

		String host = "192.168.0.29";
		String user = "root";
		String password = "QKO7=Ziul0";

		JSch jsch = new JSch();

		final Session session = jsch.getSession(user, host, 22);
		session.setPassword(password);
		session.setUserInfo(new MyUserInfo());
		session.connect(30000);

		Scanner scan = new Scanner(System.in);
		while (true) {
			String cmd = scan.nextLine();
			if ("ctrlc".equals(cmd)) { // ctrlc 这种情况
				interruptChannel();
			} else {
				sendCommand(session, cmd);
			}
		}
	}

	private static Channel lastExecChannel;

	private static void sendCommand(Session session, String command) {
		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.connect();

			// channel.setInputStream(System.in);
			channel.setOutputStream(System.out);

			lastExecChannel = channel;
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
				lastExecChannel.sendSignal("2");
				lastExecChannel.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
