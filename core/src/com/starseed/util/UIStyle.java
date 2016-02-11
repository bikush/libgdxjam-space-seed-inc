package com.starseed.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class UIStyle {
	private FreeTypeFontParameter parameter;
	private Skin skin;
	
	public UIStyle() {		
		parameter = new FreeTypeFontParameter();
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,-+:!'()<>?: ";
		skin = new Skin();
		addRocketButtonStyle();
		addBlankButtonStyle();
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
	
	private void addBlankButtonStyle(){
		if( !skin.has("blankButtonBg", TextureRegion.class) ){
			Pixmap pMap = new Pixmap(128, 128, Format.RGBA8888);
			pMap.setColor(0.2f, 0.4f, 0.7f, 0.5f);
			pMap.fill();
			skin.add("blankButtonBg", new TextureRegion( new Texture( pMap ) ), TextureRegion.class);
			pMap.dispose();
		}
		if( !skin.has("blankButtonBgDown", TextureRegion.class) ){
			Pixmap pMap = new Pixmap(128, 128, Format.RGBA8888);
			pMap.setColor(0.35f, 0.55f, 0.7f, 0.5f);
			pMap.fill();
			skin.add("blankButtonBgDown", new TextureRegion( new Texture( pMap ) ), TextureRegion.class);
			pMap.dispose();
		}

		TextButtonStyle blankButtonStyle = new TextButtonStyle();
		blankButtonStyle.up = skin.getDrawable("blankButtonBg");
		blankButtonStyle.down = skin.getDrawable("blankButtonBgDown");
		blankButtonStyle.over = blankButtonStyle.up;
		blankButtonStyle.font = getFont(24);
		skin.add("blankButton", blankButtonStyle);
	}

	public TextButtonStyle getRightRocketButtonStyle() {
		return skin.get("rightRocketButton", TextButtonStyle.class);
	}
	
	public TextButtonStyle getLeftRocketButtonStyle() {
		return skin.get("leftRocketButton", TextButtonStyle.class);
	}
	
	public TextButtonStyle getBlankButtonStyle() {
		return skin.get("blankButton", TextButtonStyle.class);
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
