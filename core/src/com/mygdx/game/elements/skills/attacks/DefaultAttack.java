package com.mygdx.game.elements.skills.attacks;

import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class DefaultAttack implements Skill{

	@Override
	public void act(Character actor, Character target) {
		float damage = actor.getStrength();
		float targetHp = target.getHp() - damage;
		target.setHp(targetHp);
		System.out.print("Doing defaultattack!");
	}
	
	@Override
	public String toString() {
		return "Defaultattack";
	}

}
