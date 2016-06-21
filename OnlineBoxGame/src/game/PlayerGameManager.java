package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import game_object.GameObject;

public class PlayerGameManager extends GameManager {
	GameObject clientObject;
	String username;

	public PlayerGameManager() {
		super();
		this.username = "";
		clientObject = new GameObject(new Vector2f(10, 10), 50, 50);
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		Input input = new Input(0);
		if (input.isKeyDown(Input.KEY_LEFT)) {
			clientObject.applyForce(new Vector2f(-.001f, 0));
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			clientObject.applyForce(new Vector2f(.001f, 0));
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			clientObject.applyForce(new Vector2f(0, .001f));
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			clientObject.applyForce(new Vector2f(0, -.001f));
		}

		clientObject.update(gc, delta);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException{
		super.render(gc,g);
		g.drawString(username, 10, 30);
		clientObject.render(gc, g);
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
