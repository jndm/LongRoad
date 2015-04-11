package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.Enemy;
import com.mygdx.game.elements.Mage;
import com.mygdx.game.elements.Rogue;
import com.mygdx.game.elements.Warrior;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Button;
import com.mygdx.game.helpers.ButtonAction;
import com.mygdx.game.elements.Character;

public class Battle extends GameState {
	
	private final int BUTTON_ROWS = 2;
	private final int BUTTON_COLS = 2;
	private final int ATTACKB_ROWS = 6;
	private final int ATTACKB_COLS = 1;
	
	private Texture bg;
	private Texture window;
	private TextureRegion[][] buttonsRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	private TextureRegion[][] clickedButtonRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	private TextureRegion[][] attackButtonRegion = new TextureRegion[ATTACKB_ROWS][ATTACKB_COLS];
	private TextureRegion[][] clickedAttackButtonRegion = new TextureRegion[ATTACKB_ROWS][ATTACKB_COLS];
	private Array<Button> mainButtons;
	private Array<Button> attackButtons;
	
	private Warrior warrior;
	private Mage mage;
	private Rogue rogue;
	private Array<Enemy> enemies;
	
	private final String BACKGROUND_IMG = "background/battle/battlebg.png";
	private final String ATTACKBUTTON_IMG = "background/battle/attackButtons.png";
	private final String CLICKEDATTACKBUTTON_IMG = "background/battle/attackButtonsClicked.png";
	private final String WARRIOR_IMG = "characters/warrior.png";
	private final String MAGE_IMG = "characters/mage.png";
	private final String ROGUE_IMG = "characters/rogue.png";
	private final String SKELETON_IMG = "characters/skeleton.png";
	private final String BUTTONS_IMG = "background/battle/buttons.png";
	private final String CLICKEDBUTTONS_IMG = "background/battle/clickedButtons.png";
		
	private BitmapFont font = new BitmapFont();
	
	public Battle(GameStateManager gsm) {
		super(gsm);
	}

	public Battle(GameStateManager gsm, Array<Enemy> enemies, Array<Character> chars) {
		super(gsm);	
		this.enemies = enemies;
		
		assets.load(BACKGROUND_IMG, Texture.class);
		assets.load(BUTTONS_IMG, Texture.class);
		assets.load(CLICKEDBUTTONS_IMG, Texture.class);
		assets.load(ATTACKBUTTON_IMG, Texture.class);
		assets.load(CLICKEDATTACKBUTTON_IMG, Texture.class);
		assets.finishLoading();
		
		bg = assets.get(BACKGROUND_IMG);
		
		initButtons();
		
		mage = (Mage) chars.get(0);
		mage.setX(20);
		mage.setY(300);
		warrior = (Warrior) chars.get(1);
		warrior.setX(100);
		warrior.setY(250);
		rogue = (Rogue) chars.get(2);
		rogue.setX(20);
		rogue.setY(228);
	}

	@Override
	public void handleInput() {
		Button clickedMainButton = null;
		//Check if one of the main buttons is already clicked
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				clickedMainButton = b;
			}
		}
		
		//for hovering effect
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ATTACK) {
			for(Button b2 : attackButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(Gdx.input.justTouched()) {
			// check if player clicked one of the sub-buttons and act
			if(clickedMainButton != null){
				switch( clickedMainButton.getAction()) {
					case ATTACK:
						for(Button b2 : attackButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								switch ( b2.getAction() ) {
								case DEFAULTATTACK:
									System.out.println("ATTACKING!");
									b2.setClicked(false);
									break;
								case SPECIALATTACK:
									break;
								default:
									break;
								}
							}
						}
						break;
					case CAST:		
						break;
					case ITEM:	
						break;
					case RUN:
						break;
				default:
					break;
				}
				clickedMainButton.setClicked(false);
			}
			//If none of the main buttons are clicked, check if player hit main button
			else {
				for(Button b : mainButtons) {
					if(b.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
						if(b.getAction() == ButtonAction.RUN) {
							/* TODO */
							System.out.println("RUNNING AWAY!");
							gsm.popState();
						}
					}
				}
			} 
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		mage.move(dt);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(hudCam.combined);

		sb.begin();
			font.draw(sb, "FPS: "+Gdx.graphics.getFramesPerSecond(), 730, 470);
			sb.draw(bg, 0, Game.HEIGHT - bg.getHeight());
		sb.end();
		
		//Characters
		mage.render(sb);
		warrior.render(sb);
		rogue.render(sb);
		
		for(Button b : mainButtons) {
			b.render(sb);
		}
		
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				switch( b.getAction() ) {
					case ATTACK:
						for(Button b2 : attackButtons) {
							b2.render(sb);
						}
						break;
					case CAST:
						
						break;
					case ITEM:
						
						break;
					case RUN:
						
						break;
				}
			}
		}
	}
	
	@Override
	public void dispose() {
		assets.unload(BACKGROUND_IMG);
		assets.unload(BUTTONS_IMG);
		assets.unload(CLICKEDBUTTONS_IMG);
		assets.unload(ATTACKBUTTON_IMG);
		assets.unload(CLICKEDATTACKBUTTON_IMG);
		font.dispose();
	}
	
	private void initButtons() {
		
		mainButtons = new Array<Button>();
		attackButtons = new Array<Button>();
		buttonsRegion = TextureRegion.split((Texture)assets.get(BUTTONS_IMG), 400, 104);
		clickedButtonRegion = TextureRegion.split((Texture)assets.get(CLICKEDBUTTONS_IMG), 400, 104);
		
		//Creating button objects, k is for keeping number of buttons combined updated for setting action values
		int k=0;
		for(int i=0; i<BUTTON_ROWS; i++) {
			for(int j=0; j<BUTTON_COLS; j++) {
				mainButtons.add(new Button(j * buttonsRegion[i][j].getRegionWidth(), 
						buttonsRegion[i][j].getRegionHeight() - i * buttonsRegion[i][j].getRegionHeight(), 
						buttonsRegion[i][j].getRegionWidth(), 
						buttonsRegion[i][j].getRegionHeight(),
						ButtonAction.values()[k].toString(),
						ButtonAction.values()[k]));
				TextureRegion[] tmp = new TextureRegion[2];
				tmp[0] = buttonsRegion[i][j];
				tmp[1] = clickedButtonRegion[i][j];
				mainButtons.get(k).setTextureRegion(tmp);
				k++;
			}
		}
		
		attackButtonRegion = TextureRegion.split((Texture)assets.get(ATTACKBUTTON_IMG), 150, 50);
		clickedAttackButtonRegion = TextureRegion.split((Texture)assets.get(CLICKEDATTACKBUTTON_IMG), 150, 50);
		int n=0;
		for(int i=0; i<ATTACKB_ROWS; i++) {
			attackButtons.add(new Button(mainButtons.get(0).getWidth()/2, 
					attackButtonRegion[i][0].getRegionHeight() - i * attackButtonRegion[i][0].getRegionHeight() + mainButtons.get(0).getY() + mainButtons.get(0).getWidth()/3, 
					attackButtonRegion[i][0].getRegionWidth(), 
					attackButtonRegion[i][0].getRegionHeight(), 
					ButtonAction.values()[k].toString(),
					ButtonAction.values()[k]));
			TextureRegion[] tmp = new TextureRegion[2];
			tmp[0] = attackButtonRegion[i][0];
			tmp[1] = clickedAttackButtonRegion[i][0];
			attackButtons.get(n).setTextureRegion(tmp);
			k++;
			n++;
			
			//TODO: ADD MORE SKILLS, 
			//keep k under 5 for now so this won't crash
			if(k > 5){
				k=5;
			}
		}
	}
}
