package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice.GameObjectType;

public abstract class GameObject {
	private boolean initialized;

	public GameObject(Vector2f pos, float width, float height) {
		this.width = width;
		this.height = height;
		this.pos = pos.copy();
		initialized = false;
	}

	protected Vector2f pos;
	protected float width;
	protected float height;

	public void recievePositionUpdate(float x, float y) {
		this.pos = new Vector2f(x, y);
	}

	protected void init(GameContainer container) {
		System.out.println("initializing object");
	}

	public abstract GameObjectType getType();

	public abstract void render(GameContainer container, Graphics g);

	public void update(GameContainer container, int delta) {
		System.out.println(initialized);
		if (!initialized) {
			init(container);
			initialized = true;
		}
	}

	public abstract void sing();

	public float getX() {
		return (int) pos.x;
	}

	public float getY() {
		return (int) pos.y;
	}
}
