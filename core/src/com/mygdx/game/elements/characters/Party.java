package com.mygdx.game.elements.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.items.Potion;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.skills.attacks.DefaultAttack;
import com.mygdx.game.elements.skills.attacks.SpecialAttack;
import com.mygdx.game.elements.skills.spells.DefaultSpell;
import com.mygdx.game.elements.skills.spells.SpecialSpell;
import com.mygdx.game.helpers.Utils;

public class Party {
	
	private AssetManager assets;
	private TextureAtlas atlas;
	
	private int level = 0;
	private float xp = 0;
	private float xpToLevelUp = 100;
	private Array<Character> characters;
	public static Array<Item> items;
	private boolean battleWon = false;
	
	private final int MAGE = 0;
	private final int WARRIOR = 1;
	private final int ROGUE = 2;
	
	private final String WARRIOR_IMG = "warrior";
	private final String WARRIOR_WALKING_IMG = "warrior.moving";
	private final String WARRIOR_ATTACK_IMG = "warrior.attack";
	private final String MAGE_IMG = "mage";
	private final String MAGE_WALKING_IMG = "mage.moving";
	private final String MAGE_ATTACK_IMG = "mage.attack";
	private final String ROGUE_IMG = "rogue";
	private final String ROGUE_WALKING_IMG = "rogue.moving";
	private final String ROGUE_ATTACK_IMG = "rogue.attack";
	
	private final String CHARATLAS = "characters/player/playercharacteratlas.pack";
	
	public Party(Game game) {
		assets = game.getAssetManger();
		assets.load(CHARATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(CHARATLAS);
		
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
		
		characters.get(WARRIOR).setTextureRegion(atlas.findRegion(WARRIOR_IMG));
		characters.get(MAGE).setTextureRegion(atlas.findRegion(MAGE_IMG));
		characters.get(ROGUE).setTextureRegion(atlas.findRegion(ROGUE_IMG));
		
		characters.get(WARRIOR).setBattleposition(new Vector2(100, 250));
		characters.get(MAGE).setBattleposition(new Vector2(20, 300));
		characters.get(ROGUE).setBattleposition(new Vector2(20, 228));
	
	}
	
	private void createAnimations() {
		characters.get(WARRIOR).setAttackAnimation(Utils.createAnimation(atlas, WARRIOR_ATTACK_IMG, 6, 1/5f));
		characters.get(MAGE).setAttackAnimation(Utils.createAnimation(atlas, MAGE_ATTACK_IMG, 6, 1/5f));
		characters.get(ROGUE).setAttackAnimation(Utils.createAnimation(atlas, ROGUE_ATTACK_IMG, 6, 1/5f));
		
		characters.get(WARRIOR).setMovingAnimation(Utils.createAnimation(atlas, WARRIOR_WALKING_IMG, 2, 1/10f));
		characters.get(MAGE).setMovingAnimation(Utils.createAnimation(atlas, MAGE_WALKING_IMG, 2, 1/10f));
		characters.get(ROGUE).setMovingAnimation(Utils.createAnimation(atlas, ROGUE_WALKING_IMG, 2, 1/10f));
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
		
		characters.get(WARRIOR).addAttackAbility(defaultattack);
		characters.get(WARRIOR).addAttackAbility(specialattack);
		characters.get(WARRIOR).addAttackAbility(specialattack);
		characters.get(WARRIOR).addAttackAbility(specialattack);
		characters.get(WARRIOR).addAttackAbility(specialattack);
		characters.get(WARRIOR).addAttackAbility(specialattack);
		characters.get(WARRIOR).addAttackAbility(specialattack);
		characters.get(WARRIOR).addAttackAbility(specialattack);
		
		characters.get(WARRIOR).addSpell(defaultspell);
		characters.get(WARRIOR).addSpell(specialspell);
		characters.get(WARRIOR).addSpell(specialspell);
		characters.get(WARRIOR).addSpell(specialspell);
		characters.get(WARRIOR).addSpell(specialspell);
		characters.get(WARRIOR).addSpell(specialspell);
		characters.get(WARRIOR).addSpell(specialspell);
		characters.get(WARRIOR).addSpell(specialspell);
		
		characters.get(MAGE).addAttackAbility(defaultattack);
		characters.get(MAGE).addAttackAbility(specialattack);
		characters.get(MAGE).addAttackAbility(specialattack);
		
		characters.get(MAGE).addSpell(defaultspell);
		characters.get(MAGE).addSpell(specialspell);
		characters.get(MAGE).addSpell(specialspell);
		
		characters.get(ROGUE).addAttackAbility(defaultattack);
		characters.get(ROGUE).addAttackAbility(specialattack);
		
		characters.get(ROGUE).addSpell(defaultspell);
		characters.get(ROGUE).addSpell(specialspell);
		
	}
	
	public void dispose() {
		assets.unload(WARRIOR_IMG);
		assets.unload(MAGE_IMG);
		assets.unload(ROGUE_IMG);
	}

	public Array<Character> getCharacters() {
		return characters;
	}

	public boolean isBattleWon() {
		return battleWon;
	}

	public void setBattleWon(boolean battleWon) {
		this.battleWon = battleWon;
	}
} 
