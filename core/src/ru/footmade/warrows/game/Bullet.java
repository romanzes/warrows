package ru.footmade.warrows.game;

import java.util.HashMap;
import java.util.Map;

public class Bullet extends Item {
	public static final int DIRECTION_LEFT = 1;
	public static final int DIRECTION_RIGHT = 2;
	public static final int DIRECTION_UP = 4;
	public static final int DIRECTION_DOWN = 8;
	
	private static final int DIRECTION_HORIZONTAL_MASK = DIRECTION_LEFT | DIRECTION_RIGHT;
	private static final int DIRECTION_VERTICAL_MASK = DIRECTION_UP | DIRECTION_DOWN;
	
	private static final Map<Integer, String> types = new HashMap<Integer, String>();
	
	static {
		types.put(DIRECTION_UP, "sprites/spr-bullet-0");
		types.put(DIRECTION_LEFT | DIRECTION_UP, "sprites/spr-bullet-1");
		types.put(DIRECTION_LEFT, "sprites/spr-bullet-2");
		types.put(DIRECTION_LEFT | DIRECTION_DOWN, "sprites/spr-bullet-3");
		types.put(DIRECTION_DOWN, "sprites/spr-bullet-4");
		types.put(DIRECTION_RIGHT | DIRECTION_DOWN, "sprites/spr-bullet-5");
		types.put(DIRECTION_RIGHT, "sprites/spr-bullet-6");
		types.put(DIRECTION_RIGHT | DIRECTION_UP, "sprites/spr-bullet-7");
	}
	
	private int direction;

	protected Bullet(int x, int y, int direction) {
		super(x, y, types.get(direction));
		this.direction = direction;
	}
	
	@Override
	public void process(Logic logic) {
		int destX = x, destY = y;
		switch (direction & DIRECTION_HORIZONTAL_MASK) {
		case DIRECTION_LEFT:
			destX--;
			break;
		case DIRECTION_RIGHT:
			destX++;
			break;
		}
		switch (direction & DIRECTION_VERTICAL_MASK) {
		case DIRECTION_UP:
			destY++;
			break;
		case DIRECTION_DOWN:
			destY--;
			break;
		}
		Item neighbour = logic.getItem(destX, destY);
		if (neighbour != null) {
			neighbour.moveTo(x, y, true);
		}
		moveTo(destX, destY);
		if (destX < 0 || destX >= Logic.FIELD_WIDTH || destY < 0 || destY >= Logic.FIELD_HEIGHT)
			destroy();
	}
}
