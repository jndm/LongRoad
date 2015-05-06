package com.mygdx.game.elements.skills.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class DefaultAttack extends Skill{

	@Override
	public void act(Character actor, Character target) {
		this.dmg = actor.getStrength();
		float targetHp = target.getHp() - this.dmg;
		target.setHp(targetHp);
		System.out.println("Doing defaultattack on "+target.getClass().getSimpleName());
		startAnimation(target);
	}
	
	@Override
	public String toString() {
		return "Defaultattack";
	}

}
