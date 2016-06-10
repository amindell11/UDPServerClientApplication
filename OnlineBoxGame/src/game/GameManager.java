package game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import game_object.GameObject;

/**
 * @author Ari
 */
public class GameManager extends BasicGame {
	List<GameObject> objects;
	GameObject clientObject;
	String username;

	public GameManager() {
		super("Box game");
		this.username = "";
		objects = new ArrayList<>();
		clientObject=new GameObject(new Vector2f(10,10),50,50);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		synchronized (objects) {
			for (GameObject object : objects) {
				object.update(container, delta);
			}
		}
				
		//TODO figure a better way to handle input
		Input input=new Input(0);
		if(input.isKeyDown(Input.KEY_LEFT)){
			clientObject.applyForce(new Vector2f(-.001f,0));
		}
		if(input.isKeyDown(Input.KEY_RIGHT)){
			clientObject.applyForce(new Vector2f(.001f,0));
		}
		if(input.isKeyDown(Input.KEY_DOWN)){
			clientObject.applyForce(new Vector2f(0,.001f));
		}
		if(input.isKeyDown(Input.KEY_UP)){
			clientObject.applyForce(new Vector2f(0,-.001f));
		}
		
		clientObject.update(container, delta);
		
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.drawString(username, 10, 30);
		clientObject.render(container, g);
	}

	public void setUsername(String username) {
		this.username = username;
	}
}