package ee.fxgame.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ee.fxgame.assets.Assets;
import ee.fxgame.main.Drawer;
import ee.fxgame.main.GameLogic;
import ee.fxgame.main.GameMain;
import ee.fxgame.main.Settings;
import ee.fxgame.objects.Punkt;
import ee.fxgame.objects.Purchase;
import ee.fxgame.objects.Sale;
import ee.fxgame.objects.Scale;

public class GameScreen implements Screen {

	private Stage stage;
	private Array<Punkt> punktid;
	private Array<Punkt> highlighted;
	private Array<Scale> scales;
	private Array<Purchase> purchases;
	private Array<Sale> sales;
	private Drawer drawer;
	private GameLogic logic;

	private Button buy;
	private Button sell;

	Random rnd;

	float gamex, gamey;
	public static float change;
	public static int money;
	
	float time = 0f;
	public static int korda, alla, yles;
	int last;

	public static Skin skin;
	
	/**
	 * Punktide pool.
	 */
	
	private final Pool<Punkt> punktPool = new Pool<Punkt>() {
		@Override
		protected Punkt newObject() {
			return new Punkt();
		}
	};

	@Override
	public void show() {
		stage = new Stage(GameMain.viewport);
		gamex = GameMain.viewport.getWorldWidth();
		gamey = GameMain.viewport.getWorldHeight();
		change = (gamey - 200) / Settings.scalecount;
		purchases = new Array<Purchase>();
		sales = new Array<Sale>();
		highlighted = new Array<Punkt>();
		Gdx.input.setInputProcessor(stage);
		punktid = new Array<Punkt>();
		scales = new Array<Scale>();
		drawer = new Drawer(punktid, scales, purchases, sales, gamex, gamey,
				change);
		money = Settings.startMoney;
		logic = new GameLogic(stage, punktid, scales, purchases, highlighted,
				sales, gamex, gamey, money, change, punktPool);		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		buy = new TextButton("BUY", skin);
		TextButtonStyle style = (TextButtonStyle) buy.getStyle();
		style.font = Assets.font;
		buy.setStyle(style);
		buy.setX(25);
		buy.setY(25);
		buy.setSize(60, 30);
		buy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				logic.handleBuy();
				super.clicked(event, x, y);
			}
		});
		sell = new TextButton("SELL", skin);
		sell.setStyle(style);
		sell.setX(gamex - 90);
		sell.setY(25);
		sell.setSize(60, 30);
		sell.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				logic.handleSell();
				super.clicked(event, x, y);
			}
		});
		stage.addActor(sell);
		stage.addActor(buy);
		
		for (int i = 0; i <= Settings.scalecount; i += 5) {
			Scale scale = new Scale();
			scale.setX(80);
			scale.setY(100 + i * change);
			scale.setValue(Settings.price + i - Settings.scalecount / 2);
			scales.add(scale);
			stage.addActor(scale);
		}
		Punkt algus = punktPool.obtain();
		algus.init();
		algus.setValue(Settings.price);
		algus.setX(gamex - 75);
		algus.setY(Settings.scalecount / 2 * change + 100);
		punktid.add(algus);
		stage.addActor(algus);
		last = punktid.size;
	}
	float t = 0;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		t+=delta;
		if (t >= 1) {
			last = punktid.size;
			t = 0;
		}
		logic.removeDeadPunktid();
		if (!areAnimating()) {
			time += delta;
		}
		if (time >= Settings.spawnSpeed && !areAnimating()) {
			logic.addNewPunkt();
			time = 0;
			logic.checkPohiAndLagi();
			logic.checkGraph();
			
		}		
		stage.act();
		drawer.drawGraph(delta);
		drawer.drawGraphLines(delta);
		drawer.drawGrapInfo(delta);		
		stage.draw();

	}
	
	/**
	 * Kontrollib kas punktid animeerivad.
	 * @return True kui animeerivad.
	 */

	private boolean areAnimating() {
		for (Punkt punkt : punktid) {
			if (punkt.isAnimating()) {
				return true;
			}
		}

		return false;
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
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		drawer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

}
