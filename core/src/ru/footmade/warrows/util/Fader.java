package ru.footmade.warrows.util;

import ru.footmade.warrows.tweens.FaderAccessor;
import ru.footmade.warrows.tweens.MyTweenManager;
import aurelienribon.tweenengine.Tween;

public class Fader {
	public float alpha;
	
	public Fader(float initialAlpha, float finalAlpha, float duration) {
		this.alpha = initialAlpha;
		Tween.to(this, FaderAccessor.ALPHA, duration).target(finalAlpha).start(MyTweenManager.getInstance());
	}
}
