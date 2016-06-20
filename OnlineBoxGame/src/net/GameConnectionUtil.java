package net;

import net.proto.ExchangeProto.Exchange;
import proto.GameStateExchangeProto.GameStateExchange;
import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice;
import proto.GameStateExchangeProto.GameStateExchange.StateExchangeType;

public class GameConnectionUtil {
	public static Exchange buildNewObjectNotice(int clientId, String schema) {
		Exchange message = Exchange.newBuilder().setId(clientId)
				.setExtension(GameStateExchange.gameUpdate,
						GameStateExchange.newBuilder().setPurpose(StateExchangeType.NEW_OBJECT)
								.setNewObject(ObjectCreatedNotice.newBuilder().setSchema(schema)).build())
				.build();
		return message;
	}

	public static Exchange buildNewObjectNotice(int clientId, int objectId, String schema) {
		Exchange message = Exchange.newBuilder().setId(clientId).setExtension(GameStateExchange.gameUpdate,
				GameStateExchange.newBuilder().setPurpose(StateExchangeType.NEW_OBJECT)
						.setNewObject(ObjectCreatedNotice.newBuilder().setSchema(schema).setObjectId(objectId)).build())
				.build();
		return message;
	}
}
