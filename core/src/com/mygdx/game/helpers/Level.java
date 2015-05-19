package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.ai.Ai;
import com.mygdx.game.elements.ai.SkeletonAi;
import com.mygdx.game.elements.characters.Skeleton;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.elements.skills.attacks.DefaultAttack;
import com.mygdx.game.elements.skills.attacks.SpecialAttack;
import com.mygdx.game.elements.skills.spells.DefaultSpell;
import com.mygdx.game.elements.skills.spells.SpecialSpell;
import com.mygdx.game.elements.characters.Character;

public class Level {

	private AssetManager assets;
	private Texture bg;
	private Texture window;
	private TextureAtlas enemyAtlas;
	
	private Array<Array<Character>> enemywaves;
	private int ENEMYWAWES = 50;
	private final float ENEMYHPMULTIPLIER = 0.15f;
	
	private final String BACKGROUND_IMG = "background/background.png";
	private final String WINDOW_IMG = "background/window.png";
	private final String SKELETON_IMG = "skeleton";
	private final String SKELETON_MOVING_IMG = "skeleton.moving";
	private final String SKELETON_ATTACK_IMG = "skeleton.attack";
	
	private final String ENEMYATLAS = "characters/enemy/enemyatlas.pack";
	
	public Level(Game game) {
		assets = game.getAssetManger();
		
		assets.load(BACKGROUND_IMG, Texture.class);
		assets.load(WINDOW_IMG, Texture.class);
		assets.load(ENEMYATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		//Extra stuff
		bg = assets.get(BACKGROUND_IMG);
		window = assets.get(WINDOW_IMG);
		
		//Create enemies
		enemyAtlas = assets.get(ENEMYATLAS);
		createEnemies();
	}
	
	private void createEnemies() {
		enemywaves = new Array<Array<Character>>();
		Ai skeletonAi = new SkeletonAi();
		//Raffling enemywave sizes
		for(int i=0; i < ENEMYWAWES; i++) {
			Skeleton s;
			Array<Character> enemies = new Array<Character>();
			if(i < 17) { 
				s = new Skeleton((int)((i+1) * Game.VIRTUAL_WIDTH * 0.825), (int)(Game.VIRTUAL_HEIGHT * 0.18), 10f, 120f, 20f, 4, 10, 12, skeletonAi);
				s.setTextureRegion(enemyAtlas.findRegion(SKELETON_IMG));
				s.setBattleposition(new Vector2((int)(Game.VIRTUAL_WIDTH * 0.825), (int)(Game.VIRTUAL_HEIGHT * 0.63)));
				enemies.add(s);
			} 
			else if (i < 34) { 
				for(int j=0; j<2; j++) {
					s = new Skeleton((int)((i+1) * Game.VIRTUAL_WIDTH * (0.825+j*0.01)), (int)(Game.VIRTUAL_HEIGHT * (0.25-j*0.2)), 80f, 120f, 20f, 4, 10, 12, skeletonAi);
					s.setTextureRegion(enemyAtlas.findRegion(SKELETON_IMG));
					s.setBattleposition(new Vector2((int)(Game.VIRTUAL_WIDTH * (0.825+j*0.01)), (int)(Game.VIRTUAL_HEIGHT * (0.67-j*0.2))));
					enemies.add(s);
				}
			} 
			else { 
				for(int j=0; j<3; j++){
					s = new Skeleton((int)((i+1) * Game.VIRTUAL_WIDTH * (0.825+j*0.01)), (int)(Game.VIRTUAL_HEIGHT * (0.32-j*0.15)), 80f, 120f, 20f, 4, 10, 12, skeletonAi);
					s.setTextureRegion(enemyAtlas.findRegion(SKELETON_IMG));
					s.setBattleposition(new Vector2((int)(Game.VIRTUAL_WIDTH * (0.825+j*0.01)), (int)(Game.VIRTUAL_HEIGHT * (0.73-j*0.15))));
					enemies.add(s);
				}
			}	
			enemywaves.add(enemies);
		}
		
		createEnemySkills();
		createEnemyAnimations();
	}
	
	private void createEnemyAnimations() {	
		Animation skelmoving = Utils.createAnimation(enemyAtlas, SKELETON_MOVING_IMG, 2, 1/10f);
		Animation skelattacking = Utils.createAnimation(enemyAtlas, SKELETON_ATTACK_IMG, 6, 1/5f);
		for(Array<Character> a  : enemywaves){
			for(Character c : a){
				c.setAttackAnimation(skelattacking);
				c.setMovingAnimation(skelmoving);
			}
		}
	}

	private void createEnemySkills() {
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
		
		for(Array<Character> a  : enemywaves){
			for(Character c : a){
				c.addAttackAbility(defaultattack);
				c.addAttackAbility(specialattack);
				c.addSpell(defaultspell);
				c.addSpell(specialspell);
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		for(int i=0; i < 51; i++) {
			sb.draw(bg, -Game.VIRTUAL_WIDTH + i*Game.VIRTUAL_WIDTH, 0);
		}
		
		for(int i=0; i<100; i++){
			sb.draw(window, Game.VIRTUAL_WIDTH/2 + i*Game.VIRTUAL_WIDTH/2, Game.VIRTUAL_HEIGHT * 0.73f);
		}
		sb.end();
		//TODO: Have to check how to only draw when visible
		for (int i=0; i < ENEMYWAWES; i++) {
			for(int j=0; j < enemywaves.get(i).size; j++) {
				enemywaves.get(i).get(j).render(sb);
			}
		}
	}
	
	public void dispose() {
		assets.unload(BACKGROUND_IMG);
		assets.unload(SKELETON_IMG);
	}

	public Array<Array<Character>> getEnemywaves() {
		return enemywaves;
	}

	public void removeEnemyWave() {
		enemywaves.removeIndex(0);
		ENEMYWAWES--;
	}	
	
}
