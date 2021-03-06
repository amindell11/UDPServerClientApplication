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
import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate.ObjectUpdate;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice;
import proto.GameStateExchangeProto.GameStateExchange.StateExchangeType;

public class GameClientThread extends Thread implements UnhandledMessageHook {
	PlayerGameManager game;
	static final int clientSendRate = 50;
	static final long updateTime = ((long)1000)/clientSendRate;
	ClientThread client;

	public PlayerGameManager getGame() {
		return game;
	}

	public GameClientThread(ClientThread client, PlayerGameManager game) {
		this.game = game;
		client.getHookManager().addHook(this);
		String schema = new Gson().toJson(game.clientObject);
		try {
			client.sendMessage(Exchange.newBuilder().setExtension(GameStateExchange.gameUpdate, GameStateExchange.newBuilder().setPurpose(StateExchangeType.OBJECT_HISTORY).build()).setId(client.getClientId()).build());
			client.sendMessage(GameConnectionUtil.buildNewObjectNotice(client.getClientId(), schema, game.clientObject.getType()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.client = client;
	}

	@Override
	public void run() {
		long lastUpdateTimestamp=System.currentTimeMillis();
		while (client.isActiveMember()) {
			long timeSinceLastUpdate = System.currentTimeMillis() - lastUpdateTimestamp;
			if (timeSinceLastUpdate < updateTime) {
				try {
					Thread.sleep(updateTime - timeSinceLastUpdate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lastUpdateTimestamp = System.currentTimeMillis();
			synchronized (game.objects) {
				update();
			}
		}
	}

	public void update() {
		GroupObjectUpdate myUpdate = GroupObjectUpdate.newBuilder().addObjects(ObjectUpdate.newBuilder().setObjectId(game.clientObject.getId()).setPosX(game.clientObject.getX()).setPosY(game.clientObject.getY()).build()).build();
		Exchange message = Exchange.newBuilder().setExtension(GameStateExchange.gameUpdate, GameStateExchange.newBuilder().setUpdatedObjectGroup(myUpdate).setPurpose(StateExchangeType.OBJECT_UPDATE).build()).setId(client.getClientId()).build();
		try {
			client.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleMessage(Exchange message) {
		if (message.hasExtension(GameStateExchange.gameUpdate)) {
			GameStateExchange update = message.getExtension(GameStateExchange.gameUpdate);
			switch (update.getPurpose()) {
			case NEW_OBJECT:
				int sourceClientId = message.getId();
				int objectId = update.getNewObject().getObjectId();
				if (sourceClientId != client.getClientId()) {
					GameObject object = GameConnectionUtil.decodeNewObjectNotice(update.getNewObject().getSchema(), update.getNewObject().getType());
					game.objects.put(objectId, object);
				} else {
					game.clientObject.setId(objectId);
				}
				break;
			case OBJECT_UPDATE:
				List<ObjectUpdate> updatedObjects = update.getUpdatedObjectGroup().getObjectsList();
				for (ObjectUpdate object : updatedObjects) {
					if (game.objects.containsKey(object.getObjectId())) {
						game.objects.get(object.getObjectId()).recievePositionUpdate((float) object.getPosX(), (float) object.getPosY());
					}
				}
				break;
			case STALE_OBJECT:
				break;
			case OBJECT_HISTORY:
				List<ObjectCreatedNotice> newObjects = update.getObjectHistory().getObjectsList();
				for (ObjectCreatedNotice object : newObjects) {
					int id = object.getObjectId();
					GameObject newOb = GameConnectionUtil.decodeNewObjectNotice(object.getSchema(), object.getType());
					game.objects.put(id, newOb);
				}
				break;
			default:
				break;
			}
		}

	}
}
