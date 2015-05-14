package com.mygdx.game.elements.characters;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.ai.Ai;
import com.mygdx.game.elements.skills.Skill;

public class Skeleton extends Character {
	
	public Skeleton(int x, int y, float maxHp, float maxMana, float attackspeed, int strength, int agility, int intelligence, Ai ai) {
		super(x, y, maxHp, maxMana, attackspeed, strength, agility, intelligence, ai);
		attackAbilities = new Array<Skill>();
		spells = new Array<Skill>();
	}
	
}
