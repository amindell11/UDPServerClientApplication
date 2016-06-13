package input;

import game_object.GameObject;

public interface Command {
	public void execute(GameObject object);
}
