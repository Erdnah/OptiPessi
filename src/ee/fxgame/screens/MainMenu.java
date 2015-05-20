package ee.fxgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ee.fxgame.main.GameMain;


public class MainMenu implements Screen {
	
	private Stage stage;
	private Button gears;

	@Override
	public void show() {
		stage = new Stage(GameMain.viewport);
		Gdx.input.setInputProcessor(stage);
		
		
		gears.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
