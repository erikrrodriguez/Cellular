package Model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.illposed.osc.*;

public class OSCSend {
	
	private OSCPortOut sender;
	private InetAddress ip;
	private int port;
	
	public OSCSend() throws SocketException, UnknownHostException {
		this.ip = InetAddress.getLocalHost();
		this.port = 57110;
		@SuppressWarnings("unused")
		OSCPortOut sender = new OSCPortOut();
	}
	
	public void sendMsg(int[] array) {
		OSCMessage msg = new OSCMessage();
		try {
			sender.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int newPort) {
		port = newPort;
	}
	
	public InetAddress getIP() {
		return ip;
	}
	
	public void setIP(String newIP) throws UnknownHostException {
		ip = InetAddress.getByName(newIP);
	}
	
	public void setIP(InetAddress newIP) {
		ip = newIP;
	}
}

