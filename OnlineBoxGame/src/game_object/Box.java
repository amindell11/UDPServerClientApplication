package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice.GameObjectType;

public class Box extends GameObject{
    public static final float friction = .5f;
    public static final float smooth = .01f;
    private transient int objectId;
    private float mass;

    private Vector2f vel;
    private Vector2f force;
    private static Vector2f zeroVector = new Vector2f(0,0);
    public Box(Vector2f pos, float width, float height){
    	this(pos,width,height,.2f);
    }
    public Box(Vector2f pos, float width, float height, float mass) {
    super(pos,width,height);
    vel = new Vector2f(0,0);
	force = new Vector2f(0,0);
	this.mass = mass;
    }

    public void update(GameContainer container, int delta) {
    super.update(container, delta);
	Vector2f initialForce = force.copy();
	Vector2f initialVelocity = vel.copy(); //initial velocity

	//Friction
	double angle = vel.getTheta();
	Vector2f frictionForce = new Vector2f(angle);
	frictionForce.scale(friction*vel.length());
	force.sub(frictionForce);

	Vector2f acceleration = force.copy().scale(1/mass);
	vel.add(acceleration.copy().scale((float)(delta)/1000)); //final velocity
	pos.add(vel.copy().add(initialVelocity.copy()).scale(delta/2)); //x = vt + .5at^2

	if(initialForce.equals(zeroVector) && initialVelocity.getTheta() == vel.getTheta() - 180){
	    zero(vel);
	}

	smooth(vel);
	zero(force);

    }
    public void sing(){
    	System.out.println("Game Object");
    }

    private void smooth(Vector2f v){
	if(Math.abs(v.x) < smooth){
	    v.x = 0;
	}
	if(Math.abs(v.y) < smooth){
	    v.y = 0;
	}
    }

    public void zero(Vector2f v){
	v.scale(0);
    }

    public void render(GameContainer container, Graphics g) {
	g.drawRect(pos.x, pos.y, width, height);
    }
    public void setId(int id){
    	this.objectId=id;
    }
    public int getId(){
    	return objectId;
    }
    public void applyForce(Vector2f force) {
	this.force.add(force);	}


	@Override
	public GameObjectType getType() {
		return GameObjectType.GAME_OBJECT;
	}
}
