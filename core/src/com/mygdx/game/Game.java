package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.handlers.GameStateManager;

public class Game implements ApplicationListener {
	
	public static final String TITLE = "Longroad";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;
	public static final int WORLD_WIDTH = 40000;
	
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	private AssetManager assetManager;
	
	public void create() {
		
		assetManager = new AssetManager();
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, WIDTH, HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, WIDTH, HEIGHT);
		
		gsm = new GameStateManager(this);
		
	}
	
	public void render() {
		gsm.update(STEP);
		gsm.render();
	}
	
	public void dispose() {
		sb.dispose();
		assetManager.dispose();
	}
	
	public void resize(int w, int h) {
		gsm.resize(w, h);
	}
	
	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }
	public AssetManager getAssetManger() { return assetManager; }
	
	
	public void pause() {}
	public void resume() {}
	
}
