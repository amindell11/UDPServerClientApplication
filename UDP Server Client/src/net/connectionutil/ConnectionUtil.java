package net.connectionutil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ConnectionUtil {
	private static DatagramSocket socket;

	protected static DatagramPacket receivePacket() throws IOException {
		byte[] recvBuf = new byte[15000];
		DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
		getUtilSocket().receive(receivePacket);
		return receivePacket;
	}
	protected static DatagramPacket receivePacket(int timeout) throws IOException {
		getUtilSocket().setSoTimeout(timeout);
		DatagramPacket receivePacket=null;
		try {
			receivePacket = receivePacket();
		} catch (IOException e) {
			System.out.println("recieve timed out.");
		}
		getUtilSocket().setSoTimeout(0);
		return receivePacket;
	}
	protected static DatagramSocket getUtilSocket(){
		if(socket==null){
			try {
				socket=new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		return socket;
	}
}
