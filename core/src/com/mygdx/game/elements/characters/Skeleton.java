package com.mygdx.game.elements.characters;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.ai.Ai;
import com.mygdx.game.elements.items.Equipment;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.skills.attacks.DefaultAttack;
import com.mygdx.game.elements.skills.attacks.SpecialAttack;
import com.mygdx.game.elements.skills.spells.DefaultSpell;
import com.mygdx.game.elements.skills.spells.SpecialSpell;

public class Skeleton extends Character {
	
	public Skeleton(int x, int y, float maxHp, float maxMana, float attackspeed, int strength, int agility, int intelligence, Ai ai) {
		super(x, y, maxHp, maxMana, attackspeed, strength, agility, intelligence, ai);
		attackAbilities = new Array<Skill>();
		attackAbilities.add(new DefaultAttack());
		attackAbilities.add(new SpecialAttack());
		
		spells = new Array<Skill>();
		spells.add(new DefaultSpell());
		spells.add(new SpecialSpell());
	}
	
	public void attack(Character target) {
		// TODO Auto-generated method stub
		
	}

	public void cast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEquipment(Equipment equipment) {
		// TODO Auto-generated method stub
		
	}
	
}
