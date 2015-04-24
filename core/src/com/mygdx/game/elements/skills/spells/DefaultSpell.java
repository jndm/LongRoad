package com.mygdx.game.elements.skills.spells;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class DefaultSpell implements Skill{

	@Override
	public void act(Character actor, Character target) {
		System.out.print("Doing defaultspell!");
	}

	@Override
	public String toString() {
		return "Defaultspell";
	}

	@Override
	public void render(SpriteBatch sb, Character target) {
		// TODO Auto-generated method stub
		
	}
}
