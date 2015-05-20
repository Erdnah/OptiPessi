package ee.fxgame.objects;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

import ee.fxgame.main.Settings;

public class Punkt extends Actor implements Poolable {
	
	private final float radius = 3;
	private boolean alive = false;
	private int value = 0;
	
	private float newY = 0;
	
	private float speed;
	private boolean highlighted;
	private boolean tipp;
	private Joon joon;
	
	public void init() {
		speed = Settings.graphSpeed;
		alive = true;
	}
	@Override
	public void act(float delta) {
		if (!isAnimating() && alive) {
			setX(getX() - Settings.graphSpeed*delta);
		}
		if (highlighted && joon != null) {
			if (getX() < -joon.getWidth()) {
				alive = false;
				joon = null;
			}
		} else {
			if (getX() < -speed) {
				alive = false;
				joon = null;
			}
		}
		
		super.act(delta);
	}
	
	public void setJoon(Joon joon) {
		this.joon = joon;
	}
	
	public Joon getJoon() {
		return joon;
	}
	
	public void setNewY(float y) {
		newY = y;
	}
	
	public float getNewY() {
		return newY;
	}
	
	public boolean isAnimating() {
		return getActions().size > 0;
	}
	
	public float getRadius() {
		return radius;
	}

	@Override
	public void reset() {
		highlighted = false;
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void highlight(boolean tipp) {
		highlighted = true;
		this.tipp = tipp;
	}
	
	public boolean isTipp() {
		return tipp;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}
	
	public void add(int toAdd) {
		value += toAdd;
	}
	
	public void sub(int toAdd) {
		value -= toAdd;
	}
	
	
	public int getValue() {
		return value;
	}

}
