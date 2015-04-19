package com.mygdx.game.elements.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Abilities.Ability;
import com.mygdx.game.elements.items.Equipment;


public abstract class Character {
	
	protected Texture texture;
	protected TextureRegion[] textureReg;
	protected Array<Equipment> equipment;
	protected float x, y;
	protected float movementSpeed = 100;
	protected float maxHp;
	protected float hp;
	protected float maxMana;
	protected float mana;
	protected float attackSpeed;
	protected float attackChargeMax = 100;
	protected float attackCharge = 0;
	protected float defaultAttackDmg;
	protected Array<Ability> attackAbilities;
	protected Array<Ability> spells;
	
	public Character(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Character(int x, int y, float maxHp, float maxMana, float attackspeed) {
		this.x = x;
		this.y = y;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.maxMana = maxMana;
		this.mana = maxMana;
		this.attackSpeed = attackspeed;
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
	
	public float getAttackSpeed() { return attackSpeed; }
	public void setAttackSpeed(int attackSpeed) { this.attackSpeed = attackSpeed; }
	
	public void setDefaultAttackDmg(float defAttDmg) { this.defaultAttackDmg = defAttDmg; }
	public float getDefaultAttackDmg() { return defaultAttackDmg; }
	
	public float getAttackChargeMax() { return attackChargeMax; }
	public void setAttackChargeMax(float attackCharge) { this.attackChargeMax = attackCharge; }
	
	public float getAttackCharge() { return attackCharge; }
	public void setAttackCharge(float attackCharge) { this.attackCharge = attackCharge; }

	public float getMana() { return mana; }
	public void setMana(int mana) { this.mana = mana; }

	public float getMaxHp() { return maxHp; }
	public void setMaxHp(float maxHp) { this.maxHp = maxHp; }

	public float getMaxMana() { return maxMana; }
	public void setMaxMana(float maxMana) { this.maxMana = maxMana; }

	public Array<Ability> getAttackAbilities() { return attackAbilities; }
	public void setAttackAbilities(Array<Ability> attackAbilities) { this.attackAbilities = attackAbilities; }

	public Array<Ability> getSpells() { return spells; }
	public void setSpells(Array<Ability> spells) { this.spells = spells; }	
	
	
	
}
