package ee.fxgame.objects;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Scale extends Actor {
	
	private int value;

	public boolean isAnimating() {
		return getActions().size > 0;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
