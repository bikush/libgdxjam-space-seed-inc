package com.starseed.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
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
	
	public Label addLabel(String text, int fontSize, Color fontColor, 
			               int posX, int posY, Boolean istitle) {
		LabelStyle lStyle = (istitle) ? getTitleLabelStyle(fontSize, fontColor):
								        getLabelStyle(fontSize, fontColor);
		final Label label = new Label(text, lStyle);
		label.setPosition(posX, posY);
		return label;
	}
	
	private void addRocketButtonStyle() {
		TextButtonStyle rigthButtonStyle = new TextButtonStyle();
		skin.addRegions(new TextureAtlas(Gdx.files.internal(Constants.ROCKET_BUTTON_ATLAS_PATH)));
		rigthButtonStyle.up = skin.newDrawable("play_no_flames");
		rigthButtonStyle.down = skin.newDrawable("play_with_flames_pressed");
		rigthButtonStyle.over = skin.newDrawable("play_with_flames_over");
		rigthButtonStyle.font = getFont(18);
		skin.add("rightRocketButton", rigthButtonStyle);
		TextButtonStyle leftButtonStyle = new TextButtonStyle();
		leftButtonStyle.up = skin.newDrawable("quit_no_flames");
		leftButtonStyle.over = skin.newDrawable("quit_with_flames_over");
		leftButtonStyle.down = skin.newDrawable("quit_with_flames_pressed");
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
