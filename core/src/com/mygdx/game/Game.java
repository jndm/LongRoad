package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.Party;

public class Game implements ApplicationListener {
	
	public static final String TITLE = "Longroad";
	public static final int VIRTUAL_WIDTH = 800;
	public static final int VIRTUAL_HEIGHT = 480;
	public static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH / (float)VIRTUAL_HEIGHT;
	
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;
	private AssetManager assetManager;
	private Viewport viewport, viewport2;
	private Party party;
	
	public void create() {
		
		assetManager = new AssetManager();
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport = new FillViewport(VIRTUAL_WIDTH * ASPECT_RATIO, VIRTUAL_HEIGHT, cam);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		viewport2 = new FillViewport(VIRTUAL_WIDTH * ASPECT_RATIO, VIRTUAL_HEIGHT, hudCam);
		party = new Party(this);
		
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
	public Viewport getViewport() { return viewport; }
	public Viewport getViewport2() { return viewport2; }
	public Party getParty() { return party; }

	public void pause() {}
	public void resume() {}
	
}
