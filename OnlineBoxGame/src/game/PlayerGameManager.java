package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game_object.Box;
import proto.GameStateExchangeProto.GameStateExchange.InputState;

public class PlayerGameManager extends GameManager {
	Box clientObject;
	String username;
	InputState.Builder currentInputState;
	public PlayerGameManager() {
		super();
		this.username = "";
		clientObject = new Box(50, 50, 2f);
		currentInputState=collectInputState(0,clientObject.getId());
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
		clientObject.update(gc, delta);
		currentInputState=collectInputState(delta,clientObject.getId());
	}

	public InputState.Builder collectInputState(int delta, int objectId) {
		InputState.Builder inputState = InputState.newBuilder();

		try {
			Input input = new Input(0);
			inputState.setIsKeyLeft(input.isKeyDown(Input.KEY_LEFT));
			inputState.setIsKeyUp(input.isKeyDown(Input.KEY_UP));
			inputState.setIsKeyRight(input.isKeyDown(Input.KEY_RIGHT));
			inputState.setIsKeyDown(input.isKeyDown(Input.KEY_DOWN));
		} catch (IllegalStateException e) {
			inputState.setIsKeyLeft(false);
			inputState.setIsKeyUp(false);
			inputState.setIsKeyRight(false);
			inputState.setIsKeyDown(false);
		}
		inputState.setDt(delta);
		inputState.setObjectId(objectId);
		return inputState;

	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		super.render(gc, g);
		g.drawString(username, 10, 30);
		clientObject.render(gc, g);
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
