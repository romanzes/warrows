package ru.footmade.warrows;

import ru.footmade.warrows.screens.SplashScreen;
import ru.footmade.warrows.tweens.MyTweenManager;
import ru.footmade.warrows.util.CommonResources;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends Game {
	private static Game _self;
	
	public static Game getSelf() {
		return _self;
	}
	
	@Override
	public void create() {
		_self = this;
		MyTweenManager.register();
		setScreen(new SplashScreen());
	}
	
	@Override
	public void render() {
		super.render();
		MyTweenManager.updateInstance(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		CommonResources.disposeAll();
	}
}
