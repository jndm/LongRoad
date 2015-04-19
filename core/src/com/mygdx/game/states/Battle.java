package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Mage;
import com.mygdx.game.elements.characters.Rogue;
import com.mygdx.game.elements.characters.Warrior;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Button;
import com.mygdx.game.helpers.ButtonAction;

public class Battle extends GameState {
	
	private final int BUTTON_ROWS = 2;
	private final int BUTTON_COLS = 2;
	
	private Texture bg;
	private Sprite itembg;
	
	private Array<Sprite> healthBarbgs;
	private Array<Sprite> healthBarfills;

	private TextureRegion[][] buttonsRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	private TextureRegion[][] notClickablebuttonsRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	private TextureRegion[][] clickedButtonRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	
	private Array<Button> mainButtons;
	private Array<Button> warriorAttackButtons;
	private Array<Button> mageAttackButtons;
	private Array<Button> rogueAttackButtons;
	
	private Array<Button> warriorCastButtons;
	private Array<Button> mageCastButtons;
	private Array<Button> rogueCastButtons;
	
	private Array<Button> itemButtons;
	
	private Warrior warrior;
	private Mage mage;
	private Rogue rogue;
	private Vector2 oldMagePosition, oldWarriorPosition, oldRoguePosition;
	private Array<Character> enemies;
	private Array<Character> turnQueue;
	private Array<Item> items;
	private final float WAIT_TIME = 1f;
	private float timeElapsed = 0;
	
	private final String BACKGROUND_IMG 					= "background/battle/battlebg.png";
	
	private final String SUBBUTTONTOP_IMG 					= "background/battle/subButtonTop.png";
	private final String SUBBUTTONMID_IMG 					= "background/battle/subButtonMiddle.png";
	private final String SUBBUTTONBOTTOM_IMG 				= "background/battle/subButtonBottom.png";
	private final String SUBBUTTONTOPHOVER_IMG 				= "background/battle/subButtonTopHover.png";
	private final String SUBBUTTONMIDHOVER_IMG 				= "background/battle/subButtonMiddleHover.png";
	private final String SUBBUTTONBOTTOMHOVER_IMG		 	= "background/battle/subButtonBottomHover.png";
	private final String SUBBUTTONTOP_NOTCLICKABLE_IMG 		= "background/battle/subButtonTopNotClickable.png";
	private final String SUBBUTTONMID_NOTCLICKABLE_IMG 		= "background/battle/subButtonMiddleNotClickable.png";
	private final String SUBBUTTONBOTTOM_NOTCLICKABLE_IMG 	= "background/battle/subButtonBottomNotClickable.png";

	private final String BUTTONS_IMG 						= "background/battle/buttons.png";
	private final String CLICKED_BUTTONS_IMG 				= "background/battle/clickedButtons.png";
	private final String NOT_CLICKABLE_BUTTONS_IMG 			= "background/battle/notClickableButtons.png";
	
	private final String ITEMBG_IMG 						= "background/battle/itembg.png";
	
	private final String HEALTHBARBG_IMG 					= "background/battle/healthbarbg.png";
	private final String HEALTHBARFILL_IMG 					= "background/battle/healthbarfill.png";
		
	private BitmapFont font = new BitmapFont();
	
	public Battle(GameStateManager gsm) {
		super(gsm);
	}

	public Battle(GameStateManager gsm, Array<Character> enemies, Array<Character> chars, Array<Item> items) {
		super(gsm);	
		this.enemies = enemies;
		this.items = items;
		turnQueue = new Array<Character>();
		
		assets.load(BACKGROUND_IMG, Texture.class);
		assets.load(BUTTONS_IMG, Texture.class);
		assets.load(CLICKED_BUTTONS_IMG, Texture.class);
		assets.load(NOT_CLICKABLE_BUTTONS_IMG, Texture.class);
		assets.load(SUBBUTTONTOP_IMG, Texture.class);					
		assets.load(SUBBUTTONMID_IMG, Texture.class); 					
		assets.load(SUBBUTTONBOTTOM_IMG, Texture.class); 				
		assets.load(SUBBUTTONTOPHOVER_IMG, Texture.class); 				
		assets.load(SUBBUTTONMIDHOVER_IMG, Texture.class); 				
		assets.load(SUBBUTTONBOTTOMHOVER_IMG, Texture.class);		 
		assets.load(SUBBUTTONTOP_NOTCLICKABLE_IMG, Texture.class); 		
		assets.load(SUBBUTTONMID_NOTCLICKABLE_IMG, Texture.class); 		
		assets.load(SUBBUTTONBOTTOM_NOTCLICKABLE_IMG, Texture.class);
		assets.load(ITEMBG_IMG, Texture.class);
		assets.load(HEALTHBARBG_IMG, Texture.class);
		assets.load(HEALTHBARFILL_IMG, Texture.class);
		assets.finishLoading();
		
		bg = assets.get(BACKGROUND_IMG);
		healthBarbgs = new Array<Sprite>();
		healthBarfills = new Array<Sprite>();

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
			c.setX(700);
			c.setY(c.getY() + 190);
		}
		
		initButtons();
		
		healthBarbgs.add(new Sprite((Texture) assets.get(HEALTHBARBG_IMG)));
		healthBarbgs.add(new Sprite((Texture) assets.get(HEALTHBARBG_IMG)));
		healthBarbgs.add(new Sprite((Texture) assets.get(HEALTHBARBG_IMG)));
		healthBarbgs.get(0).setPosition(mage.getX(), 	mage.getY() - healthBarbgs.get(0).getRegionHeight() - 5);
		healthBarbgs.get(1).setPosition(warrior.getX(), warrior.getY()- healthBarbgs.get(1).getRegionHeight() - 5);
		healthBarbgs.get(2).setPosition(rogue.getX(), 	rogue.getY()- healthBarbgs.get(2).getRegionHeight() - 5);

		healthBarfills.add(new Sprite((Texture) assets.get(HEALTHBARFILL_IMG)));
		healthBarfills.add(new Sprite((Texture) assets.get(HEALTHBARFILL_IMG)));
		healthBarfills.add(new Sprite((Texture) assets.get(HEALTHBARFILL_IMG)));
		healthBarfills.get(0).setPosition(mage.getX() + 1, 	mage.getY() - healthBarfills.get(0).getRegionHeight() - 6);
		healthBarfills.get(1).setPosition(warrior.getX() + 1, warrior.getY()- healthBarfills.get(1).getRegionHeight() - 6);
		healthBarfills.get(2).setPosition(rogue.getX() + 1, 	rogue.getY()- healthBarfills.get(2).getRegionHeight() - 6);
		
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
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.CAST) {
			for(Button b2 : warriorCastButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ITEM) {
			for(Button b2 : itemButtons){
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
								switch ( b2.getActionW() ) {
								case DEFAULTWATTACK:
									System.out.println("ATTACKING!");
									break;
								case SPECIALWSKILL:
									System.out.println("ATTACKING!");
									break;
								default:
									break;
								}
								turnQueue.removeIndex(0);
								b2.setClicked(false);
							}
						}
						break;
					case CAST:
						break;
					case ITEM:
						for(Button b2 : itemButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								b2.getItem().use(warrior);
								b2.setText(b2.getItem().toString() + " x" + b2.getItem().getCount());
								if(b2.getItem().getCount() == 0) {	
									if(itemButtons.size > 1){
										for(int i=0; i<itemButtons.size-1; i++){
											itemButtons.get(i+1).setX(itemButtons.get(i).getX());
											itemButtons.get(i+1).setY(itemButtons.get(i).getY());
										}
									}
									itemButtons.removeValue(b2, true);
								}
							}
						}
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
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.CAST) {
			for(Button b2 : rogueCastButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ITEM) {
			for(Button b2 : itemButtons){
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
								switch ( b2.getActionR() ) {
								case DEFAULTRATTACK:
									System.out.println("ATTACKING!");
									turnQueue.removeIndex(0);
									b2.setClicked(false);
									break;
								case SPECIALRSKILL:
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
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.CAST) {
			for(Button b2 : mageCastButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ITEM) {
			for(Button b2 : itemButtons){
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
								switch ( b2.getActionM() ) {
								case DEFAULTMATTACK:
									System.out.println("ATTACKING!");
									turnQueue.removeIndex(0);
									b2.setClicked(false);
									break;
								case SPECIALMSKILL:
									break;
								default:
									break;
								}
							}
						}
						break;
					case CAST:
						for(Button b2 : mageCastButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								switch ( b2.getActionM() ) {
								case DEFAULTMATTACK:
									System.out.println("ATTACKING!");
									turnQueue.removeIndex(0);
									b2.setClicked(false);
									break;
								case SPECIALMSKILL:
									break;
								default:
									break;
								}
							}
						}
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
		if(turnQueue.size == 0) {
			resetButtonClickable();
			countTurn(dt);
		} else {
			Character c = turnQueue.first();
			for(Button b : mainButtons) {
				b.setClickable(true);
			}
			if(c instanceof Warrior) {
				handleWarriorInput();
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
				Array<Button> drawableAttackSubButtons = null;
				Array<Button> drawableCastSubButtons = null;
				if(turnQueue.first() instanceof Warrior) {
					drawableAttackSubButtons = warriorAttackButtons;
					drawableCastSubButtons = warriorCastButtons;
				} else if(turnQueue.first() instanceof Mage) {
					drawableAttackSubButtons = mageAttackButtons;
					drawableCastSubButtons = mageCastButtons;
				} else if(turnQueue.first() instanceof Rogue) {
					drawableAttackSubButtons = rogueAttackButtons;
					drawableCastSubButtons = rogueCastButtons;
				}
				switch( b.getAction() ) {
					case ATTACK:
						for(Button b2 : drawableAttackSubButtons) {
							b2.render(sb);
						}
						break;
					case CAST:
						for(Button b2 : drawableCastSubButtons) {
							b2.render(sb);
						}
						break;
					case ITEM:
						sb.begin();
						itembg.draw(sb);
						sb.end();
						for(Button b2 : itemButtons) {
							b2.render(sb);
						}
						break;
					case RUN:
						
						break;
				}
			}
			sb.begin();
			
			healthBarfills.get(0).setSize(62 * mage.getHp()/mage.getMaxHp(), healthBarfills.get(0).getHeight());
			healthBarfills.get(1).setSize(62 * warrior.getHp()/warrior.getMaxHp(), healthBarfills.get(1).getHeight());
			healthBarfills.get(2).setSize(62 * rogue.getHp()/rogue.getMaxHp(), healthBarfills.get(2).getHeight());
			for(Sprite hpbg : healthBarbgs){
				hpbg.draw(sb);
			}	
			for(Sprite hpfills : healthBarfills) {
				hpfills.draw(sb);
			}
			sb.end();
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
		assets.unload(CLICKED_BUTTONS_IMG);
		assets.unload(NOT_CLICKABLE_BUTTONS_IMG);
		assets.unload(SUBBUTTONTOP_IMG);					
		assets.unload(SUBBUTTONMID_IMG); 					
		assets.unload(SUBBUTTONBOTTOM_IMG); 				
		assets.unload(SUBBUTTONTOPHOVER_IMG); 				
		assets.unload(SUBBUTTONMIDHOVER_IMG); 				
		assets.unload(SUBBUTTONBOTTOMHOVER_IMG);		 
		assets.unload(SUBBUTTONTOP_NOTCLICKABLE_IMG); 		
		assets.unload(SUBBUTTONMID_NOTCLICKABLE_IMG); 		
		assets.unload(SUBBUTTONBOTTOM_NOTCLICKABLE_IMG);
		font.dispose();
		resetCharacters();
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
		warriorCastButtons = new Array<Button>();
		mageCastButtons = new Array<Button>();
		rogueCastButtons = new Array<Button>();
		buttonsRegion = TextureRegion.split((Texture)assets.get(BUTTONS_IMG), 400, 104);
		clickedButtonRegion = TextureRegion.split((Texture)assets.get(CLICKED_BUTTONS_IMG), 400, 104);
		notClickablebuttonsRegion = TextureRegion.split((Texture)assets.get(NOT_CLICKABLE_BUTTONS_IMG), 400, 104);
		
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
				TextureRegion[] tmp = new TextureRegion[3];
				tmp[0] = buttonsRegion[i][j];
				tmp[1] = clickedButtonRegion[i][j];
				tmp[2] = notClickablebuttonsRegion[i][j];
				mainButtons.get(k).setTextureRegion(tmp);
				k++;
			}
		}

		TextureRegion[] subBtop = new TextureRegion[3];
		TextureRegion[] subBmid = new TextureRegion[3];
		TextureRegion[] subBbot = new TextureRegion[3];
		
		subBtop[0] = new TextureRegion((Texture)assets.get(SUBBUTTONTOP_IMG), 0, 0, 150, 50);
		subBtop[1] = new TextureRegion((Texture)assets.get(SUBBUTTONTOPHOVER_IMG), 0, 0, 150, 50);
		subBtop[2] = new TextureRegion((Texture)assets.get(SUBBUTTONTOP_NOTCLICKABLE_IMG), 0, 0, 150, 50);
		subBmid[0] = new TextureRegion((Texture)assets.get(SUBBUTTONMID_IMG), 0, 0, 150, 50);
		subBmid[1] = new TextureRegion((Texture)assets.get(SUBBUTTONMIDHOVER_IMG), 0, 0, 150, 50);
		subBmid[2] = new TextureRegion((Texture)assets.get(SUBBUTTONMID_NOTCLICKABLE_IMG), 0, 0, 150, 50);
		subBbot[0] = new TextureRegion((Texture)assets.get(SUBBUTTONBOTTOM_IMG), 0, 0, 150, 50);
		subBbot[1] = new TextureRegion((Texture)assets.get(SUBBUTTONBOTTOMHOVER_IMG), 0, 0, 150, 50);
		subBbot[2] = new TextureRegion((Texture)assets.get(SUBBUTTONBOTTOM_NOTCLICKABLE_IMG), 0, 0, 150, 50);
		
		//Warrior's buttons:
		for(int i=0; i<warrior.getAttackAbilities().size; i++) {
			warriorAttackButtons.add(new Button(mainButtons.get(0).getWidth() - subBtop[0].getRegionWidth() - 20, 
					(warrior.getAttackAbilities().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(0).getY()/warrior.getAttackAbilities().size, 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					warrior.getAttackAbilities().get(i).toString(),
					warrior.getAttackAbilities().get(i),
					subfont));
			
			if(i == 0) {
				warriorAttackButtons.get(i).setTextureRegion(subBtop);
			} else if(i < warrior.getAttackAbilities().size-1){
				warriorAttackButtons.get(i).setTextureRegion(subBmid);
			} else {
				warriorAttackButtons.get(i).setTextureRegion(subBbot);
			}
		}				
		
		for(int i=0; i<warrior.getSpells().size; i++) {
			warriorCastButtons.add(new Button(mainButtons.get(1).getX() + 20, 
					(warrior.getSpells().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(1).getY()/warrior.getSpells().size, 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					warrior.getSpells().get(i).toString(),
					warrior.getSpells().get(i),
					subfont));
			
			if(i == 0) {
				warriorCastButtons.get(i).setTextureRegion(subBtop);
			} else if(i < warrior.getSpells().size-1){
				warriorCastButtons.get(i).setTextureRegion(subBmid);
			} else {
				warriorCastButtons.get(i).setTextureRegion(subBbot);
			}
		}
		
		//Mage buttons:
		for(int i=0; i<mage.getAttackAbilities().size; i++) {
			mageAttackButtons.add(new Button(mainButtons.get(0).getWidth() - subBtop[0].getRegionWidth() - 20, 
					(mage.getAttackAbilities().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(0).getY()/mage.getAttackAbilities().size, 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					mage.getAttackAbilities().get(i).toString(),
					mage.getAttackAbilities().get(i),
					subfont));
			
			if(i == 0) {
				mageAttackButtons.get(i).setTextureRegion(subBtop);
			} else if(i < mage.getAttackAbilities().size-1){
				mageAttackButtons.get(i).setTextureRegion(subBmid);
			} else {
				mageAttackButtons.get(i).setTextureRegion(subBbot);
			}
		}
		
		for(int i=0; i<mage.getSpells().size; i++) {
			mageCastButtons.add(new Button(mainButtons.get(1).getX() + 20, 
					(mage.getSpells().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(1).getY()/mage.getSpells().size, 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					mage.getSpells().get(i).toString(),
					mage.getSpells().get(i),
					subfont));
			
			if(i == 0) {
				mageCastButtons.get(i).setTextureRegion(subBtop);
			} else if(i < mage.getSpells().size-1){
				mageCastButtons.get(i).setTextureRegion(subBmid);
			} else {
				mageCastButtons.get(i).setTextureRegion(subBbot);
			}
		}
		
		//Rogue buttons:
		for(int i=0; i<rogue.getAttackAbilities().size; i++) {
			rogueAttackButtons.add(new Button(mainButtons.get(0).getWidth() - subBtop[0].getRegionWidth() - 20, 
					(rogue.getAttackAbilities().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(0).getY()/rogue.getAttackAbilities().size, 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					rogue.getAttackAbilities().get(i).toString(),
					rogue.getAttackAbilities().get(i),
					subfont));
			
			if(i == 0) {
				rogueAttackButtons.get(i).setTextureRegion(subBtop);
			} else if(i < rogue.getAttackAbilities().size-1){
				rogueAttackButtons.get(i).setTextureRegion(subBmid);
			} else {
				rogueAttackButtons.get(i).setTextureRegion(subBbot);
			}
		}
		
		for(int i=0; i<rogue.getSpells().size; i++) {
			rogueCastButtons.add(new Button(mainButtons.get(1).getX() + 20, 
					(rogue.getSpells().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(1).getY()/rogue.getSpells().size, 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					rogue.getSpells().get(i).toString(),
					rogue.getSpells().get(i),
					subfont));
			
			if(i == 0) {
				rogueCastButtons.get(i).setTextureRegion(subBtop);
			} else if(i < rogue.getSpells().size-1){
				rogueCastButtons.get(i).setTextureRegion(subBmid);
			} else {
				rogueCastButtons.get(i).setTextureRegion(subBbot);
			}
		}
		
		//Item buttons:
		//BG:
		itembg = new Sprite((Texture)assets.get(ITEMBG_IMG));
		itembg.setPosition(mainButtons.get(2).getWidth()/4, mainButtons.get(2).getHeight()/2);
		
		//Buttons:
		itemButtons = new Array<Button>();
		for(int i=0; i<items.size; i++) {
			itemButtons.add(new Button((int)itembg.getX(), 
					(int)itembg.getY() + (int)itembg.getRegionHeight() - ((i+1) * subBtop[0].getRegionHeight()), 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					items.get(i).toString()+" x"+items.get(i).getCount(),
					items.get(i),
					subfont));
			
			itemButtons.get(i).setTextureRegion(subBmid);
		}
	}
	
	private void resetButtonClickable() {
		for(Button b : mainButtons) {
			b.setClickable(false);
		}
	}
	
	private void resetCharacters() {
		mage.setXY(oldMagePosition.x, oldMagePosition.y);
		warrior.setXY(oldWarriorPosition.x, oldWarriorPosition.y);
		rogue.setXY(oldRoguePosition.x, oldRoguePosition.y);
		mage.setAttackCharge(0);
		warrior.setAttackCharge(0);
		rogue.setAttackCharge(0);
	}
}
