package com.mygdx.game.elements.characters;

public class Abilities {
	/*																	*/
	/* MAX SKILL PER ATTACK STYLE IS 8 ATM, NO MORE FITS IN THE SCREEN 	*/
	/*																	*/
	public enum WarriorAbilities {
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
	
	public enum MageAbilities {
		DEFAULTMATTACK,
		SPECIALMSKILL,
		
		//Cast
		DEFAULTMCAST,
		SPECIALMCAST,	
	}

	public enum RogueAbilities {
		DEFAULTRATTACK,
		SPECIALRSKILL,
		
		//Cast
		DEFAULTRCAST,
		SPECIALRCAST,
	}
}
