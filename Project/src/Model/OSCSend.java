package Model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.illposed.osc.*;

public class OSCSend {

	private InetAddress ip;
	private int port;

	public OSCSend() throws SocketException, UnknownHostException {
		this.ip = InetAddress.getLocalHost();
		this.port = 57110;
	}

	public void sendMsg(int[] array) throws UnknownHostException,
			SocketException {
		OSCPortOut sender = new OSCPortOut(ip, port);
		OSCMessage toMax = new OSCMessage("/coornotes/");

		for (int i = 0; i < array.length; i++) {
			toMax.addArgument(array[i]);
		}
		try {
			sender.send(toMax);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

	public InetAddress getIP() {
		return ip;
	}

	public void setIP(String newIp) throws UnknownHostException {
		if (newIp.equals("localhost")) {
			this.ip = InetAddress.getLocalHost();
		} else if ((newIp.length() - newIp.replace(".", "").length()) == 3) {
			ip = InetAddress.getByName(newIp);
		}
	}

	public void setPort(String newPort) {
		if (newPort.length() >= 5) {
			port = Integer.parseInt(newPort);
		}
	}
}
