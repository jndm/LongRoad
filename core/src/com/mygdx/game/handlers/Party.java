package com.mygdx.game.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Mage;
import com.mygdx.game.elements.characters.Rogue;
import com.mygdx.game.elements.characters.Warrior;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.items.Potion;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.skills.attacks.DefaultAttack;
import com.mygdx.game.elements.skills.attacks.SpecialAttack;
import com.mygdx.game.elements.skills.spells.DefaultSpell;
import com.mygdx.game.elements.skills.spells.SpecialSpell;
import com.mygdx.game.helpers.Constants;
import com.mygdx.game.helpers.Utils;

public class Party {
	
	private AssetManager assets;
	private TextureAtlas atlas;
	
	private int level = 1;
	private float xp = 0;
	private float xpToLevelUp = 100;
	private Array<Character> characters;
	public static Array<Item> items;
	
	public Party(Game game) {
		assets = game.getAssetManger();
		assets.load(Constants.CHARATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(Constants.CHARATLAS);
		
		createPlayerCharacters();
		createAnimations();
		createSkills();
		createItems();
	}
	
	private void createPlayerCharacters() {
		characters = new Array<Character>();
		characters.add(new Mage("MAGE", 20, 150, 60, 120, 20, 4, 10, 12));
		characters.add(new Warrior("WARRIOR", 100, 60, 120, 50, 30, 12, 8, 4));
		characters.add(new Rogue("ROGUE", 20, 20, 60, 70, 20, 8, 12, 5));
		
		characters.get(Constants.WARRIOR).setTextureRegion(atlas.findRegion(Constants.WARRIOR_IMG));
		characters.get(Constants.MAGE).setTextureRegion(atlas.findRegion(Constants.MAGE_IMG));
		characters.get(Constants.ROGUE).setTextureRegion(atlas.findRegion(Constants.ROGUE_IMG));
		
		characters.get(Constants.WARRIOR).setBattleposition(new Vector2(100, 250));
		characters.get(Constants.MAGE).setBattleposition(new Vector2(20, 300));
		characters.get(Constants.ROGUE).setBattleposition(new Vector2(20, 228));
	
	}
	
	private void createAnimations() {
		characters.get(Constants.WARRIOR).setAttackAnimation(Utils.createAnimation(atlas, Constants.WARRIOR_ATTACK_IMG, 6, 1/5f));
		characters.get(Constants.MAGE).setAttackAnimation(Utils.createAnimation(atlas, Constants.MAGE_ATTACK_IMG, 6, 1/5f));
		characters.get(Constants.ROGUE).setAttackAnimation(Utils.createAnimation(atlas, Constants.ROGUE_ATTACK_IMG, 6, 1/5f));
		
		characters.get(Constants.WARRIOR).setMovingAnimation(Utils.createAnimation(atlas, Constants.WARRIOR_WALKING_IMG, 2, 1/10f));
		characters.get(Constants.MAGE).setMovingAnimation(Utils.createAnimation(atlas, Constants.MAGE_WALKING_IMG, 2, 1/10f));
		characters.get(Constants.ROGUE).setMovingAnimation(Utils.createAnimation(atlas, Constants.ROGUE_WALKING_IMG, 2, 1/10f));
	}

	private void createItems() {
		items = new Array<Item>();
		Item potion = new Potion();
		potion.addCount(1);
		items.add(potion);
		
		Item potion2 = new Potion();
		potion2.addCount(1);
		items.add(potion2);

		Item potion3 = new Potion();
		potion3.addCount(3);
		items.add(potion3);

	}

	
	private void createSkills() {
		TextureRegion[] anim = new TextureRegion[3];
		anim[0] = new TextureRegion(new Texture(Gdx.files.internal("skills/attk1.png")));
		anim[1] = new TextureRegion(new Texture(Gdx.files.internal("skills/attk2.png")));
		anim[2] = new TextureRegion(new Texture(Gdx.files.internal("skills/attk3.png")));
		
		Skill defaultattack = new DefaultAttack();
		defaultattack.createAnimation(anim);
		
		Skill specialattack = new SpecialAttack();
		specialattack.createAnimation(anim);
		
		Skill defaultspell = new DefaultSpell();
		defaultspell.createAnimation(anim);
		
		Skill specialspell = new SpecialSpell();
		specialspell.createAnimation(anim);
		
		characters.get(Constants.WARRIOR).addAttackAbility(defaultattack);
		characters.get(Constants.WARRIOR).addAttackAbility(specialattack);
		characters.get(Constants.WARRIOR).addAttackAbility(specialattack);
		characters.get(Constants.WARRIOR).addAttackAbility(specialattack);
		characters.get(Constants.WARRIOR).addAttackAbility(specialattack);
		characters.get(Constants.WARRIOR).addAttackAbility(specialattack);
		characters.get(Constants.WARRIOR).addAttackAbility(specialattack);
		characters.get(Constants.WARRIOR).addAttackAbility(specialattack);
		
		characters.get(Constants.WARRIOR).addSpell(defaultspell);
		characters.get(Constants.WARRIOR).addSpell(specialspell);
		characters.get(Constants.WARRIOR).addSpell(specialspell);
		characters.get(Constants.WARRIOR).addSpell(specialspell);
		characters.get(Constants.WARRIOR).addSpell(specialspell);
		characters.get(Constants.WARRIOR).addSpell(specialspell);
		characters.get(Constants.WARRIOR).addSpell(specialspell);
		characters.get(Constants.WARRIOR).addSpell(specialspell);
		
		characters.get(Constants.MAGE).addAttackAbility(defaultattack);
		characters.get(Constants.MAGE).addAttackAbility(specialattack);
		characters.get(Constants.MAGE).addAttackAbility(specialattack);
		
		characters.get(Constants.MAGE).addSpell(defaultspell);
		characters.get(Constants.MAGE).addSpell(specialspell);
		characters.get(Constants.MAGE).addSpell(specialspell);
		
		characters.get(Constants.ROGUE).addAttackAbility(defaultattack);
		characters.get(Constants.ROGUE).addAttackAbility(specialattack);
		
		characters.get(Constants.ROGUE).addSpell(defaultspell);
		characters.get(Constants.ROGUE).addSpell(specialspell);
		
	}
	
	public void dispose() {
		assets.unload(Constants.WARRIOR_IMG);
		assets.unload(Constants.MAGE_IMG);
		assets.unload(Constants.ROGUE_IMG);
	}

	public Array<Character> getCharacters() {
		return characters;
	}

	public void resetCharacters() {
		characters.get(Constants.MAGE).setXY(20, 150);
		characters.get(Constants.MAGE).setHp(characters.get(Constants.MAGE).getMaxHp());
		characters.get(Constants.MAGE).setMana(characters.get(Constants.MAGE).getMaxMana());
		characters.get(Constants.MAGE).setAlive(true);
		
		characters.get(Constants.WARRIOR).setXY(100, 60);
		characters.get(Constants.WARRIOR).setHp(characters.get(Constants.WARRIOR).getMaxHp());
		characters.get(Constants.WARRIOR).setMana(characters.get(Constants.WARRIOR).getMaxMana());
		characters.get(Constants.WARRIOR).setAlive(true);
		
		characters.get(Constants.ROGUE).setXY(20, 20);
		characters.get(Constants.ROGUE).setHp(characters.get(Constants.ROGUE).getMaxHp());
		characters.get(Constants.ROGUE).setMana(characters.get(Constants.ROGUE).getMaxMana());
		characters.get(Constants.ROGUE).setAlive(true);
	}
} 
