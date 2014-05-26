package ru.footmade.warrows.tweens;

import ru.footmade.warrows.game.Item;
import aurelienribon.tweenengine.TweenAccessor;

public class ItemAccessor implements TweenAccessor<Item> {
	public static final int POSITION = 1;
	public static final int ALPHA = 2;
	public static final int SCALE = 3;

    @Override
    public int getValues(Item target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case ALPHA:
            	returnValues[0] = target.alpha;
            	return 1;
            case SCALE:
            	returnValues[0] = target.getScaleX();
            	returnValues[1] = target.getScaleY();
            	return 2;
            default: assert false; return -1;
        }
    }
    
    @Override
    public void setValues(Item target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION:
                target.setPosition(newValues[0], newValues[1]);
                break;
            case ALPHA:
            	target.alpha = newValues[0];
            	break;
            case SCALE:
            	target.setScale(newValues[0], newValues[1]);
            	break;
            default: assert false; break;
        }
    }
}
