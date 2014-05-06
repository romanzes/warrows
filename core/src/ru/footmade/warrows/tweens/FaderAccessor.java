package ru.footmade.warrows.tweens;

import ru.footmade.warrows.util.Fader;
import aurelienribon.tweenengine.TweenAccessor;

public class FaderAccessor implements TweenAccessor<Fader> {
	public static final int ALPHA = 1;

    @Override
    public int getValues(Fader target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target.alpha;
                return 1;
            default: assert false; return -1;
        }
    }
    
    @Override
    public void setValues(Fader target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                target.alpha = newValues[0];
                break;
            default: assert false; break;
        }
    }
}
