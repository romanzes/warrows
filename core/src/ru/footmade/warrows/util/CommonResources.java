package ru.footmade.warrows.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class CommonResources implements Disposable {
	private static CommonResources _instance;
	
	private TextureAtlas atlas;
	
	private static CommonResources getInstance() {
		if (_instance == null)
			_instance = new CommonResources();
		return _instance;
	}
	
	private CommonResources() {
		atlas = new TextureAtlas(Gdx.files.internal("img/pack.atlas"));
	}
	
	public static TextureAtlas getAtlas() {
		return getInstance().atlas;
	}
	
	public static TextureRegion getRegion(String name) {
		return getAtlas().findRegion(name);
	}
	
	public static TextureRegion getLinearRegion(String name) {
		return getRegion(name, TextureFilter.Linear);
	}
	
	public static TextureRegion getRegion(String name, TextureFilter filter) {
		TextureRegion result = getAtlas().findRegion(name);
		result.getTexture().setFilter(filter, filter);
		return result;
	}

	@Override
	public void dispose() {
		getAtlas().dispose();
	}
	
	public static void disposeAll() {
		getInstance().dispose();
	}
}
