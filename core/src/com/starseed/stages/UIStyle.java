package com.starseed.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.starseed.util.Constants;

public class UIStyle {
	private FreeTypeFontParameter parameter;
	private Skin skin;
	private static UIStyle singleton=null;
	
	private UIStyle() {
		parameter = new FreeTypeFontParameter();
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,-:!'()>?: ";
		skin = new Skin();
		addRocketButtonStyle();
	}
	
	public static UIStyle getSingleton() {
		if (singleton == null) {
			singleton = new UIStyle();
		}
		return singleton;
	}
	private void addRocketButtonStyle() {
		TextButtonStyle rigthButtonStyle = new TextButtonStyle();
		skin.addRegions(new TextureAtlas(Gdx.files.internal(Constants.R_BUTTON_ATLAS_PATH)));
		rigthButtonStyle.up = skin.newDrawable("r0");
		rigthButtonStyle.down = skin.newDrawable("r1");
		rigthButtonStyle.over = skin.newDrawable("r2");
		rigthButtonStyle.font = getFont(18);
		skin.add("rightRocketButton", rigthButtonStyle);
		TextButtonStyle leftButtonStyle = new TextButtonStyle();
		skin.addRegions(new TextureAtlas(Gdx.files.internal(Constants.L_BUTTON_ATLAS_PATH)));
		leftButtonStyle.up = skin.newDrawable("l0");
		leftButtonStyle.down = skin.newDrawable("l1");
		leftButtonStyle.over = skin.newDrawable("l2");
		leftButtonStyle.font = getFont(20);
		skin.add("leftRocketButton", leftButtonStyle);
	}

	public TextButtonStyle getRightRocketButtonStyle() {
		return skin.get("rightRocketButton", TextButtonStyle.class);
	}
	
	public TextButtonStyle getLeftRocketButtonStyle() {
		return skin.get("leftRocketButton", TextButtonStyle.class);
	}
	
	public LabelStyle getLabelStyle(int fontSize, Color fontColor) {
		String name = String.format("label_%d_%s", fontSize, fontColor.toString());
		if (!skin.has(name, LabelStyle.class)) {
			LabelStyle lStyle = new LabelStyle(getFont(fontSize), fontColor);
			skin.add(name, lStyle);
		}
		return skin.get(name, LabelStyle.class);
	}
	
	public LabelStyle getTitleLabelStyle(int fontSize, Color fontColor) {
		String name = String.format("title_label_%d_%s", fontSize, fontColor.toString());
		if (!skin.has(name, LabelStyle.class)) {
			LabelStyle lStyle = new LabelStyle(getTitleFont(fontSize), fontColor);
			skin.add(name, lStyle);
		}
		return skin.get(name, LabelStyle.class);
	}
	
	public BitmapFont getTitleFont(int fontSize) {
		String name = String.format("title_font_%d", fontSize);
		if (!skin.has(name, BitmapFont.class)) {
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.GAME_FONT_TITLE));
			parameter.size = fontSize;
			BitmapFont bfont= generator.generateFont(parameter);
			skin.add(name, bfont);
			generator.dispose();
		}
		return skin.getFont(name);		
	}
	
	public BitmapFont getFont(int fontSize) {
		String name = String.format("font_%d", fontSize);
		if (!skin.has(name, BitmapFont.class)) {
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.GAME_FONT));
			parameter.size = fontSize;
			BitmapFont bfont= generator.generateFont(parameter);
			skin.add(name, bfont);
			generator.dispose();
		}
		return skin.getFont(name);
	}
	
	public void dispose() {
		skin.dispose();
	}
}
