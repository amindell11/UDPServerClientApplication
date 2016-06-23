package game_object;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate.ObjectUpdate;

public class GameObject {
	public static final float friction = .0001f;
	public static final float smooth = .01f;
	private transient int objectId;
	public transient int lastSentUpdate;
	public ArrayList<ObjectUpdate> sentUpdates;
	private float width;
	private float height;
	private float mass;

	private Vector2f pos;
	private Vector2f vel;
	private Vector2f force;
	private static Vector2f zeroVector = new Vector2f(0, 0);

	public GameObject(Vector2f pos, float width, float height, float mass) {
		this.width = width;
		this.height = height;
		this.pos = pos.copy();
		vel = new Vector2f(0, 0);
		force = new Vector2f(0, 0);
		sentUpdates = new ArrayList<ObjectUpdate>();
		applyForce(new Vector2f(.005f, 0));
		this.mass = mass;
	}

	public GameObject(Vector2f pos, float width, float height) {
		this(pos, width, height, 1f);
	}

	public void update(GameContainer container, int delta) {
		Vector2f initialForce = force.copy();
		Vector2f initialVelocity = vel.copy(); // initial velocity

		// Friction
		if (vel.length() > .001) {
			double angle = vel.getTheta();
			Vector2f frictionForce = new Vector2f(angle);
			frictionForce.scale(friction);
			force.sub(frictionForce);
		}

		Vector2f acceleration = force.copy().scale(1 / mass);
		vel.add(acceleration.copy().scale(delta)); // final velocity
		pos.add(vel.copy().add(initialVelocity.copy()).scale(delta / 2)); // x =
																			// vt
																			// +
																			// .5at^2

		if (initialForce.equals(zeroVector) && initialVelocity.getTheta() == vel.getTheta() - 180) {
			zero(vel);
		}

		smooth(vel);
		zero(force);

	}

	private void smooth(Vector2f v) {
		if (Math.abs(v.x) < smooth) {
			v.x = 0;
		}
		if (Math.abs(v.y) < smooth) {
			v.y = 0;
		}
	}

	public void applyObjectUpdate(ObjectUpdate update) {
		recievePositionUpdate(update.getPosX(), update.getPosY());
	}

	public void recievePositionUpdate(int x, int y) {
		this.pos = new Vector2f(x, y);
	}

	public void reconcile(int sequenceNum) {
		System.out.println("Non-acknowledged inputs "+(lastSentUpdate-sequenceNum));
		Iterator<ObjectUpdate> it = sentUpdates.listIterator();
		while (it.hasNext()) {
			ObjectUpdate update = it.next();
			if (update.getSequenceNum() > sequenceNum) {
				applyObjectUpdate(update);
			} else {
				it.remove();
			}
		}
	/*	if(sequenceNum<lastSentUpdate){
			System.out.println(sequenceNum);
			System.out.println(lastSentUpdate);
			applyObjectUpdate(sentUpdates.get(sentUpdates.size()-1));
		}*/
	}

	public void zero(Vector2f v) {
		v.scale(0);
	}

	public void render(GameContainer container, Graphics g) {
		g.drawRect(pos.x, pos.y, width, height);
	}

	public void setId(int id) {
		this.objectId = id;
	}

	public int getId() {
		return objectId;
	}

	public void applyForce(Vector2f force) {
		this.force.add(force);
	}

	public int getX() {
		return (int) pos.x;
	}

	public int getY() {
		return (int) pos.y;
	}
}
