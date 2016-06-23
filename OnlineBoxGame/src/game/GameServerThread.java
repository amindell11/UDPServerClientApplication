package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import game_object.GameObject;
import hooks.UnhandledMessageHook;
import net.GameConnectionUtil;
import net.proto.ExchangeProto.Exchange;
import net.server.ServerThread;
import proto.GameStateExchangeProto.GameStateExchange;
import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate;
import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate.ObjectUpdate;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice;
import proto.GameStateExchangeProto.GameStateExchange.ObjectHistory;
import proto.GameStateExchangeProto.GameStateExchange.StateExchangeType;

/**
 * This class manages the game update messages sent from clients. It recieves
 * messages from clients and decides what to send to other clients as updates.
 * 
 * @author Josh
 *
 */
public class GameServerThread extends Thread implements UnhandledMessageHook {
	static List<Integer> usedIds;
	private ServerThread server;
	static int serverSendRate = 50;
	GameManager game;

	/**
	 * @param server
	 *            The object running the server thread
	 */
	GameServerThread(ServerThread server) {
		this.server = server;
		server.getHookManager().addHook(this);
		game = new SpectatorGameManager();
	}

	public void update() {
		List<ObjectUpdate> updatedObjectList = new ArrayList<>();
		for (int id : game.objects.keySet()) {
			GameObject gameObject = game.objects.get(id);
			updatedObjectList.add(ObjectUpdate.newBuilder().setObjectId(id).setPosX(gameObject.getX())
					.setPosY(gameObject.getY()).setSequenceNum(gameObject.lastSentUpdate).build());
		}
		GroupObjectUpdate myUpdate = GroupObjectUpdate.newBuilder().addAllObjects(updatedObjectList).build();

		Exchange message = Exchange.newBuilder()
				.setExtension(GameStateExchange.gameUpdate, GameStateExchange.newBuilder()
						.setUpdatedObjectGroup(myUpdate).setPurpose(StateExchangeType.OBJECT_UPDATE).build())
				.setId(0).build();
		try {
			server.announceToClients(message);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			int sourceClientId = message.getId();
			GameStateExchange update = message.getExtension(GameStateExchange.gameUpdate);
			switch (update.getPurpose()) {
			case NEW_OBJECT:
				int objectId = assignObjectId();
				GameObject object = new Gson().fromJson(update.getNewObject().getSchema(), GameObject.class);
				game.objects.put(objectId, object);
				try {
					server.announceToClients(GameConnectionUtil.buildNewObjectNotice(sourceClientId, objectId,
							update.getNewObject().getSchema()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case OBJECT_UPDATE:
				List<ObjectUpdate> updatedObjects = update.getUpdatedObjectGroup().getObjectsList();
				for (ObjectUpdate updatedObject : updatedObjects) {
					System.out.println(updatedObject.getObjectId());
					if (game.objects.containsKey(updatedObject.getObjectId())) {
						game.objects.get(updatedObject.getObjectId()).applyObjectUpdate(updatedObject);
						game.objects.get(updatedObject.getObjectId()).lastSentUpdate=updatedObject.getSequenceNum();
					}
				}
				break;
			case STALE_OBJECT:
				break;
			case OBJECT_HISTORY:
				List<ObjectCreatedNotice> newObjects = new ArrayList<>();
				for (int id : game.objects.keySet()) {
					String schema = new Gson().toJson(game.objects.get(id));
					newObjects.add(ObjectCreatedNotice.newBuilder().setObjectId(id).setSchema(schema).build());
				}
				Exchange response = Exchange.newBuilder()
						.setExtension(GameStateExchange.gameUpdate,
								GameStateExchange.newBuilder()
										.setObjectHistory(ObjectHistory.newBuilder().addAllObjects(newObjects).build())
										.setPurpose(StateExchangeType.OBJECT_HISTORY).build())
						.setId(0).build();
				try {
					server.sendMessage(response, message.getId());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}

	public int assignObjectId() {
		if (usedIds == null) {
			usedIds = new ArrayList<>();
		}
		int generatedId = 1;
		while (usedIds.contains(generatedId)) {
			generatedId++;
		}
		usedIds.add(generatedId);
		return generatedId;

	}
}
