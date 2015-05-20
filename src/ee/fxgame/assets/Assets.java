package ee.fxgame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public class Assets {
	
	public static BitmapFont font;
	public static BitmapFont font16;
	
	public static void loadAssets() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Chunkfive.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		font = generator.generateFont(parameter);
		parameter.size = 30;
		font16 = generator.generateFont(parameter);
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
	}
	
	
	public static void disposeAssets() {

	}

}
