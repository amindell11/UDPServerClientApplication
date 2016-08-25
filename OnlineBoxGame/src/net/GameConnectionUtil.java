package net;

import com.google.gson.Gson;

import game_object.Box;
import game_object.GameObject;
import game_object.GameObjectChild;
import net.proto.ExchangeProto.Exchange;
import proto.GameStateExchangeProto.GameStateExchange;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice.GameObjectType;
import proto.GameStateExchangeProto.GameStateExchange.StateExchangeType;

public class GameConnectionUtil {
	public static Exchange buildNewObjectNotice(int clientId, String schema, GameObjectType type) {
		Exchange message = Exchange.newBuilder().setId(clientId).setExtension(GameStateExchange.gameUpdate, GameStateExchange.newBuilder().setPurpose(StateExchangeType.NEW_OBJECT).setNewObject(ObjectCreatedNotice.newBuilder().setSchema(schema).setType(type)).build()).build();
		return message;
	}

	public static Exchange buildNewObjectNotice(int clientId, int objectId, String schema, GameObjectType type) {
		Exchange message = Exchange.newBuilder().setId(clientId).setExtension(GameStateExchange.gameUpdate, GameStateExchange.newBuilder().setPurpose(StateExchangeType.NEW_OBJECT).setNewObject(ObjectCreatedNotice.newBuilder().setSchema(schema).setType(type).setObjectId(objectId)).build()).build();
		return message;
	}

	public static GameObject decodeNewObjectNotice(String schema, GameObjectType type) {
		switch (type) {
		case GAME_OBJECT:
			return decodeObject(schema, Box.class);
		case GAME_OBJECT_CHILD:
			return decodeObject(schema, GameObjectChild.class);
		default:
			return null;
		}

	}

	private static <T extends GameObject> T decodeObject(String schema, Class<T> type) {
		return new Gson().fromJson(schema, type);
	}
}
