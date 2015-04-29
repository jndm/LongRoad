package com.mygdx.game.helpers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Game;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.skills.Skill;

public class Button {
	private int x;
	private int y;
	private int width;
	private int height;
	private ButtonAction action;
	private Skill action2;
	private Item item;
	private TextureRegion[] texture = new TextureRegion[3];
	private boolean clicked = false;
	private boolean clickable = true;
	private final int NOT_CLICKED = 0;
	private final int CLICKED = 1;
	private final int NOT_CLICKABLE = 2;
	private String text;
	
	private BitmapFont font;
	
	//Constructor for main buttons
	public Button(int x, int y, int width, int height, String text, ButtonAction action, BitmapFont font) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.action = action;
		this.text = text;
		this.font = font;
	}
	
	//Constructor for sub buttons
	public Button(int x, int y, int width, int height, String text, Skill action, BitmapFont font) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.action2 = action;
		this.text = text;
		this.font = font;
	}
	
	//Constructor for item buttons
	public Button(int x, int y, int width, int height, String text, Item item, BitmapFont font) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.item = item;
		this.text = text;
		this.font = font;
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		if(!clickable) {
			sb.draw(texture[NOT_CLICKABLE], x, y);
		} else {
			if(!clicked){
				sb.draw(texture[NOT_CLICKED], x, y);
			} else {
				sb.draw(texture[CLICKED], x, y);
			}
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
	
	public Skill getAction2() {
		return action2;
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

	public void setClickable(boolean b) {
		clickable = b;
	}

	public Item getItem() {
		return item;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextureRegion[] getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion[] texture) {
		this.texture = texture;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public boolean isClickable() {
		return clickable;
	}

	public String getText() {
		return text;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setAction(ButtonAction action) {
		this.action = action;
	}

	public void setAction2(Skill action2) {
		this.action2 = action2;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	
}
