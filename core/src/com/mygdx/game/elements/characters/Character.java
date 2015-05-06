package com.mygdx.game.elements.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.ai.Ai;
import com.mygdx.game.elements.items.Equipment;
import com.mygdx.game.elements.skills.Skill;


public abstract class Character {
	
	protected Ai ai;
	protected TextureRegion textureReg;
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
	protected String name;
	protected Vector2 battleposition;
	protected boolean alive = true;
	
	protected Animation attackAnimation, moveAnimation;
	protected boolean attacking = false, moving = false;
	protected float elapsedTime = 0;
	
	public Character(String name, int x, int y, float maxHp, float maxMana, float attackspeed, int strength, int agility, int intelligence) {
		this.name = name;
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
		if(attacking) {
			elapsedTime += Gdx.graphics.getDeltaTime();
			sb.draw(attackAnimation.getKeyFrame(elapsedTime, false), x, y);
			if(attackAnimation.isAnimationFinished(elapsedTime)) {
				attacking = false;
			}
		} else if(moving) {
			elapsedTime += Gdx.graphics.getDeltaTime();
			sb.draw(moveAnimation.getKeyFrame(elapsedTime, true), x, y);
		}else {
			sb.draw(textureReg, x, y);
		}
		sb.end();
	}
	
	public void render(SpriteBatch sb, int _x, int _y){
		sb.begin();
		sb.draw(textureReg, _x, _y);
		sb.end();
	}
	
	public void move(float dt){
		moving = true;
		x += movementSpeed * dt;
	}
	
	public void moveBackward(float dt){
		moving = true;
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
		attackAnimation = new Animation(1/10f, tmp);
	}
	
	public void createMovingAnimation(TextureRegion[] tr) {
		moveAnimation = new Animation(1/10f, tr);
	}
	
	public Skill raffleSkillToUse() {
		return ai.raffleSkillToUse(attackAbilities, spells);
	}

	public Character raffleTarget(Array<Character> playerCharacters) {
		return ai.raffleTarget(playerCharacters);
	}
	
	public void addAttackAbility(Skill ability) {
		attackAbilities.add(ability);
	}
	public void addSpell(Skill ability) {
		spells.add(ability);
	}
	
	public void setTextureRegion(TextureRegion t){ textureReg = t; }
	public TextureRegion getTextureRegion() { return textureReg; }
	
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

	public boolean isAttacking() { return attacking; }
	
	public void setAttacking(boolean attacking) {
		if(attacking) {
			elapsedTime = 0;
		}
		this.attacking = attacking; 
	}

	public Animation getActAnimation() { return attackAnimation; }
	public void setActAnimation(Animation actAnimation) { this.attackAnimation = actAnimation; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public Vector2 getBattleposition() { return battleposition; }
	public void setBattleposition(Vector2 battleposition) { this.battleposition = battleposition; }

	public boolean isAlive() { return alive; }
	public void setAlive(boolean alive) { this.alive = alive;}

	public boolean isMoving() { return moving; }
	public void setMoving(boolean moving) { 
		if(moving) {
			elapsedTime = 0;
		}
		this.moving = moving; 
	}
		
}
