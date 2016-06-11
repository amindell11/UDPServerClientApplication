package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class GameObject {
    public static final float friction = .0001f;
    private float width;
    private float height;
    private float mass;

    private Vector2f pos;
    private Vector2f vel;
    private Vector2f force;
    private static Vector2f zeroVector = new Vector2f(0,0);

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
	Vector2f initialForce = force.copy();
	
	//Friction
	double angle = vel.getTheta();
	Vector2f frictionForce = new Vector2f(angle);
	frictionForce.scale(friction);
	force.sub(frictionForce);

	
	Vector2f acceleration = force.copy().scale(1/mass);
	Vector2f oldVelocity = vel.copy(); //initial velocity
	vel.add(acceleration.copy().scale(delta)); //final velocity
	pos.add(oldVelocity.scale(delta)).add(vel.copy().scale(delta/2)); //x = vt + .5at^2

	if(initialForce.equals(zeroVector) && oldVelocity.getTheta() == vel.getTheta() - 180){
	    zero(vel);
	}
	
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
