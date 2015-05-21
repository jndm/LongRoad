package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.Level;
import com.mygdx.game.handlers.Party;
import com.mygdx.game.helpers.Constants;

public class Play extends GameState {
	
	private BitmapFont font = new BitmapFont();
	
	private Level level;
	private Battle battle;
	private String xpEarned = null;
	
	private long startTime = 0;
	private float timeElapsed = 0;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		//Create level
		level = new Level(game);
		
		party.resetCharacters();	// Have to reset the party position and hp etc incase this isn't the first time entering playstate
		
		startTime = System.currentTimeMillis();
	}

	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			gsm.pushMenuState(GameStateManager.MENU);
		}
	}
	
	public void update(float dt) {
		timeElapsed += dt;
		
		if(battle == null) {	//If battle is null move forward
			handleInput();
			for(Character c : party.getCharacters()){
				c.move(dt);
			}	
			//If close enough of the enemywave go to battle
			if(level.getEnemywaves().first().get(0).getX() - party.getCharacters().get(Constants.WARRIOR).getX() < 200) {
				for(Character c : party.getCharacters()) {
					c.setMoving(false);
				}
				battle = new Battle(gsm, level.getEnemywaves().first());
				gsm.pushBattleState(battle);
			}
		} else { //Handle returning from battle
			if(battle.isBattleWon()) {	
				xpEarned = "+9999xp";
				level.removeEnemyWave();
			} else {				
				gsm.popState();
			}
			battle = null;	//No need to call dispose since it was already called in gsm
		}
		/* For testing how long it takes player to reach end of the level
		if(WARRIOR.getX() > Game.WORLD_WIDTH) {
			System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime)/1000+"s" );
		}
		*/
	}
	
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.position.x = party.getCharacters().get(Constants.WARRIOR).getX() + Game.VIRTUAL_WIDTH / 3;
		cam.update();	
		viewport.apply();
		
		//BG
		sb.setProjectionMatrix(cam.combined);
		level.render(sb);
		
		//Characters
		for(Character c : party.getCharacters()) {
			if(c.isAlive()) {
				c.render(sb);
			}
		}
		
		//HUD
		sb.setProjectionMatrix(hudCam.combined);
		viewport2.apply();
		sb.begin();
			font.draw(sb, "FPS: "+Gdx.graphics.getFramesPerSecond(), 730, 470);
			if(xpEarned != null) {
				font.draw(sb, xpEarned+"xp", Game.VIRTUAL_WIDTH/2 - font.getBounds(xpEarned+"xp").width/2, Game.VIRTUAL_HEIGHT * 0.75f);
			}
		sb.end();
		
	}
	
	public void dispose() {
		level.dispose();
	}

	@Override
	public void resize(int w, int h) {
		viewport.update(w, h, true);
		viewport2.update(w, h, false);
	}	
}
