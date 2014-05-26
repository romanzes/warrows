package ru.footmade.warrows.screens;

import java.util.ArrayList;
import java.util.List;

import ru.footmade.warrows.game.Item;
import ru.footmade.warrows.game.Logic;
import ru.footmade.warrows.tweens.MyTweenManager;
import ru.footmade.warrows.tweens.SpriteAccessor;
import ru.footmade.warrows.util.CommonResources;
import ru.footmade.warrows.util.GLCleaner;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;

public class GameplayScreen extends ScreenAdapter {
	private SpriteBatch batch;
	private int scrW, scrH;
	
	private Logic logic;
	
	private static final float FIELD_RELATIVE_SIZE = 0.9f;
	private static final float MOVE_TIME = 0.5f;
	
	private TextureRegion fieldTile;
	private TextureRegion backgroundPart;
	
	private Rectangle fieldRect;
	private float cellSize;
	private float statusHeight;
	
	private final List<Item> backgroundItems = new ArrayList<Item>();
	private final List<Item> foregroundItems = new ArrayList<Item>();
	
	private boolean allowAction = true;
	
	@Override
	public void show() {
		logic = new Logic();
		
		fieldTile = CommonResources.getLinearRegion("sprites/tile-sand");
		backgroundPart = CommonResources.getLinearRegion("ui/statusbg");
		
		initInput();
	}
	
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	private void initInput() {
		Gdx.input.setInputProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
			@Override
			public boolean tap(float x, float y, int count, int button) {
				if (allowAction) {
					y = scrH - y;
					for (Item item : logic.items) {
						if (item.getBoundingRectangle().contains(x, y)) {
							item.process(logic);
							allowAction = false;
							return true;
						}
					}
				}
				return false;
			}
		}));
	}
	
	@Override
	public void resize(int width, int height) {
		batch = new SpriteBatch();
		scrW = width;
		scrH = height;
		cellSize = Math.min(scrH / (Logic.FIELD_HEIGHT + 2), scrW / Logic.FIELD_WIDTH);
		statusHeight = cellSize * 2;
		cellSize *= FIELD_RELATIVE_SIZE;
		float fieldW = cellSize * Logic.FIELD_WIDTH;
		float fieldH = cellSize * Logic.FIELD_HEIGHT;
		fieldRect = new Rectangle((scrW - fieldW) / 2, (scrH + statusHeight - fieldH) / 2, fieldW, fieldH);
		
		for (Item item : logic.items) {
			item.setBounds(fieldRect.x + item.x * cellSize, fieldRect.y + item.y * cellSize,
					(float) Math.ceil(cellSize), (float) Math.ceil(cellSize));
		}
	}
	
	private void drawBackground() {
		float backgroundPartHeight = backgroundPart.getRegionHeight() * scrW / backgroundPart.getRegionWidth();
		int backgroundPartCount = (int) (scrH / backgroundPartHeight) + 1;
		for (int i = 0; i < backgroundPartCount; i++) {
			batch.draw(backgroundPart, 0, i * backgroundPartHeight, scrW, backgroundPartHeight + 1);
		}
	}
	
	private void drawField() {
		for (int i = 0; i < Logic.FIELD_WIDTH; i++) {
			for (int j = 0; j < Logic.FIELD_HEIGHT; j++) {
				batch.draw(fieldTile, fieldRect.x + i * cellSize, fieldRect.y + j * cellSize,
						(float) Math.ceil(cellSize), (float) Math.ceil(cellSize));
			}
		}
	}
	
	private void drawItems() {
		for (Item item : backgroundItems) {
			item.draw(batch);
		}
		for (final Item item : logic.items) {
			if (!(backgroundItems.contains(item) || foregroundItems.contains(item)))
				item.draw(batch);
			if (item.moveFlag) {
				if (item.reverseMove)
					backgroundItems.add(item);
				else
					foregroundItems.add(item);
				Tween.to((Sprite) item, SpriteAccessor.POSITION, MOVE_TIME)
						.target(fieldRect.x + item.x * cellSize, fieldRect.y + item.y * cellSize)
						.start(MyTweenManager.getInstance())
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.setCallback(new TweenCallback() {
							@Override
							public void onEvent(int type, BaseTween<?> source) {
								backgroundItems.remove(item);
								foregroundItems.remove(item);
							}
						});
				item.moveFlag = false;
				item.reverseMove = false;
			}
		}
		for (Item item : foregroundItems) {
			item.draw(batch);
		}
	}
	
	@Override
	public void render(float delta) {
		GLCleaner.clearARGB(0xffffffff);
		batch.begin();
		drawBackground();
		drawField();
		drawItems();
		allowAction = backgroundItems.size() + foregroundItems.size() == 0;
		batch.end();
	}
}