package net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import com.google.gson.Gson;

import net.Config;
import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;

public class Server implements Runnable {
	static final boolean REQUIRE_UNIQUE_CLIENTS = false;
	ServerInfo info;
	DatagramSocket socket;
	int maxClients;
	Map<String, ClientThread> clients;
	boolean open;

	public Server(String name,int port) {
		this(name,port, Config.MAX_CLIENTS);
	}

	public Server(String name,int port, int maxClients) {
		try {
			info=new ServerInfo(InetAddress.getLocalHost().getHostAddress(),port, name, 0, maxClients);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		try {
			socket = new DatagramSocket(info.port, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);
			System.out.println("server running at "+InetAddress.getLocalHost()+" port "+info.port);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		open=true;
	}

	public Server(int port) {
		this("default",port);
	}

	public void update() throws IOException {
		System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

		// Receive a packet
		byte[] recvBuf = new byte[15000];
		DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
		socket.receive(packet);
		// Packet received
		System.out.println(getClass().getName() + ">>>Packet received from: " + packet.getAddress().getHostAddress());
		System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));
		simpleExchangeRequest req = new SimpleExchangePacket(packet.getData()).getRequest();
		System.out.println(req);
		// See if the packet holds the right command (message)
		InetAddress address=packet.getAddress();
		System.out.println(req.getRequestType());
		if (req.getRequestType().equals(RequestType.PROBE)) {
			// Send a response
			socket.send(
					new SimpleExchangePacket(ResponseType.PROBE, "").getPacket(address, packet.getPort()));

			System.out.println(getClass().getName() + ">>>Sent packet to: " + packet.getAddress().getHostAddress());
		} else if (req.getRequestType().equals(RequestType.SERVER_NAME)) {
			socket.send(new SimpleExchangePacket(ResponseType.SERVER_NAME, info.name).getPacket(address,
					packet.getPort()));
		} else if (req.getRequestType().equals(RequestType.SERVER_INFO)) {
			String msg = new Gson().toJson(info);
			socket.send(new SimpleExchangePacket(ResponseType.SERVER_INFO, msg).getPacket(address,
					packet.getPort()));

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
	public void close(){
		open=false;
		socket.close();
	}
	public static void main(String[] args) {
		new Thread(new Server("test",Config.PORT)).start();
	}
}