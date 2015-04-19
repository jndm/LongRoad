package com.mygdx.game.elements.characters;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Abilities.RogueAbilities;
import com.mygdx.game.elements.items.Equipment;


public class Rogue extends Character {

	private Array<RogueAbilities> attackAbilities;
	private Array<RogueAbilities> spells;
	
	public Rogue(int x, int y, float maxHp, float maxMana, float attackSpeed) {
		super(x, y, maxHp, maxMana, attackSpeed);
		attackAbilities = new Array<RogueAbilities>();
		attackAbilities.add(RogueAbilities.DEFAULTRATTACK);
		attackAbilities.add(RogueAbilities.SPECIALRSKILL);
		
		spells = new Array<RogueAbilities>();
		spells.add(RogueAbilities.DEFAULTRCAST);
		spells.add(RogueAbilities.SPECIALRCAST);
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
	
	public void addUnlockAttackSkill(Abilities.RogueAbilities ability) {
		attackAbilities.add(ability);
	}

	public Array<Abilities.RogueAbilities> getAttackAbilities() {
		return attackAbilities;
	}
	
	public void addUnlockCastSkill(Abilities.RogueAbilities ability) {
		spells.add(ability);
	}

	public Array<Abilities.RogueAbilities> getSpells() {
		return spells;
	}
	
}
