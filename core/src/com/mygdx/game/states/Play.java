package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.Game;
import com.mygdx.game.elements.Mage;
import com.mygdx.game.elements.Rogue;
import com.mygdx.game.elements.Warrior;
import com.mygdx.game.handlers.GameStateManager;

public class Play extends GameState {
	
	private BitmapFont font = new BitmapFont();
	private AssetManager assetManager;
	private Texture bg;
	
	private Warrior warrior;
	private Mage mage;
	private Rogue rogue;
	
	private final String BACKGROUND_IMG = "background/background.png";
	private final String WARRIOR_IMG = "characters/warrior.png";
	private final String MAGE_IMG = "characters/mage.png";
	private final String ROGUE_IMG = "characters/rogue.png";
	
	public Play(GameStateManager gsm) {
		super(gsm);
				
		assets.load(BACKGROUND_IMG, Texture.class);
		assets.load(WARRIOR_IMG, Texture.class);
		assets.load(MAGE_IMG, Texture.class);
		assets.load(ROGUE_IMG, Texture.class);
		assets.finishLoading();
		
		bg = assets.get(BACKGROUND_IMG);
		
		warrior = new Warrior(100, 80);
		mage = new Mage(20, 150);
		rogue = new Rogue(20, 20);
		
		warrior.setTexture((Texture)assets.get(WARRIOR_IMG));
		mage.setTexture((Texture)assets.get(MAGE_IMG));
		rogue.setTexture((Texture)assets.get(ROGUE_IMG));
		
	}
	
	public void handleInput() {}
	
	public void update(float dt) {
		warrior.move(dt);
		mage.move(dt);
		rogue.move(dt);
	}
	
	public void render() {
		
		cam.position.x = warrior.getX() + Game.WIDTH / 3;
		cam.update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
			for( int i=0; i<5; i++ ) {
				sb.draw(bg, -800 + i*800, 0);
			}
		sb.end();
		mage.render(sb);
		warrior.render(sb);
		rogue.render(sb);	
		
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
			font.draw(sb, "FPS: "+Gdx.graphics.getFramesPerSecond(), 730, 470);
		sb.end();
		
	}
	
	public void dispose() {}
	
}
