package ru.footmade.warrows.util;

import ru.footmade.warrows.tweens.FaderAccessor;
import ru.footmade.warrows.tweens.MyTweenManager;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Linear;

public class Fader {
	public float alpha;
	private Tween tween;
	
	public Fader(float initialAlpha, float finalAlpha, float duration) {
		this(initialAlpha, finalAlpha, duration, Linear.INOUT);
	}
	
	public Fader(float initialAlpha, float finalAlpha, float duration, TweenEquation equation) {
		this.alpha = initialAlpha;
		tween = Tween.to(this, FaderAccessor.ALPHA, duration).target(finalAlpha).start(MyTweenManager.getInstance()).ease(equation);
	}
	
	public void setOnFinishCallback(final Runnable callback) {
		tween.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (type == TweenCallback.COMPLETE) {
					callback.run();
				}
			}
		});
	}
}
