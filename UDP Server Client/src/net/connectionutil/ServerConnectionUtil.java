package net.connectionutil;

import java.net.DatagramSocket;

public class ServerConnectionUtil extends ConnectionUtil{

	public ServerConnectionUtil(DatagramSocket socket) {
		super(socket);
	}

}
