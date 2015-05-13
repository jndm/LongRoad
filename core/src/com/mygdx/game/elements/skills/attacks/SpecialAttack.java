package com.mygdx.game.elements.skills.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class SpecialAttack extends Skill{
	
	public SpecialAttack() {
		super();
		manareq = 100;
	}

	@Override
	public void act(Character actor, Character target) {
		actor.setMana(actor.getMana() - manareq);
		this.dmg = actor.getStrength() * 2;
		float targetHp = target.getHp() - this.dmg;
		target.setHp(targetHp);
		System.out.println("Doing specialtattack on "+target.getClass().getSimpleName());
		startAnimation(target);
	}
	
	@Override
	public String toString() {
		return "Specialattack";
	}

}
