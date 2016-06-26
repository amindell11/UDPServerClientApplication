package game_object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import proto.GameStateExchangeProto.GameStateExchange.GroupObjectUpdate.ObjectUpdate;
import proto.GameStateExchangeProto.GameStateExchange.InputState;

public class Box {
	private transient int objectId;
	public transient int lastSentUpdate;
	public ArrayList<InputState> pendingInputs;
	private float width;
	private float height;
	private int x;
	private int y;
	public int realx;
	private int realy;
	private float vel;

	public Box(float width, float height, float vel) {
		this.width = width;
		this.height = height;
		this.vel = vel;
		pendingInputs = new ArrayList<>();
		x = (int)(Math.random() * 400);
		y = (int)(Math.random() * 400);
		realx = x;
		realy = y;
	}

	public void applyInput(InputState input) {
		float ds = (float) (vel * input.getDt());

		if (input.getIsKeyLeft()) {
			x -= ds;
		}
		if (input.getIsKeyRight()) {
			x += ds;
		}
		if (input.getIsKeyUp()) {
			y -= ds;
		}
		if (input.getIsKeyDown()) {
			y += ds;
		}
	}

	public void render(GameContainer gc, Graphics g) {
	//g.setColor(Color.white);
	g.drawRect(realx, realy, width, height);
	//g.setColor(Color.blue);
	//g.drawRect(x, y, width, height);
	//g.setColor(Color.white);


	}

	public void update(GameContainer gc, int delta, Map<Integer, Box> otherBoxes) {

	    	int oldRealX = realx;
		int oldRealY = realy;
		
		if (realx != x) {
			realx += (float) (x - realx) / 20;
		}
		if (realy != y) {
			realy += (float) (y - realy) / 20;
		}
		
		//Assume it will never hit directly on the corner
		//Assume just calculate each box interaction sequentially. This might actually work, im not sure.
		
		
		Rectangle currentHitbox = getHitbox();
		for(Box box : otherBoxes.values()){
		    if(this != box && currentHitbox.intersects(box.getHitbox())){
			realx = oldRealX;
			realy = oldRealY;
			x = realx;
			y = realy;
			//collision(oldRealX, oldRealY, box);
		    }
		}
		
	}
	
	private void collision(int oldx, int oldy, Box box) {
	    Vector2f movementVector = new Vector2f(realx - oldx, realy - oldy);
	    Vector2f vectorRelativeToIntersect = new Vector2f();
	}

	public Rectangle getHitbox(){
	    return new Rectangle(realx, realy, width, height);
	}

	public void applyObjectUpdate(ObjectUpdate update) {
		this.x = update.getPosX();
		this.y = update.getPosY();
	}

	public void setId(int id) {
		this.objectId = id;
	}

	public int getId() {
		return objectId;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public void reconcile(int sequenceNum) {
		synchronized (pendingInputs) {
			Iterator<InputState> it = pendingInputs.listIterator();
			while (it.hasNext()) {
				// ConcurrentModification error happens here
				InputState update = it.next();

				if (update.getSequenceNum() > sequenceNum) {
					applyInput(update);
				} else {
					it.remove();
				}
			}
		}

	}

}
