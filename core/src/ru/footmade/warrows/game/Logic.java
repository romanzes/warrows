package ru.footmade.warrows.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Logic {
	public static final int FIELD_WIDTH = 6;
	public static final int FIELD_HEIGHT = 6;
	
	public final List<Item> items = new ArrayList<Item>();
	
	public Logic() {
		int[] types = new int[] {
				Bullet.DIRECTION_LEFT,
				Bullet.DIRECTION_RIGHT,
				Bullet.DIRECTION_UP,
				Bullet.DIRECTION_DOWN,
				Bullet.DIRECTION_LEFT | Bullet.DIRECTION_UP,
				Bullet.DIRECTION_LEFT | Bullet.DIRECTION_DOWN,
				Bullet.DIRECTION_RIGHT | Bullet.DIRECTION_DOWN,
				Bullet.DIRECTION_RIGHT | Bullet.DIRECTION_UP
		};
		Random rand = new Random();
		for (int i = 0; i < FIELD_WIDTH; i++) {
			for (int j = 0; j < FIELD_HEIGHT; j++) {
				if (rand.nextInt(7) == 0) {
					if (rand.nextInt(2) == 0)
						items.add(new Grenade(this, i, j));
					else
						items.add(new Wall(this, i, j));
				} else {
					items.add(new Bullet(this, i, j, types[rand.nextInt(types.length)]));
				}
			}
		}
	}
	
	public Item getItem(int x, int y) {
		for (Item item : items) {
			if (item.x == x && item.y == y)
				return item;
		}
		return null;
	}
}
