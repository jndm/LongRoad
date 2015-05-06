package com.mygdx.game.elements.skills.spells;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class DefaultSpell extends Skill{

	@Override
	public void act(Character actor, Character target) {
		System.out.print("Doing defaultspell!");
		startAnimation(target);
	}

	@Override
	public String toString() {
		return "Defaultspell";
	}
}
