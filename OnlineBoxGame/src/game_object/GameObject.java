package game_object;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice.GameObjectType;

public abstract class GameObject {
	private boolean initialized;
	protected transient Shape collisionModel;

	public GameObject(Vector2f pos, float width, float height) {
		this.width = width;
		this.height = height;
		this.pos = pos.copy();
		initialized = false;
		collisionModel=getCollisionInstance();
	}

	protected Vector2f pos;
	protected float width;
	protected float height;

	public void recievePositionUpdate(float x, float y) {
		this.pos = new Vector2f(x, y);
	}

	protected void init(GameContainer container) {
		updateCollisionModel();
		System.out.println("init complete");
	}

	public abstract GameObjectType getType();

	public void render(GameContainer container, Graphics g){
		g.draw(collisionModel);
	}

	public void update(GameContainer container, int delta) {
		if (!initialized) {
			init(container);
			initialized = true;
		}
		updateCollisionModel();
	}
	private void updateCollisionModel(){
		Shape collisionTemp=getCollisionInstance().transform(Transform.createTranslateTransform(pos.getX()-width/2, pos.getY()-height/2));
		collisionModel=collisionTemp;
	}
	public Shape getCollisionInstance(){
		return new Circle(height/2, height/2, height/2);
	}
	
	public abstract boolean collidesWith(GameObject obj);
	
	public boolean isCollidingWith(GameObject obj) {
		return false;
		//System.out.println("obj "+this+"col"+" checking for collision with "+obj+"collision model "+obj.collisionModel);
		/*if (collisionModel.intersects(obj.collisionModel)&&this.collidesWith(obj))
			return true;
		else
			return false;*/
	}

	public GameObject isCollidingWith(ArrayList<GameObject> CollisionList) {
		for (GameObject CollisionObject : CollisionList) {
			if (isCollidingWith(CollisionObject)) {
				return CollisionObject;
			}
		}
		return null;
	}

	public abstract void sing();

	public float getX() {
		return (int) pos.x;
	}

	public float getY() {
		return (int) pos.y;
	}
}
