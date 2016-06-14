import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.connectionutil.ConnectionUtil;
import net.proto.ExchangeProto.Exchange;
import net.proto.SimpleExchangeProto.SimpleExchange;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeRequest.RequestType;
import net.proto.SimpleExchangeProto.SimpleExchange.SimpleExchangeResponse.ResponseType;

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
				DatagramPacket packet=ConnectionUtil.receivePacket(socket);
				Exchange simpleExchange=ConnectionUtil.convertMessage(packet.getData());
				System.out.println(simpleExchange);
				SimpleExchangeRequest req=simpleExchange.getExtension(SimpleExchange.simpleExchange).getRequest();
				if (req.getRequestType().equals(RequestType.PROBE)) {
				    System.out.println(true);
/*					syso
					// Send a response
					ConnectionUtil.sendResponse(ResponseType.PROBE, "", 0, socket, packet.getAddress(), packet.getPort());
					System.out.println(
							getClass().getName() + ">>>Sent packet to: " + packet.getAddress().getHostAddress());
				} else if (req.getRequestType().equals(RequestType.SERVER_NAME)) {
					socket.send(new SimpleExchangePacket(ResponseType.SERVER_NAME, "test").getPacket(packet.getAddress(),
							packet.getPort()));
				}*/
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