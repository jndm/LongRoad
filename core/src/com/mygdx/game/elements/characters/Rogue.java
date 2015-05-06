package com.mygdx.game.elements.characters;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.items.Equipment;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.skills.attacks.DefaultAttack;
import com.mygdx.game.elements.skills.attacks.SpecialAttack;
import com.mygdx.game.elements.skills.spells.DefaultSpell;
import com.mygdx.game.elements.skills.spells.SpecialSpell;


public class Rogue extends Character {
	
	public Rogue(String name, int x, int y, float maxHp, float maxMana, float attackspeed,
			int strength, int agility, int intelligence) {
		super(name, x, y, maxHp, maxMana, attackspeed, strength, agility, intelligence);
		attackAbilities = new Array<Skill>();	
		spells = new Array<Skill>();
	}
	
}
