package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice.GameObjectType;

public class GameObjectChild extends Box {
	transient Image img;
	String imgPath;
	public GameObjectChild(Vector2f pos, float width, float height) {
		super(pos, width, height);
	}

	@Override
	protected void init(GameContainer gc) {
		try {
			img = new Image("img-thing.jpg");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sing() {
		System.out.println("Game Object Child");
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		if(img!=null)img.draw(pos.x, pos.y, width, height);
	}

	@Override
	public GameObjectType getType() {
		return GameObjectType.GAME_OBJECT_CHILD;
	}

}
