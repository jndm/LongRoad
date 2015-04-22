package com.mygdx.game.elements.characters;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.items.Equipment;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.skills.attacks.DefaultAttack;
import com.mygdx.game.elements.skills.attacks.SpecialAttack;
import com.mygdx.game.elements.skills.spells.DefaultSpell;
import com.mygdx.game.elements.skills.spells.SpecialSpell;


public class Mage extends Character {
	
	public Mage(int x, int y, float maxHp, float maxMana, float attackspeed,
			int strength, int agility, int intelligence) {
		super(x, y, maxHp, maxMana, attackspeed, strength, agility, intelligence);
		attackAbilities = new Array<Skill>();
		attackAbilities.add(new DefaultAttack());
		attackAbilities.add(new SpecialAttack());
		
		spells = new Array<Skill>();
		spells.add(new DefaultSpell());
		spells.add(new SpecialSpell());
	}
	
	public void attack(Character target) {
		
	}

	public void cast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEquipment(Equipment equipment) {
		// TODO Auto-generated method stub
		
	}
	
	public void addUnlockAttackSkill(Skill ability) {
		attackAbilities.add(ability);
	}

	public Array<Skill> getAttackAbilities() {
		return attackAbilities;
	}
	
	public void addUnlockCastSkill(Skill ability) {
		spells.add(ability);
	}

	public Array<Skill> getSpells() {
		return spells;
	}
	
}
