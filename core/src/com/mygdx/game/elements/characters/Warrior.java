package com.mygdx.game.elements.characters;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Abilities.Ability;
import com.mygdx.game.elements.characters.Abilities.WarriorAbilities;
import com.mygdx.game.elements.items.Equipment;


public class Warrior extends Character {
	
	public Warrior(int x, int y, float maxHp, float maxMana, float attackSpeed) {
		super(x, y, maxHp, maxMana, attackSpeed);
		attackAbilities = new Array<Ability>();
		attackAbilities.add(WarriorAbilities.DEFAULTWATTACK);
		attackAbilities.add(WarriorAbilities.SPECIALWSKILL);
		attackAbilities.add(WarriorAbilities.SPECIALWSKILL2);
		attackAbilities.add(WarriorAbilities.SPECIALWSKILL3);
		attackAbilities.add(WarriorAbilities.SPECIALWSKILL4);
		attackAbilities.add(WarriorAbilities.SPECIALWSKILL5);
		attackAbilities.add(WarriorAbilities.SPECIALWSKILL6);
		attackAbilities.add(WarriorAbilities.SPECIALWSKILL7);
		
		
		spells = new Array<Ability>();
		spells.add(WarriorAbilities.DEFAULTWCAST);
		spells.add(WarriorAbilities.SPECIALWCAST);
		spells.add(WarriorAbilities.SPECIALWCAST);
		spells.add(WarriorAbilities.SPECIALWCAST);
		spells.add(WarriorAbilities.SPECIALWCAST);
		spells.add(WarriorAbilities.SPECIALWCAST);
		
		hp = 60;
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEquipment(Equipment equipment) {
		// TODO Auto-generated method stub
		
	}
	
	public void addUnlockAttackSkill(Abilities.WarriorAbilities ability) {
		attackAbilities.add(ability);
	}

	public Array<Ability> getAttackAbilities() {
		return attackAbilities;
	}
	
	public void addUnlockCastSkill(Abilities.WarriorAbilities ability) {
		spells.add(ability);
	}

	public Array<Ability> getSpells() {
		return spells;
	}

	
}
