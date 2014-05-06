package ru.footmade.warrows;

import ru.footmade.warrows.screens.SplashScreen;
import ru.footmade.warrows.tweens.MyTweenManager;
import ru.footmade.warrows.util.CommonResources;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends Game {
	
	@Override
	public void create () {
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
