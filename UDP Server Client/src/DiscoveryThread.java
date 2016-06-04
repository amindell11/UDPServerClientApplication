import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.SimpleExchangePacket;
import net.communication.SimpleExchangeComm;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeRequest.RequestType;
import net.communication.SimpleExchangeComm.simpleExchange.simpleExchangeResponse.ResponseType;

public class DiscoveryThread implements Runnable {

	DatagramSocket socket;

	@Override
	public void run() {
		try {
			// Keep a socket open to listen to all the UDP trafic that is
			// destined for this port
			socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (true) {
				System.out.println(getClass().getName() + ">>>Ready to receive broadcast packets!");

				// Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);
				// Packet received
				System.out.println(
						getClass().getName() + ">>>Packet received from: " + packet.getAddress().getHostAddress());
				System.out.println(getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()));
				simpleExchangeRequest req = new SimpleExchangePacket(packet.getData()).getRequest();
				// See if the packet holds the right command (message)
				if (req.getRequestType().equals(RequestType.PROBE)) {

					// Send a response
					socket.send(new SimpleExchangePacket(ResponseType.PROBE, "").getPacket(packet.getAddress(),
							packet.getPort()));

					System.out.println(
							getClass().getName() + ">>>Sent packet to: " + packet.getAddress().getHostAddress());
				} else if (req.getRequestType().equals(RequestType.SERVER_NAME)) {
					socket.send(new SimpleExchangePacket(ResponseType.SERVER_NAME, "test").getPacket(packet.getAddress(),
							packet.getPort()));
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static DiscoveryThread getInstance() {
		return DiscoveryThreadHolder.INSTANCE;
	}

	private static class DiscoveryThreadHolder {
		private static final DiscoveryThread INSTANCE = new DiscoveryThread();
	}

	public static void main(String[] args) {
		Thread discoveryThread = new Thread(DiscoveryThread.getInstance());
		discoveryThread.start();
	}
}