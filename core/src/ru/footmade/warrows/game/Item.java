package ru.footmade.warrows.game;

import ru.footmade.warrows.util.CommonResources;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Item extends Sprite {
	private Field field;
	public float alpha = 1f;
	public int x, y;
	public boolean moveFlag, reverseMove;
	public boolean destroyFlag;
	public boolean explodeFlag;
	
	protected Item(Field field, int x, int y, String type) {
		super(CommonResources.getLinearRegion(type));
		this.field = field;
		this.x = x;
		this.y = y;
	}
	
	public Field getField() {
		return field;
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
	
	protected void explode() {
		explodeFlag = true;
	}
	
	public void process() {}
}
