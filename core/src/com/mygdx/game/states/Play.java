package com.mygdx.game.states;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.Enemy;
import com.mygdx.game.elements.Mage;
import com.mygdx.game.elements.Rogue;
import com.mygdx.game.elements.Skeleton;
import com.mygdx.game.elements.Warrior;
import com.mygdx.game.handlers.GameStateManager;

public class Play extends GameState {
	
	private BitmapFont font = new BitmapFont();
	private AssetManager assetManager;
	private Texture bg;
	private Texture window;
	
	private Warrior warrior;
	private Mage mage;
	private Rogue rogue;
	private Array<Enemy> enemies;
	private Array<Array<Enemy>> enemywaves;
	
	private long startTime;
	private int ENEMYWAWES = 50;
	private final int ENEMYSPAWNRANGE = Game.WORLD_WIDTH / ENEMYWAWES;
	private final float ENEMYHPMULTIPLIER = 0.15f;
	private int enemiesDrawn = 0;
	
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
		
		warrior = new Warrior(100, 80);
		mage = new Mage(20, 150);
		rogue = new Rogue(20, 20);
			
		enemywaves = new Array<Array<Enemy>>();
		
		//Raffling enemywave sizes NEEDS REFACTORING
		for(int i=0; i < ENEMYWAWES; i++) {
			Skeleton s;
			enemies = new Array<Enemy>();
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
		
		enemies.clear();
		
		warrior.setTexture((Texture)assets.get(WARRIOR_IMG));
		mage.setTexture((Texture)assets.get(MAGE_IMG));
		rogue.setTexture((Texture)assets.get(ROGUE_IMG));
		
		startTime = System.currentTimeMillis();
		
	}
	
	public void handleInput() {}
	
	public void update(float dt) {
		warrior.move(dt);
		System.out.println(warrior.getX());
		mage.move(dt);
		rogue.move(dt);
		
		if(enemywaves.first().get(0).getX() - warrior.getX() < 200) {
			gsm.pushBattleState(GameStateManager.BATTLE, enemywaves.first());
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
