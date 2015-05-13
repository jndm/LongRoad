package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.ai.Ai;
import com.mygdx.game.elements.ai.SkeletonAi;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Mage;
import com.mygdx.game.elements.characters.Rogue;
import com.mygdx.game.elements.characters.Skeleton;
import com.mygdx.game.elements.characters.Warrior;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.items.Potion;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.skills.attacks.DefaultAttack;
import com.mygdx.game.elements.skills.attacks.SpecialAttack;
import com.mygdx.game.elements.skills.spells.DefaultSpell;
import com.mygdx.game.elements.skills.spells.SpecialSpell;
import com.mygdx.game.handlers.GameStateManager;

public class Play extends GameState {
	
	private BitmapFont font = new BitmapFont();
	private AssetManager assetManager;
	private Texture bg;
	private Texture window;
	private TextureAtlas characterAtlas;

	private final int MAGE = 0;
	private final int WARRIOR = 1;
	private final int ROGUE = 2;
	private Array<Character> playerCharacters;
	private Array<Array<Character>> enemywaves;
	public static Array<Item> items;
	
	private long startTime = 0;
	
	private int ENEMYWAWES = 50;
	private final float ENEMYHPMULTIPLIER = 0.15f;
	
	private final String BACKGROUND_IMG = "background/background.png";
	private final String WINDOW_IMG = "background/window.png";
	private final String WARRIOR_IMG = "warrior";
	private final String WARRIOR_WALKING_IMG = "warriormoving";
	private final String MAGE_IMG = "mage";
	private final String MAGE_WALKING_IMG = "magemoving";
	private final String ROGUE_IMG = "rogue";
	private final String ROGUE_WALKING_IMG = "roguemoving";
	private final String SKELETON_IMG = "Skeleton";
	
	private final String CHARATLAS = "characters/charassets.pack";
	
	public Play(GameStateManager gsm) {
		super(gsm);
				
		assets.load(BACKGROUND_IMG, Texture.class);
		assets.load(WINDOW_IMG, Texture.class);
		assets.load(CHARATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		characterAtlas = assets.get(CHARATLAS);
		
		bg = assets.get(BACKGROUND_IMG);
		window = assets.get(WINDOW_IMG);
		
		createPlayerCharacters();
		createEnemies();
		createItems();
		createSkills();
		createAnimations();
		
		startTime = System.currentTimeMillis();
	}
	
	private void createAnimations() { //JUST FOR TESTING NEEDS REFACTORING
		
		playerCharacters.get(WARRIOR).createAttackAnimation(characterAtlas.findRegion(MAGE_IMG), characterAtlas.findRegion(WARRIOR_IMG), characterAtlas.findRegion(ROGUE_IMG));
		playerCharacters.get(MAGE).createAttackAnimation(characterAtlas.findRegion(MAGE_IMG), characterAtlas.findRegion(WARRIOR_IMG), characterAtlas.findRegion(ROGUE_IMG));		
		playerCharacters.get(ROGUE).createAttackAnimation(characterAtlas.findRegion(MAGE_IMG), characterAtlas.findRegion(WARRIOR_IMG), characterAtlas.findRegion(ROGUE_IMG));
		
		TextureRegion[] wtr = new TextureRegion[2];
		TextureRegion[] mtr = new TextureRegion[2];
		TextureRegion[] rtr = new TextureRegion[2];
		for(int i=0; i<2; i++) {
			wtr[i] = characterAtlas.findRegion(WARRIOR_WALKING_IMG+(i+1));
			mtr[i] = characterAtlas.findRegion(MAGE_WALKING_IMG+(i+1));
			rtr[i] = characterAtlas.findRegion(ROGUE_WALKING_IMG+(i+1));
		}
		playerCharacters.get(WARRIOR).createMovingAnimation(wtr);
		playerCharacters.get(MAGE).createMovingAnimation(mtr);
		playerCharacters.get(ROGUE).createMovingAnimation(rtr);
		
		for(Array<Character> a  : enemywaves){
			for(Character c : a){
				c.createAttackAnimation(characterAtlas.findRegion(MAGE_IMG), characterAtlas.findRegion(WARRIOR_IMG), characterAtlas.findRegion(ROGUE_IMG));
				TextureRegion[] tr = new TextureRegion[1];
				tr[0] = characterAtlas.findRegion(SKELETON_IMG);
				c.createMovingAnimation(tr);
			}
		}
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

	private void createPlayerCharacters() {
		playerCharacters = new Array<Character>();
		playerCharacters.add(new Mage("MAGE", 20, 150, 60, 120, 20, 4, 10, 12));
		playerCharacters.add(new Warrior("WARRIOR", 100, 60, 120, 50, 30, 12, 8, 4));
		playerCharacters.add(new Rogue("ROGUE", 20, 20, 60, 70, 20, 8, 12, 5));
		
		playerCharacters.get(WARRIOR).setTextureRegion(characterAtlas.findRegion(WARRIOR_IMG));
		playerCharacters.get(MAGE).setTextureRegion(characterAtlas.findRegion(MAGE_IMG));
		playerCharacters.get(ROGUE).setTextureRegion(characterAtlas.findRegion(ROGUE_IMG));
		
		playerCharacters.get(WARRIOR).setBattleposition(new Vector2(100, 250));
		playerCharacters.get(MAGE).setBattleposition(new Vector2(20, 300));
		playerCharacters.get(ROGUE).setBattleposition(new Vector2(20, 228));
	
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
		
		playerCharacters.get(WARRIOR).addAttackAbility(defaultattack);
		playerCharacters.get(WARRIOR).addAttackAbility(specialattack);
		playerCharacters.get(WARRIOR).addAttackAbility(specialattack);
		playerCharacters.get(WARRIOR).addAttackAbility(specialattack);
		playerCharacters.get(WARRIOR).addAttackAbility(specialattack);
		playerCharacters.get(WARRIOR).addAttackAbility(specialattack);
		playerCharacters.get(WARRIOR).addAttackAbility(specialattack);
		playerCharacters.get(WARRIOR).addAttackAbility(specialattack);
		
		playerCharacters.get(WARRIOR).addSpell(defaultspell);
		playerCharacters.get(WARRIOR).addSpell(specialspell);
		playerCharacters.get(WARRIOR).addSpell(specialspell);
		playerCharacters.get(WARRIOR).addSpell(specialspell);
		playerCharacters.get(WARRIOR).addSpell(specialspell);
		playerCharacters.get(WARRIOR).addSpell(specialspell);
		
		playerCharacters.get(MAGE).addAttackAbility(defaultattack);
		playerCharacters.get(MAGE).addAttackAbility(specialattack);
		playerCharacters.get(MAGE).addAttackAbility(specialattack);
		
		playerCharacters.get(MAGE).addSpell(defaultspell);
		playerCharacters.get(MAGE).addSpell(specialspell);
		playerCharacters.get(MAGE).addSpell(specialspell);
		
		playerCharacters.get(ROGUE).addAttackAbility(defaultattack);
		playerCharacters.get(ROGUE).addAttackAbility(specialattack);
		
		playerCharacters.get(ROGUE).addSpell(defaultspell);
		playerCharacters.get(ROGUE).addSpell(specialspell);
		
		for(Array<Character> a  : enemywaves){
			for(Character c : a){
				c.addAttackAbility(defaultattack);
				c.addAttackAbility(specialattack);
				c.addSpell(defaultspell);
				c.addSpell(specialspell);
			}
		}
	}

	private void createEnemies() {
		enemywaves = new Array<Array<Character>>();
		Ai skeletonAi = new SkeletonAi();
		//Raffling enemywave sizes
		for(int i=0; i < ENEMYWAWES; i++) {
			Skeleton s;
			Array<Character> enemies = new Array<Character>();
			if(i < 17) { 
				s = new Skeleton((int)((i+1) * Game.WIDTH * 0.825), (int)(Game.HEIGHT * 0.18), 10f, 120f, 20f, 4, 10, 12, skeletonAi);
				s.setTextureRegion(characterAtlas.findRegion(SKELETON_IMG));
				s.setBattleposition(new Vector2((int)(Game.WIDTH * 0.825), (int)(Game.HEIGHT * 0.63)));
				enemies.add(s);
			} 
			else if (i < 34) { 
				for(int j=0; j<2; j++) {
					s = new Skeleton((int)((i+1) * Game.WIDTH * (0.825+j*0.01)), (int)(Game.HEIGHT * (0.25-j*0.2)), 80f, 120f, 20f, 4, 10, 12, skeletonAi);
					s.setTextureRegion(characterAtlas.findRegion(SKELETON_IMG));
					s.setBattleposition(new Vector2((int)(Game.WIDTH * (0.825+j*0.01)), (int)(Game.HEIGHT * (0.67-j*0.2))));
					enemies.add(s);
				}
			} 
			else { 
				for(int j=0; j<3; j++){
					s = new Skeleton((int)((i+1) * Game.WIDTH * (0.825+j*0.01)), (int)(Game.HEIGHT * (0.32-j*0.15)), 80f, 120f, 20f, 4, 10, 12, skeletonAi);
					s.setTextureRegion(characterAtlas.findRegion(SKELETON_IMG));
					s.setBattleposition(new Vector2((int)(Game.WIDTH * (0.825+j*0.01)), (int)(Game.HEIGHT * (0.73-j*0.15))));
					enemies.add(s);
				}
			}	
			enemywaves.add(enemies);
		}	
	}

	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			gsm.pushMainMenuState(GameStateManager.MENU, playerCharacters, items);
		}
	}
	
	public void update(float dt) {
		handleInput();
		for(Character c : playerCharacters){
			c.move(dt);
		}	
	
		if(enemywaves.first().get(0).getX() - playerCharacters.get(WARRIOR).getX() < 200) {
			//gsm.pushMainMenuState(GameStateManager.MAINMENU, chars, items);
			for(Character c : playerCharacters) {
				c.setMoving(false);
			}
			gsm.pushBattleState(GameStateManager.BATTLE, enemywaves.first(), playerCharacters, items);
			enemywaves.removeIndex(0);
			ENEMYWAWES--;
		}
		/* For testing how long it takes player to reach end of the level
		if(WARRIOR.getX() > Game.WORLD_WIDTH) {
			System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime)/1000+"s" );
		}
		*/
	}
	
	public void render() {
		
		cam.position.x = playerCharacters.get(WARRIOR).getX() + Game.WIDTH / 3;
		cam.update();	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		
		//BG
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
			for(int i=0; i < 51; i++) {
				sb.draw(bg, -800 + i*800, 0);
			}
			
			for(int i=0; i<100; i++){
				sb.draw(window, 400 + i*400, 350);
			}
		sb.end();
		
		//Characters
		for(Character c : playerCharacters){
			c.render(sb);
		}
		
		//TODO: Have to check how to only draw when visible
		for (int i=0; i < ENEMYWAWES; i++) {
			for(int j=0; j < enemywaves.get(i).size; j++) {
				enemywaves.get(i).get(j).render(sb);
			}
		}
		
		//HUD
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
			font.draw(sb, "FPS: "+Gdx.graphics.getFramesPerSecond(), 730, 470);
		sb.end();
		
	}
	
	public void dispose() {
		assets.unload(BACKGROUND_IMG);
		assets.unload(WARRIOR_IMG);
		assets.unload(MAGE_IMG);
		assets.unload(ROGUE_IMG);
		assets.unload(SKELETON_IMG);
	}

	@Override
	public void resize(int w, int h) {
		// TODO Auto-generated method stub
		
	}	
}
