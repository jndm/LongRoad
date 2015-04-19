package com.mygdx.game.elements.characters;

public class Abilities {
	
	public interface Ability {
		
	}
	/*																	*/
	/* MAX SKILL PER ATTACK STYLE IS 8 ATM, NO MORE FITS IN THE SCREEN 	*/
	/*																	*/
	public enum WarriorAbilities implements Ability {
		//Attack
		DEFAULTWATTACK,
		SPECIALWSKILL,
		SPECIALWSKILL2,
		SPECIALWSKILL3,
		SPECIALWSKILL4,
		SPECIALWSKILL5,
		SPECIALWSKILL6,
		SPECIALWSKILL7,
		
		//Cast
		DEFAULTWCAST,
		SPECIALWCAST,
	}
	
	public enum MageAbilities implements Ability {
		//Attack
		DEFAULTMATTACK,
		SPECIALMSKILL,
		
		//Cast
		DEFAULTMCAST,
		SPECIALMCAST,	
	}

	public enum RogueAbilities implements Ability {
		//Attack
		DEFAULTRATTACK,
		SPECIALRSKILL,
		
		//Cast
		DEFAULTRCAST,
		SPECIALRCAST,
	}
}
