package ee.fxgame.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import ee.fxgame.assets.Assets;
import ee.fxgame.screens.GameScreen;

public class GameMain extends Game {
	
	public static ExtendViewport viewport;
	
	@Override
	public void create () {
		OrthographicCamera camera = new OrthographicCamera(800, 480);
		viewport = new ExtendViewport(800, 480, camera);
		Assets.loadAssets();
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();
	}
}
