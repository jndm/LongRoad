package com.mygdx.game.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


public abstract class Character {
	
	private Texture texture;
	private TextureRegion[] textureReg;
	private Array<Equipment> equipment;
	private float x, y;
	private final float SPEED = 50;
	
	public Character(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(texture, x, y);
		sb.end();
	}
	
	public void move(float dt){
		x += SPEED * dt;
	}
	
	public abstract void attack();
	public abstract void cast();
	public abstract void addEquipment(Equipment equipment);
	
	public void setTexture(Texture t){ texture = t; }
	public Texture getTexture() { return texture; }
	public float getX() { return x; }
	public float getY() { return y; }
}
