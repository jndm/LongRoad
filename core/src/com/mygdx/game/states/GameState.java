package com.mygdx.game.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Game;
import com.mygdx.game.handlers.GameStateManager;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected Game game;
	protected AssetManager assets;
	
	protected SpriteBatch sb;
	protected OrthographicCamera cam;
	protected OrthographicCamera hudCam;
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.game();
		sb = game.getSpriteBatch();
		cam = game.getCamera();
		hudCam = game.getHUDCamera();
		assets = game.getAssetManger();
	}
	
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();
	
}
