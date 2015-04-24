package com.mygdx.game.elements.ai;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.skills.Skill;

public class SkeletonAi implements Ai{

	@Override
	public Skill raffleSkillToUse(Array<Skill> attacks, Array<Skill> spells) {
		return attacks.get((int)(Math.random() * attacks.size));
	}

	@Override
	public Character raffleTarget(Array<Character> playerCharacters) {		
		return playerCharacters.get((int)(Math.random() * playerCharacters.size));
	}

}
