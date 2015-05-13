package com.mygdx.game.helpers;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.characters.Character;

public class SkillTextButton extends TextButton {

	private Skill skill;
	public SkillTextButton(String text, Skin skin, String styleName, Skill s) {
		super(text, skin, styleName);
		this.skill = s;
	}
	
	public void setDisabledIfNotEnoughMana(Character c) {
		if(!skill.haveMana(c)) {
			this.setDisabled(true);
		}
	}

	public Skill getSkill() {
		return skill;
	}
}
