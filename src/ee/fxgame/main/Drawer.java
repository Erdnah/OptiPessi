package ee.fxgame.main;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import ee.fxgame.assets.Assets;
import ee.fxgame.objects.Punkt;
import ee.fxgame.objects.Purchase;
import ee.fxgame.objects.Sale;
import ee.fxgame.objects.Scale;


/**
 * Mängu joonistaja.
 * @author Handre
 *
 */
public class Drawer {

	Array<Punkt> punktid;
	Array<Scale> scales;
	Array<Purchase> purchases;
	Array<Sale> sales;
	private ShapeRenderer render;

	private float gamex, gamey;
	private Batch batch;
	public static float change;
	private BitmapFont font;
	private BitmapFont font16;

	public Drawer(Array<Punkt> punktid, Array<Scale> scales,
			Array<Purchase> purchases, Array<Sale> sales, float gamex,
			float gamey, float change) {
		this.punktid = punktid;
		this.gamex = gamex;
		this.gamey = gamey;
		Drawer.change = change;
		this.scales = scales;
		this.purchases = purchases;
		this.sales = sales;
		render = new ShapeRenderer();
		batch = new SpriteBatch();
		render.setProjectionMatrix(GameMain.viewport.getCamera().combined);
		batch.setProjectionMatrix(GameMain.viewport.getCamera().combined);
		font = Assets.font;
		font16 = Assets.font16;
	}
	
	/**
	 * Joonistab kõikide punktide vahele jooned ning tipud ja põhjad.
	 * 
	 * @param delta Aeg viimasest joonistamisest
	 */
	public void drawGraph(float delta) {
		render.setColor(Color.WHITE);
		render.begin(ShapeType.Line);
		Punkt last = null;
		for (Punkt punkt : punktid) {
			punkt.act(delta);
			if (punkt.isHighlighted()) {
				render.end();
				render.begin(ShapeType.Line);
				if (punkt.isTipp()) {
					render.setColor(Color.RED);
				} else {
					render.setColor(Color.BLUE);
				}
				if (punkt.getJoon() != null) {
					if (punkt.getJoon().getWidth() == 0) {
						render.line(punkt.getX(), punkt.getY(), punkt.getJoon().getX(),
								punkt.getY());
					} else {
						render.line(punkt.getX(), punkt.getY(), punkt.getX()
								+ punkt.getJoon().getWidth(), punkt.getY());
					}

				}

				render.circle(punkt.getX(), punkt.getY(), 2);
				render.setColor(Color.WHITE);
			}
			if (last != null) {
				// if ((punkt.getY() >= 100) && (last.getY() >= 100) &&
				// (punkt.getY() <= gamey-100) && (last.getY() <= gamey-100)) {
				render.line(last.getX(), last.getY(), punkt.getX(),
						punkt.getY());
				// }
				// render.line(punkt.getX(), 100, punkt.getX(), 102);

			}
			last = punkt;
		}
		render.end();
	}
	
	/**
	 * Joonistab graafiku ümber jooned ja kastid
	 * 
	 * @param delta
	 */

	public void drawGraphLines(float delta) {

		render.begin(ShapeType.Filled);
		render.setColor(Color.BLACK);
		render.rect(gamex - 80, 0, 80, gamey);
		render.rect(0, 0, 80, gamey);
		render.rect(0, gamey - 100, gamex, 100);
		render.rect(0, 0, gamex, 100);
		render.end();
		render.begin(ShapeType.Line);
		render.setColor(Color.WHITE);
		//render.line(80, 100, gamex - 80, 100);
		render.line(80, 100, 80, gamey - 100);

		render.end();
		// render.setColor(255, 255, 255, 0.2f);

		render.end();
	}
	
	/**
	 * Joonistab graafikut puudutava info.
	 * @param delta
	 */

	public void drawGrapInfo(float delta) {
		batch.begin();
		for (int i = 0; i < scales.size; i++) {

			Scale scale = scales.get(i);
			if (scale.getY() >= 95) {
				font.draw(batch, "" + scale.getValue(), 45, scale.getY() + 5);
			}

		}
		font.draw(batch, "$$$: " + GameLogic.money, gamex - 100, gamey - 20);
		int toDraw = 0;
		if (purchases.size > 0) {
			toDraw = purchases.size;
		} else {
			toDraw = -1 * sales.size;
		}
		font16.draw(batch, "" + toDraw, gamex / 2 - 20, 50);
		// font.draw(batch, "3x:  " + GameScreen.korda + " Alla: " +
		// GameScreen.alla
		// + " Üles: " + GameScreen.yles + " lastscle " +
		// scales.peek().getY()+
		// " c: " + scales.size, gamex / 2 - 100, gamey - 50);
		batch.end();
	}

	public void dispose() {
		render.dispose();
		batch.dispose();
		font.dispose();
		font16.dispose();
	}

}
