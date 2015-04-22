package com.mygdx.game.elements.skills.attacks;

import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class SpecialAttack implements Skill{

	@Override
	public void act(Character actor, Character target) {
		System.out.print("Doing specialtattack!");
	}
	
	@Override
	public String toString() {
		return "Specialattack";
	}

}
