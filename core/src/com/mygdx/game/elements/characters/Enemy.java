package com.mygdx.game.elements.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.elements.items.Equipment;

public abstract class Enemy {
	private float hp;
	private Texture texture;
	private TextureRegion[] textureReg;
	private float x, y;
	
	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Enemy(){}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(texture, x, y);
		sb.end();
	}
	
	public void render(SpriteBatch sb, int _x, int _y){
		sb.begin();
		sb.draw(texture, _x, _y);
		sb.end();
	}
	
	public abstract void attack();
	public abstract void cast();
	public abstract void addEquipment(Equipment equipment);
	public abstract void addCharge();
	
	public void setTexture(Texture t){ texture = t; }
	public Texture getTexture() { return texture; }
	public float getX() { return x; }
	public float getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setHp(float hp) { this.hp = hp; }
	public float getHp() { return hp; }
}
