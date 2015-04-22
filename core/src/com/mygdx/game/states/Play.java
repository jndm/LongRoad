package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Mage;
import com.mygdx.game.elements.characters.Rogue;
import com.mygdx.game.elements.characters.Skeleton;
import com.mygdx.game.elements.characters.Warrior;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.items.Potion;
import com.mygdx.game.handlers.GameStateManager;

public class Play extends GameState {
	
	private BitmapFont font = new BitmapFont();
	private AssetManager assetManager;
	private Texture bg;
	private Texture window;
	
	private Warrior warrior;
	private Mage mage;
	private Rogue rogue;
	private Array<Array<Character>> enemywaves;
	private Array<Item> items;
	
	private long startTime = 0;
	
	private int ENEMYWAWES = 50;
	private final float ENEMYHPMULTIPLIER = 0.15f;
	
	private final String BACKGROUND_IMG = "background/background.png";
	private final String WINDOW_IMG = "background/window.png";
	private final String WARRIOR_IMG = "characters/warrior.png";
	private final String MAGE_IMG = "characters/mage.png";
	private final String ROGUE_IMG = "characters/rogue.png";
	private final String SKELETON_IMG = "characters/skeleton.png";
	
	public Play(GameStateManager gsm) {
		super(gsm);
				
		assets.load(BACKGROUND_IMG, Texture.class);
		assets.load(WARRIOR_IMG, Texture.class);
		assets.load(MAGE_IMG, Texture.class);
		assets.load(ROGUE_IMG, Texture.class);
		assets.load(SKELETON_IMG, Texture.class);
		assets.load(WINDOW_IMG, Texture.class);
		assets.finishLoading();
		
		bg = assets.get(BACKGROUND_IMG);
		window = assets.get(WINDOW_IMG);
		
		createPlayerCharacters();
		createEnemies();
		createItems();
		
		startTime = System.currentTimeMillis();
		
	}
	
	private void createItems() {
		items = new Array<Item>();
		Item potion = new Potion();
		potion.addCount(3);
		items.add(potion);
		
		Item potion2 = new Potion();
		potion2.addCount(3);
		items.add(potion2);
	}

	private void createPlayerCharacters() {
		warrior = new Warrior(100, 80, 120, 50, 30, 12, 8, 4);
		mage = new Mage(20, 150, 80, 120, 20, 4, 10, 12);
		rogue = new Rogue(20, 20, 100, 70, 20, 8, 12, 5);
		
		warrior.setTexture((Texture)assets.get(WARRIOR_IMG));
		mage.setTexture((Texture)assets.get(MAGE_IMG));
		rogue.setTexture((Texture)assets.get(ROGUE_IMG));
		
		//JUST FOR TESTING
		warrior.createAttackAnimation(new TextureRegion((Texture)assets.get(WARRIOR_IMG), 0, 0, warrior.getTexture().getWidth(), warrior.getTexture().getHeight()), 
				new TextureRegion((Texture)assets.get(MAGE_IMG), 0, 0, mage.getTexture().getWidth(), mage.getTexture().getHeight()), 
				new TextureRegion((Texture)assets.get(ROGUE_IMG), 0, 0, rogue.getTexture().getWidth(), rogue.getTexture().getHeight()));
		
		mage.createAttackAnimation(new TextureRegion((Texture)assets.get(MAGE_IMG), 0, 0, mage.getTexture().getWidth(), mage.getTexture().getHeight()), 
				new TextureRegion((Texture)assets.get(WARRIOR_IMG), 0, 0, mage.getTexture().getWidth(), mage.getTexture().getHeight()), 
				new TextureRegion((Texture)assets.get(ROGUE_IMG), 0, 0, rogue.getTexture().getWidth(), rogue.getTexture().getHeight()));	
		
		rogue.createAttackAnimation(new TextureRegion((Texture)assets.get(ROGUE_IMG), 0, 0, warrior.getTexture().getWidth(), warrior.getTexture().getHeight()), 
				new TextureRegion((Texture)assets.get(MAGE_IMG), 0, 0, mage.getTexture().getWidth(), mage.getTexture().getHeight()), 
				new TextureRegion((Texture)assets.get(WARRIOR_IMG), 0, 0, rogue.getTexture().getWidth(), rogue.getTexture().getHeight()));	
	}

	private void createEnemies() {
		enemywaves = new Array<Array<Character>>();
		
		//Raffling enemywave sizes NEEDS REFACTORING and more enemies
		for(int i=0; i < ENEMYWAWES; i++) {
			Skeleton s;
			Array<Character> enemies = new Array<Character>();
			if(i < 17) { 
				s = new Skeleton(700 + i * 700, 80);
				s.setTexture((Texture)assets.get(SKELETON_IMG));
				s.setHp(5 + i * ENEMYHPMULTIPLIER);
				enemies.add(s);
			} 
			else if (i < 34) { 
				s = new Skeleton(700 + i * 700, 150);
				s.setTexture((Texture)assets.get(SKELETON_IMG));
				s.setHp(5 + i * ENEMYHPMULTIPLIER);
				enemies.add(s);
				s = new Skeleton(700 + i * 700, 20);
				s.setTexture((Texture)assets.get(SKELETON_IMG));
				s.setHp(5 + i * ENEMYHPMULTIPLIER);
				enemies.add(s);
			} 
			else { 
				s = new Skeleton(700 + i * 700, 150);
				s.setTexture((Texture)assets.get(SKELETON_IMG));
				s.setHp(5 + i * ENEMYHPMULTIPLIER);
				enemies.add(s);
				s = new Skeleton(700 + i * 700 - 80, 80);
				s.setTexture((Texture)assets.get(SKELETON_IMG));
				s.setHp(5 + i * ENEMYHPMULTIPLIER);
				enemies.add(s);
				s = new Skeleton(700 + i * 700, 20);
				s.setTexture((Texture)assets.get(SKELETON_IMG));
				s.setHp(5 + i * ENEMYHPMULTIPLIER);
				enemies.add(s);
			}
			
			enemywaves.add(enemies);
		}	
	}

	public void handleInput() {}
	
	public void update(float dt) {
		warrior.move(dt);
		mage.move(dt);
		rogue.move(dt);
		
		if(enemywaves.first().get(0).getX() - warrior.getX() < 200) {
			Array<Character> chars = new Array<Character>();
			chars.add(mage);
			chars.add(warrior);
			chars.add(rogue);
			gsm.pushBattleState(GameStateManager.BATTLE, enemywaves.first(), chars, items);
			enemywaves.removeIndex(0);
			ENEMYWAWES--;
		}
		/* For testing how long it takes player to reach end of the level
		if(warrior.getX() > Game.WORLD_WIDTH) {
			System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime)/1000+"s" );
		}
		*/
	}
	
	public void render() {
		
		cam.position.x = warrior.getX() + Game.WIDTH / 3;
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
		mage.render(sb);
		warrior.render(sb);
		rogue.render(sb);
		
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
	
}
