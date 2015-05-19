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
import com.mygdx.game.elements.characters.Party;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Level;

public class Play extends GameState {
	
	private BitmapFont font = new BitmapFont();
	
	private Level level;
	private String xpEarned = null;
	
	private final int MAGE = 0;
	private final int WARRIOR = 1;
	private final int ROGUE = 2;
	
	private long startTime = 0;
	private float timeElapsed = 0;
	
	public Play(GameStateManager gsm) {
		super(gsm);
		
		//Create level
		level = new Level(game);
		
		startTime = System.currentTimeMillis();
	}

	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			gsm.pushMenuState(GameStateManager.MENU);
		}
	}
	
	public void update(float dt) {
		timeElapsed += dt;
		
		if(party.isBattleWon()) {
			timeElapsed = 0;
			xpEarned = "+9999";
			party.setBattleWon(false);
		}
		
		handleInput();
		for(Character c : party.getCharacters()){
			c.move(dt);
		}	
	
		if(level.getEnemywaves().first().get(0).getX() - party.getCharacters().get(WARRIOR).getX() < 200) {
			for(Character c : party.getCharacters()) {
				c.setMoving(false);
			}
			gsm.pushBattleState(GameStateManager.BATTLE, level.getEnemywaves().first());
			level.removeEnemyWave();
		}
		
		if(timeElapsed > 2f) {	//If exp text have been on long enough delete it 
			xpEarned = null;
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
		
		cam.position.x = party.getCharacters().get(WARRIOR).getX() + Game.VIRTUAL_WIDTH / 3;
		cam.update();	
		viewport.apply();
		
		//BG
		sb.setProjectionMatrix(cam.combined);
		level.render(sb);
		
		//Characters
		for(Character c : party.getCharacters()){
			c.render(sb);
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
		party.dispose();
		level.dispose();
	}

	@Override
	public void resize(int w, int h) {
		viewport.update(w, h, true);
		viewport2.update(w, h, false);
	}	
}
