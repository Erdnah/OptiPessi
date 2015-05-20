package ee.fxgame.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Joon extends Actor {
	
	private boolean tipp;
	private boolean hitted;
	
	public void setTipp(boolean tipp) {
		this.tipp = tipp;
	}
	
	public boolean isTip() {
		return tipp;
	}
	
	public boolean isHitted() {
		return hitted;
	}
	
	public void hit() {
		hitted = true;
	}

}
