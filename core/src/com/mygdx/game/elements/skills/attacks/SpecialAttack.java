package com.mygdx.game.elements.skills.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class SpecialAttack implements Skill{

	@Override
	public void act(Character actor, Character target) {
		float damage = actor.getStrength() * 2;
		float targetHp = target.getHp() - damage;
		target.setHp(targetHp);
		System.out.println("Doing specialtattack on "+target.getClass().getSimpleName());
	}
	
	@Override
	public String toString() {
		return "Specialattack";
	}

	@Override
	public void render(SpriteBatch sb, Character target) {
		// TODO Auto-generated method stub
		
	}

}
