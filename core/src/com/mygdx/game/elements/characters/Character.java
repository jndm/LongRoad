package com.mygdx.game.elements.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.ai.Ai;
import com.mygdx.game.elements.items.Equipment;
import com.mygdx.game.elements.skills.Skill;


public abstract class Character {
	
	protected Ai ai;
	protected Texture texture;
	protected TextureRegion[] textureReg;
	protected Array<Equipment> equipment;
	protected float x, y;
	protected float movementSpeed = 100;
	protected float maxHp;
	protected float hp;
	protected float maxMana;
	protected float mana;
	protected int strength;
	protected int agility;
	protected int intelligence;
	protected float attackSpeed;
	protected float attackChargeMax = 100;
	protected float attackCharge = 0;
	protected float defaultAttackDmg;
	protected Array<Skill> attackAbilities;
	protected Array<Skill> spells;
	
	protected Animation actAnimation;
	protected boolean actFinished = true;
	protected float elapsedTime = 0;
	
	public Character(int x, int y, float maxHp, float maxMana, float attackspeed, int strength, int agility, int intelligence) {
		this.x = x;
		this.y = y;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.maxMana = maxMana;
		this.mana = maxMana;
		this.attackSpeed = attackspeed;
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
	}
	
	public Character(int x, int y, float maxHp, float maxMana, float attackspeed, int strength, int agility, int intelligence, Ai ai) {
		this.x = x;
		this.y = y;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.maxMana = maxMana;
		this.mana = maxMana;
		this.attackSpeed = attackspeed;
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
		this.ai = ai;
	}
	
	public void render(SpriteBatch sb){
		sb.begin();	
		if(!actFinished) {
			elapsedTime += Gdx.graphics.getDeltaTime();
			sb.draw(actAnimation.getKeyFrame(elapsedTime, false), x, y);
			if(actAnimation.isAnimationFinished(elapsedTime)) {
				actFinished = true;
				elapsedTime = 0;
			}
		} else {
			sb.draw(texture, x, y);
		}
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
	
	public void moveBackward(float dt){
		x -= movementSpeed * dt;
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
	
	//JUST FOR TESTING
	public void createAttackAnimation(TextureRegion t1, TextureRegion t2, TextureRegion t3) {
		TextureRegion[] tmp = new TextureRegion[10];
		tmp[0] = t2;
		tmp[1] = t2;
		tmp[2] = t3;
		tmp[3] = t1;
		tmp[4] = t2;
		tmp[5] = t3;
		tmp[6] = t1;
		tmp[7] = t2;
		tmp[8] = t3;
		tmp[9] = t1;
		actAnimation = new Animation(1/10f, tmp);
	}
	
	public Skill raffleSkillToUse() {
		return ai.raffleSkillToUse(attackAbilities, spells);
	}

	public Character raffleTarget(Array<Character> playerCharacters) {
		return ai.raffleTarget(playerCharacters);
	}
	
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

	public Array<Skill> getAttackAbilities() { return attackAbilities; }
	public void setAttackAbilities(Array<Skill> attackAbilities) { this.attackAbilities = attackAbilities; }

	public Array<Skill> getSpells() { return spells; }
	public void setSpells(Array<Skill> spells) { this.spells = spells; }

	public int getStrength() { return strength; }
	public void setStrength(int strength) { this.strength = strength; }

	public int getAgility() { return agility; }
	public void setAgility(int agility) { this.agility = agility; }

	public int getIntelligence() { return intelligence; }
	public void setIntelligence(int intelligence) { this.intelligence = intelligence; }

	public boolean isActFinished() { return actFinished; }
	public void setActFinished(boolean actfinished) { this.actFinished = actfinished; }

	public Animation getActAnimation() { return actAnimation; }
	public void setActAnimation(Animation actAnimation) { this.actAnimation = actAnimation; }
	
}
