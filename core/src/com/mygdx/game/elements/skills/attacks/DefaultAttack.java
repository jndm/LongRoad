package com.mygdx.game.elements.skills.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class DefaultAttack implements Skill{

	@Override
	public void act(Character actor, Character target) {
		float damage = actor.getStrength();
		float targetHp = target.getHp() - damage;
		target.setHp(targetHp);
		System.out.println("Doing defaultattack on "+target.getClass().getSimpleName());
	}
	
	@Override
	public String toString() {
		return "Defaultattack";
	}

	@Override
	public void render(SpriteBatch sb, Character target) {
		// TODO Auto-generated method stub
		
	}

}
