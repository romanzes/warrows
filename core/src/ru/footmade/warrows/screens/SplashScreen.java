package ru.footmade.warrows.screens;

import ru.footmade.warrows.MyGdxGame;
import ru.footmade.warrows.tweens.MyTweenManager;
import ru.footmade.warrows.util.CommonResources;
import ru.footmade.warrows.util.Fader;
import ru.footmade.warrows.util.GLCleaner;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Circ;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SplashScreen extends ScreenAdapter {
	private static final int BACKGROUND_COLOR = 0xffffffff;
	private static final float LOGO_RELATIVE_SIZE = 0.5f;
	private static final float LOGO_APPEAR_TIME = 1;
	private static final float LOGO_WAIT_TIME = 1;
	
	private SpriteBatch batch;
	private Sprite logo;
	private Fader logoFader;
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		logo = new Sprite(CommonResources.getLinearRegion("ui/footmade-logo"));
		Rectangle logoRect = logo.getBoundingRectangle();
		logoRect.fitInside(new Rectangle(0, 0, Gdx.graphics.getWidth() * LOGO_RELATIVE_SIZE,
				Gdx.graphics.getHeight() * LOGO_RELATIVE_SIZE)).setCenter(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		logo.setPosition(logoRect.x, logoRect.y);
		logo.setSize(logoRect.width, logoRect.height);
	}
	
	@Override
	public void render(float delta) {
		if (logoFader == null) {
			logoFader = new Fader(0, 1, LOGO_APPEAR_TIME, Circ.IN);
			logoFader.setOnFinishCallback(new Runnable() {
				@Override
				public void run() {
					Tween.call(new TweenCallback() {
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							MyGdxGame.getSelf().setScreen(new ComicScreen(ComicScreen.COMIC_INTRO));
						}
					}).delay(LOGO_WAIT_TIME).start(MyTweenManager.getInstance());
				}
			});
		}
		
		GLCleaner.clearARGB(BACKGROUND_COLOR);
		
		batch.begin();
		logo.draw(batch, logoFader.alpha);
		batch.end();
	}
}
