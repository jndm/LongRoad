package com.mygdx.game.elements.skills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.elements.characters.Character;

public interface Skill {
	public void act(Character actor, Character target);
	public void render(SpriteBatch sb, Character target);
}
