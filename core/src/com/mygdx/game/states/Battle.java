package com.mygdx.game.states;

import java.sql.Time;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.Character;
import com.mygdx.game.elements.Enemy;
import com.mygdx.game.elements.Mage;
import com.mygdx.game.elements.Rogue;
import com.mygdx.game.elements.Warrior;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Button;
import com.mygdx.game.helpers.ButtonAction;

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
	private Array<Button> warriorAttackButtons;
	private Array<Button> mageAttackButtons;
	private Array<Button> rogueAttackButtons;
	
	private Warrior warrior;
	private Mage mage;
	private Rogue rogue;
	private Vector2 oldMagePosition, oldWarriorPosition, oldRoguePosition;
	private Array<Character> enemies;
	private Array<Character> turnQueue;
	private final float WAIT_TIME = 1f;
	private float timeElapsed = 0;
	
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

	public Battle(GameStateManager gsm, Array<Character> enemies, Array<Character> chars) {
		super(gsm);	
		this.enemies = enemies;
		turnQueue = new Array<Character>();
		
		assets.load(BACKGROUND_IMG, Texture.class);
		assets.load(BUTTONS_IMG, Texture.class);
		assets.load(CLICKEDBUTTONS_IMG, Texture.class);
		assets.load(ATTACKBUTTON_IMG, Texture.class);
		assets.load(CLICKEDATTACKBUTTON_IMG, Texture.class);
		assets.finishLoading();
		
		bg = assets.get(BACKGROUND_IMG);
		
		initButtons();
		
		mage = (Mage) chars.get(0);
		oldMagePosition = new Vector2(mage.getX(), mage.getY());
		mage.setXY(20, 300);
		warrior = (Warrior) chars.get(1);
		oldWarriorPosition = new Vector2(warrior.getX(), warrior.getY());
		warrior.setXY(100, 250);
		rogue = (Rogue) chars.get(2);
		oldRoguePosition = new Vector2(rogue.getX(), rogue.getY());
		rogue.setXY(20, 228);
		
		for(Character c : enemies) {
			c.setY(c.getY() + 190);
		}
	}

	public void handleWarriorInput() {
		Button clickedMainButton = null;
		//Check if one of the main buttons is already clicked
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				clickedMainButton = b;
			}
		}
		
		//for hovering effect
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ATTACK) {
			for(Button b2 : warriorAttackButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(Gdx.input.justTouched()) {
			// check if player clicked one of the sub-buttons and act
			if(clickedMainButton != null){
				switch( clickedMainButton.getAction()) {
					case ATTACK:
						for(Button b2 : warriorAttackButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								switch ( b2.getAction() ) {
								case DEFAULTATTACK:
									System.out.println("ATTACKING!");
									turnQueue.removeIndex(0);
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
							resetCharacterPosition();
							gsm.popState();
						}
					}
				}
			} 
		}
	}
	
	private void handleRogueInput() {
		Button clickedMainButton = null;
		//Check if one of the main buttons is already clicked
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				clickedMainButton = b;
			}
		}
		
		//for hovering effect
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ATTACK) {
			for(Button b2 : rogueAttackButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(Gdx.input.justTouched()) {
			// check if player clicked one of the sub-buttons and act
			if(clickedMainButton != null){
				switch( clickedMainButton.getAction()) {
					case ATTACK:
						for(Button b2 : rogueAttackButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								switch ( b2.getAction() ) {
								case DEFAULTROGUEATTACK:
									System.out.println("ATTACKING!");
									turnQueue.removeIndex(0);
									b2.setClicked(false);
									break;
								case SPECIALROGUEATTACK:
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
							resetCharacterPosition();
							gsm.popState();
						}
					}
				}
			} 
		}
		
	}

	private void handleMageInput() {
		Button clickedMainButton = null;
		//Check if one of the main buttons is already clicked
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				clickedMainButton = b;
			}
		}
		
		//for hovering effect
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ATTACK) {
			for(Button b2 : mageAttackButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(Gdx.input.justTouched()) {
			// check if player clicked one of the sub-buttons and act
			if(clickedMainButton != null){
				switch( clickedMainButton.getAction()) {
					case ATTACK:
						for(Button b2 : mageAttackButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								switch ( b2.getAction() ) {
								case DEFAULTMAGEATTACK:
									System.out.println("ATTACKING!");
									turnQueue.removeIndex(0);
									b2.setClicked(false);
									break;
								case SPECIALMAGEATTACK:
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
							resetCharacterPosition();
							gsm.popState();
						}
					}
				}
			} 
		}	
	}

	@Override
	public void update(float dt) {
		if(turnQueue.size == 0) {
			countTurn(dt);
		} else {
			Character c = turnQueue.first();
			if(c instanceof Warrior) {
				handleWarriorInput();
				//System.out.println("Warrior's turn");
			}else if (c instanceof Mage) {
				handleMageInput();
			}else if (c instanceof Rogue) {
				handleRogueInput();
			}else {
				
			}
		}
	}


	private void countTurn(float dt) {
		timeElapsed += dt;
		if(WAIT_TIME <= timeElapsed) {
			warrior.addCharge();
			if(warrior.isCharged()) {
				turnQueue.add(warrior);
				System.out.println("Warrior charged");
			}
			mage.addCharge();
			if(mage.isCharged()) {
				turnQueue.add(mage);
				System.out.println("Mage charged");
			}
			rogue.addCharge();
			if(rogue.isCharged()) {
				turnQueue.add(rogue);
				System.out.println("Rogue charged");
			}
			for(Character c : enemies) {
				c.addCharge();
				if(c.isCharged()) {
					//turnQueue.add(c);
					System.out.println("Skeleton charged");
				}
			}
			timeElapsed -= WAIT_TIME;
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(hudCam.combined);

		sb.begin();
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
						if(turnQueue.first() instanceof Warrior) {
							for(Button b2 : warriorAttackButtons) {
								b2.render(sb);
							}
						} else if(turnQueue.first() instanceof Mage) {
							for(Button b2 : mageAttackButtons) {
								b2.render(sb);
							}
						} else if(turnQueue.first() instanceof Rogue) {
							for(Button b2 : rogueAttackButtons) {
								b2.render(sb);
							}
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
		
		for(Character c : enemies) {
			c.render(sb);
		}
		
		sb.begin();
			font.draw(sb, "FPS: "+Gdx.graphics.getFramesPerSecond(), 730, 470);
		sb.end();
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
		//Creating fonts for buttons
		Texture fonttexture = new Texture(Gdx.files.internal("fonts/mainButton.png"));
		fonttexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		BitmapFont mainfont = new BitmapFont(Gdx.files.internal("fonts/mainButton.fnt"), new TextureRegion(fonttexture), false);
		
		fonttexture = new Texture(Gdx.files.internal("fonts/subButton.png"));
		fonttexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		BitmapFont subfont = new BitmapFont(Gdx.files.internal("fonts/subButton.fnt"), new TextureRegion(fonttexture), false);
		
		mainButtons = new Array<Button>();
		warriorAttackButtons = new Array<Button>();
		mageAttackButtons = new Array<Button>();
		rogueAttackButtons = new Array<Button>();
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
						ButtonAction.values()[k],
						mainfont));
				TextureRegion[] tmp = new TextureRegion[2];
				tmp[0] = buttonsRegion[i][j];
				tmp[1] = clickedButtonRegion[i][j];
				mainButtons.get(k).setTextureRegion(tmp);
				k++;
			}
		}
		
		attackButtonRegion = TextureRegion.split((Texture)assets.get(ATTACKBUTTON_IMG), 150, 50);
		clickedAttackButtonRegion = TextureRegion.split((Texture)assets.get(CLICKEDATTACKBUTTON_IMG), 150, 50);
		
		// WARRIOR SUBATTACK-BUTTONS
		int n=0;
		for(int i=0; i<ATTACKB_ROWS; i++) {
			warriorAttackButtons.add(new Button(mainButtons.get(0).getWidth()/2, 
					attackButtonRegion[i][0].getRegionHeight() - i * attackButtonRegion[i][0].getRegionHeight() + mainButtons.get(0).getY() + mainButtons.get(0).getWidth()/3, 
					attackButtonRegion[i][0].getRegionWidth(), 
					attackButtonRegion[i][0].getRegionHeight(), 
					ButtonAction.values()[k].toString(),
					ButtonAction.values()[k],
					subfont));
			TextureRegion[] tmp = new TextureRegion[2];
			tmp[0] = attackButtonRegion[i][0];
			tmp[1] = clickedAttackButtonRegion[i][0];
			warriorAttackButtons.get(n).setTextureRegion(tmp);
			k++;
			n++;
			
			//TODO: ADD MORE SKILLS, 
			//keep k under 5 for now so this won't crash
			if(k > 5){
				k=5;
			}
		}
		
		k++; //just for not crashing / bugging
		// MAGE SUBATTACK-BUTTONS
		n=0;
		for(int i=0; i<ATTACKB_ROWS; i++) {
			mageAttackButtons.add(new Button(mainButtons.get(0).getWidth()/2, 
					attackButtonRegion[i][0].getRegionHeight() - i * attackButtonRegion[i][0].getRegionHeight() + mainButtons.get(0).getY() + mainButtons.get(0).getWidth()/3, 
					attackButtonRegion[i][0].getRegionWidth(), 
					attackButtonRegion[i][0].getRegionHeight(), 
					ButtonAction.values()[k].toString(),
					ButtonAction.values()[k],
					subfont));
			TextureRegion[] tmp = new TextureRegion[2];
			tmp[0] = attackButtonRegion[i][0];
			tmp[1] = clickedAttackButtonRegion[i][0];
			mageAttackButtons.get(n).setTextureRegion(tmp);
			k++;
			n++;
			
			//TODO: ADD MORE SKILLS, 
			//keep k under 7 for now so this won't crash
			if(k > 7){
				k=7;
			}
		}

		k++; //just for not crashing / bugging
		// ROGUE SUBATTACK-BUTTONS
		n=0;
		for(int i=0; i<ATTACKB_ROWS; i++) {
			rogueAttackButtons.add(new Button(mainButtons.get(0).getWidth()/2, 
					attackButtonRegion[i][0].getRegionHeight() - i * attackButtonRegion[i][0].getRegionHeight() + mainButtons.get(0).getY() + mainButtons.get(0).getWidth()/3, 
					attackButtonRegion[i][0].getRegionWidth(), 
					attackButtonRegion[i][0].getRegionHeight(), 
					ButtonAction.values()[k].toString(),
					ButtonAction.values()[k],
					subfont));
			TextureRegion[] tmp = new TextureRegion[2];
			tmp[0] = attackButtonRegion[i][0];
			tmp[1] = clickedAttackButtonRegion[i][0];
			rogueAttackButtons.get(n).setTextureRegion(tmp);
			k++;
			n++;
			
			//TODO: ADD MORE SKILLS, 
			//keep k under 9 for now so this won't crash
			if(k > 9){
				k=9;
			}
		}
	}
	
	private void resetCharacterPosition() {
		mage.setXY(oldMagePosition.x, oldMagePosition.y);
		warrior.setXY(oldWarriorPosition.x, oldWarriorPosition.y);
		rogue.setXY(oldRoguePosition.x, oldRoguePosition.y);	
	}
}
