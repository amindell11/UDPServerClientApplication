package net.connectionutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import net.communication.SimpleExchangeComm.simpleExchange;

public class ConnectionUtil {
	private static DatagramSocket socket;

	public static DatagramPacket receivePacket(DatagramSocket socket) throws IOException {
		byte[] recvBuf = new byte[15000];
		DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
		socket.receive(receivePacket);
		return receivePacket;
	}
	public static DatagramPacket receivePacket(DatagramSocket socket,int timeout) throws IOException {
		socket.setSoTimeout(timeout);
		DatagramPacket receivePacket=null;
		try {
			receivePacket = receivePacket(socket);
		} catch (IOException e) {
			System.out.println("recieve timed out.");
		}
		socket.setSoTimeout(0);
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
	private static void sendBytes(byte[] bytes,DatagramSocket sourceSocket,String address,int port) throws IOException{
		InetAddress netAddress=InetAddress.getByName(address);
		sourceSocket.send(new DatagramPacket(bytes,bytes.length,netAddress,port));
	}
	public static void sendMessage(simpleExchange message,DatagramSocket sourceSocket,String address,int port) throws IOException{
		ByteArrayOutputStream aOutput = new ByteArrayOutputStream(15000);
		message.writeDelimitedTo(aOutput);
		sendBytes(aOutput.toByteArray(),sourceSocket, address, port);
	}
}
