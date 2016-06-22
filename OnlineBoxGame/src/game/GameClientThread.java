package game;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import game_object.GameObject;
import hooks.UnhandledMessageHook;
import net.GameConnectionUtil;
import net.client.ClientThread;
import net.proto.ExchangeProto.Exchange;
import proto.GameStateExchangeProto.GameStateExchange;
import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate;
import proto.GameStateExchangeProto.GameStateExchange.StateExchangeType;
import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate.ObjectUpdate;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice;

public class GameClientThread extends Thread implements UnhandledMessageHook {
	PlayerGameManager game;
	static int clientSendRate = 50;
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
					update();
				}
			}

		}
	}

	public void update() {
		System.out.println(game.clientObject.getId());
		GroupObjectUpdate myUpdate = GroupObjectUpdate.newBuilder()
				.addObjects(ObjectUpdate.newBuilder().setObjectId(game.clientObject.getId())
						.setPosX(game.clientObject.getX()).setPosY(game.clientObject.getY()).build())
				.build();
		Exchange message = Exchange.newBuilder()
				.setExtension(GameStateExchange.gameUpdate,
						GameStateExchange.newBuilder().setUpdatedObjectGroup(myUpdate)
								.setPurpose(StateExchangeType.OBJECT_UPDATE).build())
				.setId(client.getClientId()).build();
		System.out.println(message);
		try {
			client.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GameClientThread(new ClientThread(null, null)).start();
	}

	@Override
	public void handleMessage(Exchange message) {
		if (message.hasExtension(GameStateExchange.gameUpdate)) {
			GameStateExchange update = message.getExtension(GameStateExchange.gameUpdate);
			switch (update.getPurpose()) {
			case NEW_OBJECT:
				System.out.println(message);
				int sourceClientId = message.getId();
				int objectId = update.getNewObject().getObjectId();
				if (sourceClientId != client.getClientId()) {
					GameObject object = new Gson().fromJson(update.getNewObject().getSchema(), GameObject.class);
					game.objects.put(objectId, object);
				} else {
					System.out.println("mine "+objectId);
					game.clientObject.setId(objectId);
				}
				break;
			case OBJECT_UPDATE:
				List<ObjectUpdate> updatedObjects = update.getUpdatedObjectGroup().getObjectsList();
				for (ObjectUpdate object : updatedObjects) {
					if (game.objects.containsKey(object.getObjectId())) {
						game.objects.get(object.getObjectId()).recievePositionUpdate(object.getPosX(),
								object.getPosY());
					}
				}
				break;
			case STALE_OBJECT:
				break;
			case OBJECT_HISTORY:
				List<ObjectCreatedNotice> newObjects = update.getObjectHistory().getObjectsList();
				for (ObjectCreatedNotice object : newObjects) {
					int id = object.getObjectId();
					GameObject newOb = new Gson().fromJson(object.getSchema(), GameObject.class);
					game.objects.put(id, newOb);
				}
				break;
			default:
				break;
			}
		}

	}
}
