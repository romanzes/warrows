package ru.footmade.warrows.game;

import java.util.ArrayList;
import java.util.List;

public class Grenade extends Item {

	protected Grenade(Field logic, int x, int y) {
		super(logic, x, y, "sprites/spr-grenade");
	}
	
	@Override
	public void process() {
		explode();
	}
	
	@Override
	protected void explode() {
		super.explode();
		Field logic = getField();
		List<Item> neighbours = new ArrayList<Item>();
		neighbours.add(logic.getItem(x - 1, y - 1));
		neighbours.add(logic.getItem(x, y - 1));
		neighbours.add(logic.getItem(x + 1, y - 1));
		neighbours.add(logic.getItem(x + 1, y));
		neighbours.add(logic.getItem(x + 1, y + 1));
		neighbours.add(logic.getItem(x, y + 1));
		neighbours.add(logic.getItem(x - 1, y + 1));
		neighbours.add(logic.getItem(x - 1, y));
		for (Item neighbour : neighbours) {
			if (neighbour != null && !neighbour.explodeFlag)
				neighbour.explode();
		}
	}
}
