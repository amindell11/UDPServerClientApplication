package game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import game_object.GameObject;

/**
 * @author Ari
 */
public class GameManager extends BasicGame {
	List<GameObject> objects;

	public GameManager() {
		super("Box game");
		objects = new ArrayList<>();
	}

	@Override
	public void init(GameContainer container) throws SlickException {
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		synchronized (objects) {
			for (GameObject object : objects) {
				object.update(container, delta);
			}
		}

	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		synchronized (objects) {
			for (GameObject object : objects) {
				object.render(container, g);
			}
		}
	}
}