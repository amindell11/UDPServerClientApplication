package game;

import hooks.UnhandledMessageHook;
import net.proto.ExchangeProto.Exchange;
import net.server.ServerThread;

/**
 * This class manages the game update messages sent from clients. It recieves
 * messages from clients and decides what to send to other clients as updates.
 * 
 * @author Josh
 *
 */
public class GameServerThread extends Thread implements UnhandledMessageHook {

	private ServerThread server;
	static int serverSendRate = 30;

	/**
	 * @param server
	 *            The object running the server thread
	 */
	GameServerThread(ServerThread server) {
		this.server = server;
		server.getHookManager().addHook(this);

	}

	public void update() {

	}

	public void run() {
		long startTime = System.currentTimeMillis();
		while (server.isOpen()) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > 1000 / serverSendRate) {
				startTime = currentTime;
				update();
			}

		}

	}

	@Override
	public void handleMessage(Exchange message) {

	}

}
