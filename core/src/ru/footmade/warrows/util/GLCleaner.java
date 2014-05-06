package ru.footmade.warrows.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GLCleaner {
	public static void clearARGB(int color) {
		float alpha = ((color >> 24) & 0xff) / 0xff;
		float red = ((color >> 16) & 0xff) / 0xff;
		float green = ((color >> 8) & 0xff) / 0xff;
		float blue = (color & 0xff) / 0xff;
		Gdx.gl.glClearColor(red, green, blue, alpha);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
