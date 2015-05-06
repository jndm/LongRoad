package com.mygdx.game.elements.skills.spells;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class SpecialSpell extends Skill{

	@Override
	public void act(Character actor, Character target) {
		System.out.print("Doing specialspell!");
		startAnimation(target);
	}
	
	@Override
	public String toString() {
		return "Specialspell";
	}

}
