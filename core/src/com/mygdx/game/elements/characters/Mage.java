package com.mygdx.game.elements.characters;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Abilities.Ability;
import com.mygdx.game.elements.characters.Abilities.MageAbilities;
import com.mygdx.game.elements.items.Equipment;


public class Mage extends Character {
	
	public Mage (int x, int y, float maxHp, float maxMana, float attackSpeed) {
		super(x, y, maxHp, maxMana, attackSpeed);
		attackAbilities = new Array<Ability>();
		attackAbilities.add(MageAbilities.DEFAULTMATTACK);
		attackAbilities.add(MageAbilities.SPECIALMSKILL);
		
		spells = new Array<Ability>();
		spells.add(MageAbilities.DEFAULTMCAST);
		spells.add(MageAbilities.SPECIALMCAST);
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
	
	public void addUnlockAttackSkill(Abilities.MageAbilities ability) {
		attackAbilities.add(ability);
	}

	public Array<Ability> getAttackAbilities() {
		return attackAbilities;
	}
	
	public void addUnlockCastSkill(Abilities.MageAbilities ability) {
		spells.add(ability);
	}

	public Array<Ability> getSpells() {
		return spells;
	}
	
}
