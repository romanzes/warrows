package ru.footmade.warrows.tweens;

import ru.footmade.warrows.util.Observer;
import aurelienribon.tweenengine.TweenAccessor;

public class ObserverAccessor implements TweenAccessor<Observer> {
	public static final int POSITION = 1;

    @Override
    public int getValues(Observer target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION:
                returnValues[0] = target.x;
                returnValues[1] = target.y;
                returnValues[2] = target.scale;
                return 3;
            default: assert false; return -1;
        }
    }
    
    @Override
    public void setValues(Observer target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION:
                target.x = newValues[0];
                target.y = newValues[1];
                target.scale = newValues[2];
                break;
            default: assert false; break;
        }
    }
}
