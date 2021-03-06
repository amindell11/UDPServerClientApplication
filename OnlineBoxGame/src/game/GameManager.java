package game;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import game_object.GameObject;

/**
 * @author Ari7
 */
public abstract class GameManager extends BasicGame {
	Map<Integer,GameObject> objects;
	public GameManager() {
		super("Box game");
		objects = new HashMap<>();
	}

	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		synchronized (objects) {
			for (GameObject object : objects.values()) {
				object.update(container, delta);
			}
		}

	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		synchronized (objects) {
			for (GameObject object : objects.values()) {
				object.render(container, g);
			}
		}
	}
}