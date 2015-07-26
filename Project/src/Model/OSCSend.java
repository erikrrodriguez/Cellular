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
	
	public OSCSend(InetAddress ip, int port) throws SocketException, UnknownHostException {
		this.ip = ip;
		this.port = port;
		@SuppressWarnings("unused")
		OSCPortOut sender = new OSCPortOut();
		//default port 57110
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
	
	public void sendMsg(String message) {
		
		OSCMessage msg = new OSCMessage(message);
		try {
			sender.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

