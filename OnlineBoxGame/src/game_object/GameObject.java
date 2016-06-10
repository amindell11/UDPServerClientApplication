package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class GameObject {
	public static final float friction = .0001f;
	float width;
	float height;
	float mass;
	
	Vector2f pos;
	Vector2f vel;
	Vector2f force;

	public GameObject(Vector2f pos, float width, float height, float mass) {
		this.width = width;
		this.height = height;
		this.pos = pos.copy();
		vel = new Vector2f(0,0);
		force = new Vector2f(0,0);
		this.mass = mass;
	}
	
	public GameObject(Vector2f pos, float width, float height) {
		this(pos, width, height, 1f);
	}

	public void update(GameContainer container, int delta) {
		if (vel.length() > .001) {
			//double angle = force.getTheta();
			//Vector2f frictionForce = new Vector2f(angle);
			//frictionForce.scale(friction);
			//force.sub(frictionForce);
		}
		else{
			zero(vel);
		}
		Vector2f acceleration = force.copy();
		vel.add(acceleration.scale(delta));
		pos.add(vel.scale(delta));
		zero(force);

	}
	
	public void zero(Vector2f v){
		v.scale(0);
	}

	public void render(GameContainer container, Graphics g) {
		g.drawRect(pos.x, pos.y, width, height);
	}
	
	public void applyForce(Vector2f force) {
		this.force.add(force);	}
	
}
