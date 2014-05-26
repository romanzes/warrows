package ru.footmade.warrows.tweens;

import ru.footmade.warrows.util.Fader;
import ru.footmade.warrows.util.Observer;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyTweenManager extends TweenManager {
	private static MyTweenManager _instance;
	
	public static TweenManager getInstance() {
		if (_instance == null)
			_instance = new MyTweenManager();
		return _instance;
	}
	
	public static void register() {
		Tween.registerAccessor(Fader.class, new FaderAccessor());
		Tween.registerAccessor(Observer.class, new ObserverAccessor());
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
	}
	
	public static void updateInstance(float deltaTime) {
		getInstance().update(deltaTime);
	}
}
