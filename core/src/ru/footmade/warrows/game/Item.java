package ru.footmade.warrows.game;

import ru.footmade.warrows.util.CommonResources;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Item extends Sprite {
	public float alpha = 1f;
	public int x, y;
	public boolean moveFlag, reverseMove;
	public boolean destroyFlag;
	
	protected Item(int x, int y, String type) {
		super(CommonResources.getLinearRegion(type));
		this.x = x;
		this.y = y;
	}
	
	protected void moveTo(int x, int y) {
		moveTo(x, y, false);
	}
	
	protected void moveTo(int x, int y, boolean reverse) {
		this.x = x;
		this.y = y;
		moveFlag = true;
		reverseMove = reverse;
	}
	
	protected void destroy() {
		destroyFlag = true;
	}
	
	public void process(Logic logic) {}
}
