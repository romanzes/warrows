package ru.footmade.warrows.tweens;

import ru.footmade.warrows.util.Fader;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class MyTweenManager extends TweenManager {
	private static MyTweenManager _instance;
	
	public static TweenManager getInstance() {
		if (_instance == null)
			_instance = new MyTweenManager();
		return _instance;
	}
	
	public static void register() {
		Tween.registerAccessor(Fader.class, new FaderAccessor());
	}
	
	public static void updateInstance(float deltaTime) {
		getInstance().update(deltaTime);
	}
}
