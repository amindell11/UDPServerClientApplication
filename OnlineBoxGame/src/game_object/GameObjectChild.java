package game_object;

import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice.GameObjectType;

public class GameObjectChild extends Box {

	public GameObjectChild(Vector2f pos, float width, float height) {
		super(pos, width, height);
	}
	@Override
	public void sing(){
		System.out.println("Game Object Child");
	}
	@Override
	public GameObjectType getType(){
		return GameObjectType.GAME_OBJECT_CHILD;
	}

}
