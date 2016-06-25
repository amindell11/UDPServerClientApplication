package game_object;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

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
	private int realx;
	private int realy;
	private float vel;

	public Box(float width, float height, float vel) {
		this.width = width;
		this.height = height;
		this.vel = vel;
		pendingInputs = new ArrayList<>();
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
	//	g.drawRect(x, y, width, height);
		g.drawRect(realx, realy, width, height);

	}
	public void update(GameContainer gc,int delta){
		if(realx!=x){
			realx+=(float)(x-realx)/20;
		}
		if(realy!=y){
			realy+=(float)(y-realy)/20;
		}
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
		System.out.println(pendingInputs.size());
		Iterator<InputState> it = pendingInputs.listIterator();
		while (it.hasNext()) {
		    	//ConcurrentModification error happens here
			InputState update = it.next();

			if (update.getSequenceNum() > sequenceNum) {
				applyInput(update);
			} else {
				it.remove();
			}
		}

	}

}
