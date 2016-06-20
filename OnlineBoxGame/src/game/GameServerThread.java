package game;

import java.io.IOException;

import hooks.UnhandledMessageHook;
import net.proto.ExchangeProto.Exchange;
import net.server.ServerThread;
import proto.GameStateExchangeProto.GameStateExchange;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice;
import proto.GameStateExchangeProto.GameStateExchange.StateExchangeType;

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
		if (message.hasExtension(GameStateExchange.gameUpdate)) {
			GameStateExchange update = message.getExtension(GameStateExchange.gameUpdate);
			switch (update.getPurpose()) {
			case NEW_OBJECT:
				ObjectCreatedNotice ex = ObjectCreatedNotice.newBuilder(update.getNewObject()).setObjectId(0).build();
				try {
					server.announceToClients(Exchange.newBuilder().setExtension(GameStateExchange.gameUpdate,
							GameStateExchange.newBuilder().setNewObject(ex).setPurpose(StateExchangeType.NEW_OBJECT).build()).build());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case OBJECT_UPDATE:
				break;
			case STALE_OBJECT:
				break;
			default:
				break;
			}
		}
	}

}
