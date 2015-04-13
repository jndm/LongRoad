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
	private float movementSpeed = 100;
	private float hp;
	private int mana;
	private int attackSpeed;
	private float attackChargeMax = 100;
	private float attackCharge = 0;
	private float defaultAttackDmg;
	
	public Character(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
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
	
	public void move(float dt){
		x += movementSpeed * dt;
	}
	
	public boolean isCharged() {
		if(attackCharge >= attackChargeMax) {
			attackCharge = 0;
			return true;
		} else {
			return false;
		}
	}
	
	public void addCharge() {
		attackCharge += attackSpeed;
	}
	
	public abstract void attack();
	public abstract void cast();
	public abstract void addEquipment(Equipment equipment);
	
	public void setTexture(Texture t){ texture = t; }
	public Texture getTexture() { return texture; }
	
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = y; }
	public void setXY(float x, float y) { this.x = x; this.y = y; }
	
	public float getX() { return x; }
	public float getY() { return y; }
	
	public float getHp() { return hp; }
	public void setHp(float hp) { this.hp = hp; }
	
	public int getAttackSpeed() { return attackSpeed; }
	public void setAttackSpeed(int attackSpeed) { this.attackSpeed = attackSpeed; }
	
	public void setDefaultAttackDmg(float defAttDmg) { this.defaultAttackDmg = defAttDmg; }
	public float getDefaultAttackDmg() { return defaultAttackDmg; }
	
	public float getAttackChargeMax() { return attackChargeMax; }
	public void setAttackChargeMax(float attackCharge) { this.attackChargeMax = attackCharge; }
	
	public float getAttackCharge() { return attackCharge; }
	public void setAttackCharge(float attackCharge) { this.attackCharge = attackCharge; }	

}
