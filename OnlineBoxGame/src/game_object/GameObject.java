package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.ObjectCreatedNotice.GameObjectType;

public abstract class GameObject {
    public GameObject(Vector2f pos, float width, float height) {
	this.width = width;
	this.height = height;
	this.pos = pos.copy();

    }
    protected Vector2f pos;
    protected float width;
    protected float height;
    public void recievePositionUpdate(float x,float y){
    	this.pos=new Vector2f(x,y);
    }

	public abstract GameObjectType getType();

	public abstract void render(GameContainer container, Graphics g);

	public abstract void update(GameContainer container, int delta);
	
	public abstract void sing();
    public float getX(){
    	return (int) pos.x;
    }
    public float getY(){
    	return (int) pos.y;
    }
}
