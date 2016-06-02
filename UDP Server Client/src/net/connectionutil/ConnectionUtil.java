package net.connectionutil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ConnectionUtil {
	protected DatagramSocket socket;

	public ConnectionUtil(DatagramSocket socket) {
		this.socket = socket;
	}
	protected DatagramPacket receivePacket() throws IOException {
		byte[] recvBuf = new byte[15000];
		DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
		socket.receive(receivePacket);
		return receivePacket;
	}
}
