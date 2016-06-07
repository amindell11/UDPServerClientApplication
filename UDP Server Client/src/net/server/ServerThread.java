package net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange;

public class ServerThread extends Thread {
	static final boolean REQUIRE_UNIQUE_CLIENTS = false;
	ServerInfo info;
	ServerSecretary secretary;

	public ServerInfo getInfo() {
		return info;
	}

	DatagramSocket socket;
	int maxClients;
	Map<Integer, Client> clients;
	boolean open;

	public ServerThread(String name, int port) {
		this(name, port, Config.MAX_CLIENTS);
	}

	public ServerThread(String name, int port, int maxClients) {
		try {
			info = new ServerInfo(InetAddress.getLocalHost().getHostAddress(),
					port, name, 0, maxClients);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		secretary = new ServerSecretary(this);
	}

	public void init() {
		try {
			socket = new DatagramSocket(info.port,
					InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);
			clients = new HashMap<Integer, Client>();
			System.out.println("server running at "
					+ InetAddress.getLocalHost() + " port " + info.port);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		open = true;
	}

	public ServerThread(int port) {
		this("default", port);
	}

	public void update() throws IOException {

		// Receive a packet
		byte[] recvBuf = new byte[15000];
		DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
		if(clients.size()==0){
			System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

			socket.receive(packet);

			// Packet received
			System.out.println(getClass().getName() + ">>>Packet received from: "+ packet.getAddress().getHostAddress());
			simpleExchange msg = new SimpleExchangePacket(packet.getData()).getMessage();
			System.out.println(msg);

			if (msg.hasId()) {
				System.out.println("forwarding packet to client handle method");
				clients.get(msg.getId()).handleMessage(msg);
			}
			secretary.handleRequest(msg.getRequest(), packet);
		}
	}

	@Override
	public void run() {
		init();
		while (open) {
			try {
				update();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		open = false;
		socket.close();
	}

	public static void main(String[] args) {
		new ServerThread("test", Config.PORT).start();
	}
}
