package game_object;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class GameObject {
	float width;
	float height;
	Vector2f pos;
	Vector2f speed; 
	public GameObject(float x,float y,float width,float height){
		this.width=width;
		this.height=height;
		this.pos=new Vector2f(x,y);
		speed=new Vector2f(0,0);
	}
	
	public void update(GameContainer container, int delta){
		pos.add(speed.scale(delta));
	}
	public void render(GameContainer container, Graphics g){
		
	}
}
