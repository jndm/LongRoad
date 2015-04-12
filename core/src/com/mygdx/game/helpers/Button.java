package com.mygdx.game.helpers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Game;

public class Button {
	private int x;
	private int y;
	private int width;
	private int height;
	private ButtonAction action;
	private TextureRegion[] texture = new TextureRegion[2];
	private boolean clicked = false;
	private final int NOT_CLICKED = 0;
	private final int CLICKED = 1;
	private String text;
	
	private BitmapFont font;
	
	public Button(int x, int y, int width, int height, String text, ButtonAction action, BitmapFont font) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.action = action;
		this.text = text;
		this.font = font;
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		if(!clicked){
			sb.draw(texture[NOT_CLICKED], x, y);
		} else {
			sb.draw(texture[CLICKED], x, y);
		}
		font.draw(sb, text, x + width/2 - font.getBounds(text).width/2, y + height/2 + font.getBounds(text).height/2);
		sb.end();
	}
	
	public boolean isMouseOnButton(int mx, int my) {
		int myInverted = Game.HEIGHT - my - 1;
		if(mx < x || mx > x + width || 
				myInverted < y || myInverted > y + height) {
			clicked = false;
			return clicked;
		} else {
			clicked = true;
			return clicked;
		}
	}

	
	public ButtonAction getAction() {
		return action;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setTextureRegion(TextureRegion[] t) {
		this.texture = t;
	}
	
	public void setBounds(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public boolean isClicked() {
		return clicked;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	
	public void dispose() {
		font.dispose();
	}
}