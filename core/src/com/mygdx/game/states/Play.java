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

	private Party party;
	private Level level;
	
	private final int MAGE = 0;
	private final int WARRIOR = 1;
	private final int ROGUE = 2;
	
	private long startTime = 0;
	
	public Play(GameStateManager gsm) {
		super(gsm);

		//Create party
		party = new Party(game);
		
		//Create level
		level = new Level(game);
		
		startTime = System.currentTimeMillis();
	}

	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			gsm.pushMainMenuState(GameStateManager.MENU, party);
		}
	}
	
	public void update(float dt) {
		handleInput();
		for(Character c : party.getCharacters()){
			c.move(dt);
		}	
	
		if(level.getEnemywaves().first().get(0).getX() - party.getCharacters().get(WARRIOR).getX() < 200) {
			//gsm.pushMainMenuState(GameStateManager.MAINMENU, chars, items);
			for(Character c : party.getCharacters()) {
				c.setMoving(false);
			}
			gsm.pushBattleState(GameStateManager.BATTLE, level.getEnemywaves().first(), party);
			level.removeEnemyWave();
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
		viewport.apply();
		cam.position.x = party.getCharacters().get(WARRIOR).getX() + Game.WIDTH / 3;
		cam.update();	
		
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
