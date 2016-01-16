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
	
	public void sendMsg(int[] array) throws UnknownHostException, SocketException {
		OSCPortOut sender = new OSCPortOut(ip, port);
		OSCMessage toMax = new OSCMessage("/coornotes/");
		
		for(int i = 0; i < array.length; i++) {
			toMax.addArgument(array[i]);
		}
		try {
			sender.send(toMax);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		OSCMessage coorMsg = new OSCMessage("/coor/");
//		coorMsg.addArgument(array[0]);
//		coorMsg.addArgument(array[1]);
//		try {
//			sender.send(coorMsg);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		OSCMessage noteMsg = new OSCMessage("/notes/");
//		for(int i=2; i < array.length; i++) {
//			noteMsg.addArgument(array[i]);
//		}
//		try {
//			sender.send(noteMsg);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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

