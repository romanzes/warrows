package ru.footmade.warrows.game;

import ru.footmade.warrows.util.CommonResources;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Item extends Sprite {
	public int x, y;
	public boolean moveFlag, reverseMove;
	
	protected Item(int x, int y, String type) {
		super(CommonResources.getLinearRegion(type));
		this.x = x;
		this.y = y;
	}
	
	protected void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
		moveFlag = true;
	}
	
	public void process(Logic logic) {}
}
