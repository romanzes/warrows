package ru.footmade.warrows.screens;

import java.util.ArrayList;
import java.util.List;

import ru.footmade.warrows.tweens.MyTweenManager;
import ru.footmade.warrows.tweens.ObserverAccessor;
import ru.footmade.warrows.util.CommonResources;
import ru.footmade.warrows.util.GLCleaner;
import ru.footmade.warrows.util.Observer;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ComicScreen extends ScreenAdapter {
	public static final String COMIC_INTRO = "intro";
	
	private String comicId;
	
	private SpriteBatch batch;
	private int bgColor;
	private int scrW, scrH;
	private int comicW, comicH;
	private float screenRatio;
	
	private Observer observer;
	
	private final List<FrameInfo> frames = new ArrayList<FrameInfo>();
	private final List<WaypointInfo> waypoints = new ArrayList<WaypointInfo>();
	private int currentWaypoint = 0;
	
	public ComicScreen(String comicId) {
		this.comicId = comicId;
		batch = new SpriteBatch();
		scrW = Gdx.graphics.getWidth();
		scrH = Gdx.graphics.getHeight();
		readJson(Gdx.files.internal("comics/" + comicId + ".txt").readString());
		screenRatio = Math.min((float) comicW / scrW, (float) comicH / scrH);
	}
	
	private void readJson(String str) {
		JsonValue map = new JsonReader().parse(str);
		
		bgColor = (int) Long.parseLong(map.getString("color"), 16);
		comicW = map.getInt("width");
		comicH = map.getInt("height");
		
		JsonValue framesElem = map.getChild("frames");
		for (; framesElem != null; framesElem = framesElem.next()) {
			frames.add(readFrame(framesElem));
		}
		
		JsonValue waypointsElem = map.getChild("waypoints");
		for (; waypointsElem != null; waypointsElem = waypointsElem.next()) {
			waypoints.add(readWaypoint(waypointsElem));
		}
	}
	
	private FrameInfo readFrame(JsonValue framesElem) {
		FrameInfo result = new FrameInfo();
		result.picture = CommonResources.getLinearRegion("comics/" + comicId + "/" + framesElem.getString("img"));
		float rectX = framesElem.getInt("x");
		float rectY = framesElem.getInt("y");
		float rectW = framesElem.getInt("width");
		float rectH = framesElem.getInt("height");
		rectY = comicH - rectY - rectH;
		result.rect = new Rectangle(rectX, rectY, rectW, rectH);
		return result;
	}
	
	private WaypointInfo readWaypoint(JsonValue waypointsElem) {
		WaypointInfo result = new WaypointInfo();
		result.x = waypointsElem.getInt("x");
		result.y = comicH - waypointsElem.getInt("y");
		result.scale = waypointsElem.getFloat("scale");
		result.delay = waypointsElem.getFloat("delay", 0);
		result.period = waypointsElem.getFloat("period", 0);
		String ease = waypointsElem.getString("ease", "inout");
		if (ease.equals("in"))
			result.ease = Quad.IN;
		else if (ease.equals("out"))
			result.ease = Quad.OUT;
		else
			result.ease = Quad.INOUT;
		return result;
	}
	
	private void nextWaypoint() {
		WaypointInfo current = waypoints.get(currentWaypoint);
		currentWaypoint++;
		if (currentWaypoint < waypoints.size()) {
			WaypointInfo next = waypoints.get(currentWaypoint);
			float targetScale = next.scale / screenRatio;
			Tween.to(observer, ObserverAccessor.POSITION, next.period)
					.target(next.x, next.y, targetScale)
					.start(MyTweenManager.getInstance())
					.ease(next.ease)
					.delay(current.delay)
					.setCallback(new TweenCallback() {
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							if (type == TweenCallback.COMPLETE)
								nextWaypoint();
						}
					});
		} else {
			//enableInput();
		}
	}
	
	@Override
	public void render(float delta) {
		GLCleaner.clearARGB(bgColor);
		batch.begin();
		float left = observer.x - scrW / observer.scale / 2;
		float bottom = observer.y - scrH / observer.scale / 2;
		for (FrameInfo frame : frames) {
			batch.draw(frame.picture, (frame.rect.x - left) * observer.scale, (frame.rect.y - bottom) * observer.scale,
					(float) Math.ceil(frame.rect.width * observer.scale), (float) Math.ceil(frame.rect.height * observer.scale));
		}
		batch.end();
	}
	
	@Override
	public void show() {
		WaypointInfo firstWaypoint = waypoints.get(0);
		observer = new Observer(firstWaypoint.x, firstWaypoint.y, firstWaypoint.scale);
		nextWaypoint();
	}
	
	private void enableInput() {
		Gdx.input.setInputProcessor(new InputProcessor() {
			private int dragX, dragY;
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				int dx = screenX - dragX;
				int dy = screenY - dragY;
				dragX = screenX;
				dragY = screenY;
				observer.x -= dx;
				observer.y += dy;
				return false;
			}
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if (button == Buttons.MIDDLE) {
					System.out.println("lol");
				}
				dragX = screenX;
				dragY = screenY;
				return true;
			}
			
			@Override
			public boolean scrolled(int amount) {
				observer.scale += amount * 0.1f;
				return true;
			}
			
			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}
		});
	}
	
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
	
	private static class FrameInfo {
		private TextureRegion picture;
		private Rectangle rect;
	}
	
	private static class WaypointInfo {
		private float x, y;
		private float scale;
		private float delay;
		private float period;
		private TweenEquation ease;
	}
}
