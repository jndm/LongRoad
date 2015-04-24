package com.mygdx.game.elements.ai;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public interface Ai {
	public abstract Skill raffleSkillToUse(Array<Skill> attacks, Array<Skill> spells);
	public abstract Character raffleTarget(Array<Character> playerCharacters);
}
