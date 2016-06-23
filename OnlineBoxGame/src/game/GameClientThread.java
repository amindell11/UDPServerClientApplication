package game;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import game_object.Box;
import hooks.UnhandledMessageHook;
import net.GameConnectionUtil;
import net.client.ClientThread;
import net.proto.ExchangeProto.Exchange;
import proto.GameStateExchangeProto.GameStateExchange;
import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate.ObjectUpdate;
import proto.GameStateExchangeProto.GameStateExchange.InputState;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice;
import proto.GameStateExchangeProto.GameStateExchange.StateExchangeType;

public class GameClientThread extends Thread implements UnhandledMessageHook {
	PlayerGameManager game;
	static int clientSendRate = 20;
	ClientThread client;

	public PlayerGameManager getGame() {
		return game;
	}

	public GameClientThread(ClientThread client) {
		game = new PlayerGameManager();
		client.getHookManager().addHook(this);
		String schema = new Gson().toJson(game.clientObject);
		try {
			client.sendMessage(Exchange.newBuilder()
					.setExtension(GameStateExchange.gameUpdate,
							GameStateExchange.newBuilder().setPurpose(StateExchangeType.OBJECT_HISTORY).build())
					.setId(client.getClientId()).build());
			client.sendMessage(GameConnectionUtil.buildNewObjectNotice(client.getClientId(), schema));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.client = client;
	}

	@Override
	public void run() {
		new GameDisplayThread(game).start();

		long startTime = System.currentTimeMillis();
		while (client.isActiveMember()) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > 1000 / clientSendRate) {
				startTime = currentTime;
				synchronized (game.objects) {
					update((int) (currentTime - startTime));
				}
			}

		}
	}

	public void update(int dt) {
		game.clientObject.lastSentUpdate++;
		InputState.Builder collectInputState = game.currentInputState;
		collectInputState.setSequenceNum(game.clientObject.lastSentUpdate);
		InputState state=collectInputState.build();
		game.clientObject.pendingInputs.add(state);
		Exchange message = Exchange.newBuilder()
				.setExtension(GameStateExchange.gameUpdate,
						GameStateExchange.newBuilder().setInputState(state)
								.setPurpose(StateExchangeType.INPUT_STATE).build())
				.setId(client.getClientId()).build();
		try {
			client.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void main(String[] args) { new GameClientThread(new
	 * ClientThread(null, null)).start(); }
	 */

	@Override
	public void handleMessage(Exchange message) {
		System.out.println(message);
		if (message.hasExtension(GameStateExchange.gameUpdate)) {
			GameStateExchange update = message.getExtension(GameStateExchange.gameUpdate);
			switch (update.getPurpose()) {
			case NEW_OBJECT:
				System.out.println(message);
				int sourceClientId = message.getId();
				int objectId = update.getNewObject().getObjectId();
				if (sourceClientId != client.getClientId()) {
					Box object = new Gson().fromJson(update.getNewObject().getSchema(), Box.class);
					game.objects.put(objectId, object);
				} else {
					System.out.println("mine " + objectId);
					game.clientObject.setId(objectId);
				}
				break;
			case OBJECT_UPDATE:
				List<ObjectUpdate> updatedObjects = update.getUpdatedObjectGroup().getObjectsList();
				for (ObjectUpdate object : updatedObjects) {
					if (game.objects.containsKey(object.getObjectId())) {
						game.objects.get(object.getObjectId()).applyObjectUpdate(object);
					} else if (game.clientObject.getId() == object.getObjectId()) {
						game.clientObject.applyObjectUpdate(object);
						game.clientObject.reconcile(object.getSequenceNum());
					}
				}
				break;
			case STALE_OBJECT:
				break;
			case OBJECT_HISTORY:
				List<ObjectCreatedNotice> newObjects = update.getObjectHistory().getObjectsList();
				for (ObjectCreatedNotice object : newObjects) {
					int id = object.getObjectId();
					Box newOb = new Gson().fromJson(object.getSchema(), Box.class);
					game.objects.put(id, newOb);
				}
				break;
			default:
				break;
			}
		}

	}
}
