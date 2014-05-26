package ru.footmade.warrows.screens;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ru.footmade.warrows.game.Item;
import ru.footmade.warrows.game.Field;
import ru.footmade.warrows.tweens.ItemAccessor;
import ru.footmade.warrows.tweens.MyTweenManager;
import ru.footmade.warrows.util.CommonResources;
import ru.footmade.warrows.util.GLCleaner;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;

public class GameplayScreen extends ScreenAdapter {
	private SpriteBatch batch;
	private int scrW, scrH;
	
	private Field field;
	
	private static final float FIELD_RELATIVE_SIZE = 0.9f;
	private static final float MOVE_TIME = 0.4f;
	private static final float JOLT_PERIOD = 0.05f;
	private static final int JOLT_COUNT = 20;
	private static final float JOLT_RADIUS = 0.1f;
	private static final float EXPLOSION_REDUCTION_RATIO = 0.85f;
	private static final float EXPLOSION_REDUCTION_TIME = 0.2f;
	
	private TextureRegion fieldTile;
	private TextureRegion backgroundPart;
	
	private Rectangle fieldRect;
	private float cellSize;
	private float statusHeight;
	
	private final List<Item> backgroundItems = new ArrayList<Item>();
	private final List<Item> foregroundItems = new ArrayList<Item>();
	
	private final Set<Object> actionLock = new HashSet<Object>();
	
	@Override
	public void show() {
		field = new Field();
		
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
				if (actionLock.isEmpty()) {
					y = scrH - y;
					for (Item item : field.items) {
						if (item.getBoundingRectangle().contains(x, y)) {
							item.process();
							actionLock.add(GameplayScreen.this);
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
		cellSize = Math.min(scrH / (Field.FIELD_HEIGHT + 2), scrW / Field.FIELD_WIDTH);
		statusHeight = cellSize * 2;
		cellSize *= FIELD_RELATIVE_SIZE;
		float fieldW = cellSize * Field.FIELD_WIDTH;
		float fieldH = cellSize * Field.FIELD_HEIGHT;
		fieldRect = new Rectangle((scrW - fieldW) / 2, (scrH + statusHeight - fieldH) / 2, fieldW, fieldH);
		
		for (Item item : field.items) {
			item.setBounds(fieldRect.x + item.x * cellSize, fieldRect.y + item.y * cellSize,
					(float) Math.ceil(cellSize), (float) Math.ceil(cellSize));
			item.setOriginCenter();
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
		for (int i = 0; i < Field.FIELD_WIDTH; i++) {
			for (int j = 0; j < Field.FIELD_HEIGHT; j++) {
				batch.draw(fieldTile, fieldRect.x + i * cellSize, fieldRect.y + j * cellSize,
						(float) Math.ceil(cellSize), (float) Math.ceil(cellSize));
			}
		}
	}
	
	private void drawItems() {
		for (Item item : backgroundItems) {
			item.draw(batch, item.alpha);
		}
		for (final Item item : field.items) {
			if (!(backgroundItems.contains(item) || foregroundItems.contains(item)))
				item.draw(batch, item.alpha);
			if (item.moveFlag) {
				actionLock.add(item);
				if (item.reverseMove)
					backgroundItems.add(item);
				else
					foregroundItems.add(item);
				Tween.to(item, ItemAccessor.POSITION, MOVE_TIME)
						.target(fieldRect.x + item.x * cellSize, fieldRect.y + item.y * cellSize)
						.start(MyTweenManager.getInstance())
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.setCallback(new TweenCallback() {
							@Override
							public void onEvent(int type, BaseTween<?> source) {
								backgroundItems.remove(item);
								foregroundItems.remove(item);
								actionLock.remove(item);
							}
						});
				item.moveFlag = false;
				item.reverseMove = false;
			}
			if (item.destroyFlag) {
				actionLock.add(item);
				Tween.to(item, ItemAccessor.ALPHA, MOVE_TIME)
						.target(0)
						.start(MyTweenManager.getInstance())
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.setCallback(new TweenCallback() {
							@Override
							public void onEvent(int type, BaseTween<?> source) {
								field.items.remove(item);
								actionLock.remove(item);
							}
						});
				item.destroyFlag = false;
			}
			if (item.explodeFlag) {
				actionLock.add(item);
				Random rand = new Random();
				Timeline timeline = Timeline.createParallel().beginSequence();
				for (int i = 0; i < JOLT_COUNT; i++) {
					float angle = (float) (rand.nextFloat() * Math.PI * 2);
					float radius = cellSize * JOLT_RADIUS / 2;
					float targetX = fieldRect.x + item.x * cellSize + (float) Math.cos(angle) * radius;
					float targetY = fieldRect.y + item.y * cellSize + (float) Math.sin(angle) * radius;
					timeline.push(Tween.to(item, ItemAccessor.POSITION, JOLT_PERIOD).target(targetX, targetY));
				}
				timeline.end();
				float explosionTime = JOLT_PERIOD * JOLT_COUNT;
				timeline.beginSequence()
							.pushPause(explosionTime / 2)
							.push(Tween.to(item, ItemAccessor.ALPHA, explosionTime / 2).target(0))
						.end()
						.push(Tween.to(item, ItemAccessor.SCALE, EXPLOSION_REDUCTION_TIME)
								.target(EXPLOSION_REDUCTION_RATIO, EXPLOSION_REDUCTION_RATIO))
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.setCallback(new TweenCallback() {
							@Override
							public void onEvent(int type, BaseTween<?> source) {
								field.items.remove(item);
								actionLock.remove(item);
							}
						}).start(MyTweenManager.getInstance());
				item.explodeFlag = false;
			}
		}
		for (Item item : foregroundItems) {
			item.draw(batch, item.alpha);
		}
	}
	
	@Override
	public void render(float delta) {
		GLCleaner.clearARGB(0xffffffff);
		batch.begin();
		drawBackground();
		drawField();
		drawItems();
		actionLock.remove(this);
		batch.end();
	}
}
