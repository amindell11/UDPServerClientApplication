package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class GameObject {
	public static final float friction = .0001f;
	float width;
	float height;
	float x;
	float y;
	float velocity;
	float force;
	float mass;

	public GameObject(float x, float y, float width, float height) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		mass = 1f;
	}

	public void update(GameContainer container, int delta) {
		if (Math.abs(velocity)>.001) {
			force -= friction * Math.abs(velocity) / velocity;
		}
		System.out.println(force);
		float acc = force / mass;
		velocity = velocity + acc * delta;
		x += velocity * delta;
		force=0;

	}

	public void render(GameContainer container, Graphics g) {
		g.drawRect(x, y, width, height);
	}

	public void applyForce(float force) {
		this.force = force;
	}
}
