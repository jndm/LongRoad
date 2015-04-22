package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Mage;
import com.mygdx.game.elements.characters.Rogue;
import com.mygdx.game.elements.characters.Warrior;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Button;
import com.mygdx.game.helpers.ButtonAction;

public class Battle extends GameState {
	
	private final int BUTTON_ROWS = 2;
	private final int BUTTON_COLS = 2;
	
	private TextureRegion bg;
	private Sprite itembg;
	
	private Array<Sprite> healthBarbgs;
	private Array<Sprite> healthBarfills;

	//Buttons
	private TextureAtlas atlas;
	private TextureRegion[][] buttonsRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	private TextureRegion[][] notClickablebuttonsRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	private TextureRegion[][] clickedButtonRegion = new TextureRegion[BUTTON_ROWS][BUTTON_COLS];
	
	private Array<Button> mainButtons;
	private Array<Button> subAttackButtons;
	private Array<Button> subCastButtons;
	
	private Array<Button> warriorAttackButtons;
	private Array<Button> mageAttackButtons;
	private Array<Button> rogueAttackButtons;
	
	private Array<Button> warriorCastButtons;
	private Array<Button> mageCastButtons;
	private Array<Button> rogueCastButtons;
	
	private Array<Button> itemButtons;
	
	//Character
	private Array<Character> playerCharacters;
	private final int MAGE = 0;
	private final int WARRIOR = 1;
	private final int ROGUE = 2;
	private Character target;
	
	private Vector2 oldMagePosition, oldWarriorPosition, oldRoguePosition;
	private Array<Character> enemies;
	private Array<Character> turnQueue;
	private Array<Item> items;
	private final float WAIT_TIME = 1f;
	private float timeElapsed = 0;
	private Skill activeAction;
	private final int ACTIONMOVEMAX = 300;
	private final float ORIGINALMAGEX = 20;
	private final float ORIGINALWARRIORX = 100;
	private final float ORIGINALROGUEX = 20;
	private boolean steppedForvard = false;
	private boolean animationPlayed = false;
	private float stateTime = 0;
	
	//Final variables for images
	private final String BACKGROUND_IMG 					= "background";
	
	private final String SUBBUTTONTOP_IMG 					= "subButtonTop";
	private final String SUBBUTTONMID_IMG 					= "subButtonMiddle";
	private final String SUBBUTTONBOTTOM_IMG 				= "subButtonBottom";
	private final String SUBBUTTONTOPHOVER_IMG 				= "subButtonTopHover";
	private final String SUBBUTTONMIDHOVER_IMG 				= "subButtonMiddleHover";
	private final String SUBBUTTONBOTTOMHOVER_IMG		 	= "subButtonBottomHover";
	private final String SUBBUTTONTOP_NOTCLICKABLE_IMG 		= "subButtonTopNotClickable";
	private final String SUBBUTTONMID_NOTCLICKABLE_IMG 		= "subButtonMiddleNotClickable";
	private final String SUBBUTTONBOTTOM_NOTCLICKABLE_IMG 	= "subButtonBottomNotClickable";

	private final String BUTTONS_IMG 						= "buttons";
	private final String CLICKED_BUTTONS_IMG 				= "clickedButtons";
	private final String NOT_CLICKABLE_BUTTONS_IMG 			= "notClickableButtons";
	
	private final String ITEMBG_IMG 						= "itembg";
	
	private final String HEALTHBARBG_IMG 					= "healthbarbg";
	private final String HEALTHBARFILL_IMG 					= "healthbarfill";
	
	private final String BATTLEATLAS						= "background/battle/battleassets.pack";
	
	private BitmapFont font = new BitmapFont();
	
	public Battle(GameStateManager gsm) {
		super(gsm);
	}

	public Battle(GameStateManager gsm, Array<Character> enemies, Array<Character> chars, Array<Item> items) {
		super(gsm);	
		this.enemies = enemies;
		this.items = items;
		turnQueue = new Array<Character>();
		
		assets.load(BATTLEATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(BATTLEATLAS);
		bg = atlas.findRegion(BACKGROUND_IMG);
		healthBarbgs = new Array<Sprite>();
		healthBarfills = new Array<Sprite>();

		playerCharacters = chars;
		
		oldMagePosition = new Vector2(playerCharacters.get(MAGE).getX(), playerCharacters.get(MAGE).getY());
		oldWarriorPosition = new Vector2(playerCharacters.get(WARRIOR).getX(), playerCharacters.get(WARRIOR).getY());
		oldRoguePosition = new Vector2(playerCharacters.get(ROGUE).getX(), playerCharacters.get(ROGUE).getY());
		
		playerCharacters.get(MAGE).setXY(ORIGINALMAGEX, 300);
		playerCharacters.get(WARRIOR).setXY(ORIGINALWARRIORX, 250);
		playerCharacters.get(ROGUE).setXY(ORIGINALROGUEX, 228);
		
		for(Character c : enemies) {
			c.setX(700);
			c.setY(c.getY() + 190);
		}
		
		initButtons();
		
		for(int i=0; i<playerCharacters.size; i++) {
			healthBarbgs.add(new Sprite(atlas.findRegion(HEALTHBARBG_IMG)));
			healthBarbgs.get(i).setPosition(playerCharacters.get(i).getX(), playerCharacters.get(i).getY() - healthBarbgs.get(0).getRegionHeight() - 5);
			healthBarfills.add(new Sprite(atlas.findRegion(HEALTHBARFILL_IMG)));
			healthBarfills.get(i).setPosition(playerCharacters.get(i).getX() + 1, 	playerCharacters.get(i).getY() - healthBarfills.get(0).getRegionHeight() - 6);
		}
		
	}

	private void handleInput() {
		Button clickedMainButton = null;
		//Check if one of the main buttons is already clicked
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				clickedMainButton = b;
			}
		}
		
		checkSubButtonHovering(clickedMainButton);
		
		if(Gdx.input.justTouched()) {
			// check if player clicked one of the sub-buttons and act
			if(clickedMainButton != null){
				switch( clickedMainButton.getAction()) {
				
					case ATTACK:
						for(Button b2 : subAttackButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								activeAction = b2.getAction2();
								b2.setClicked(false);
							}
						}
						break;
						
					case CAST:
						for(Button b2 : subCastButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								
								turnQueue.removeIndex(0);
								b2.setClicked(false);
							}
						}
						break;
						
					case ITEM:
						for(Button b2 : itemButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								b2.getItem().use(playerCharacters.get(WARRIOR));
								b2.setText(b2.getItem().toString() + " x" + b2.getItem().getCount());
								if(b2.getItem().getCount() == 0) {	//If items count is 0, move next item higher in the items line
									if(itemButtons.size > 1){
										for(int i=0; i<itemButtons.size-1; i++){
											itemButtons.get(i+1).setX(itemButtons.get(i).getX());
											itemButtons.get(i+1).setY(itemButtons.get(i).getY());
										}
									}
									itemButtons.removeValue(b2, true);
								}
								turnQueue.removeIndex(0);
								b2.setClicked(false);
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
							System.out.println("RUNNING AWAY!");
							gsm.popState();
						}
					}
				}
			} 
		}
	}
	
	private void checkSubButtonHovering(Button clickedMainButton) {
		//for hovering effect
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ATTACK) {
			for(Button b2 : subAttackButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.CAST) {
			for(Button b2 : subCastButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
		
		if(clickedMainButton != null && clickedMainButton.getAction() == ButtonAction.ITEM) {
			for(Button b2 : itemButtons){
				b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY());
			}
		}
	}

	@Override
	public void update(float dt) {
		//If there is no action to be shown:
		if(activeAction == null) {
			if(turnQueue.size == 0) {
				resetButtonClickable();
				countTurn(dt);
			} else {
				for(Button b : mainButtons) {
					b.setClickable(true);
				}
				if(turnQueue.first() instanceof Warrior) {
					subAttackButtons = warriorAttackButtons;
					subCastButtons = warriorCastButtons;
				}else if (turnQueue.first() instanceof Mage) {
					subAttackButtons = mageAttackButtons;
					subCastButtons = mageCastButtons;
				}else if (turnQueue.first() instanceof Rogue) {
					subAttackButtons = rogueAttackButtons;
					subCastButtons = rogueCastButtons;
				}else {
					
				}			
				handleInput();
			}
			
			checkTarget();
		}
		//If there is an action to be shown wait for it to finish:
		else {
			doActiveAction(dt);
		}
	}

	private void doActiveAction(float dt) {
		stateTime += dt;
		if(!steppedForvard){
			if(turnQueue.first().getX() < ACTIONMOVEMAX){
				turnQueue.first().move(dt);	
			}else {
				steppedForvard = true;
				turnQueue.first().setActFinished(false);
			}
		}
		
		if(steppedForvard && turnQueue.first().isActFinished()) {
			if(turnQueue.first() instanceof Warrior) {
				if(turnQueue.first().getX() > ORIGINALWARRIORX) {
					turnQueue.first().moveBackward(dt);
				} else {
					steppedForvard = false;
					animationPlayed = false;
					activeAction = null;
					turnQueue.removeIndex(0);
				}
			}else if (turnQueue.first() instanceof Mage) {
				if(turnQueue.first().getX() > ORIGINALMAGEX) {
					turnQueue.first().moveBackward(dt);
				} else {
					steppedForvard = false;
					animationPlayed = false;
					activeAction = null;
					turnQueue.removeIndex(0);
				}
			}else if (turnQueue.first() instanceof Rogue) {
				if(turnQueue.first().getX() > ORIGINALROGUEX) {
					turnQueue.first().moveBackward(dt);
				} else {
					steppedForvard = false;
					animationPlayed = false;
					activeAction = null;
					turnQueue.removeIndex(0);
				}
			}
		}
	}

	private void checkTarget() {
		if(Gdx.input.justTouched()) {
			for(Character e : enemies) {
				if(Gdx.input.getX() > e.getX() && Gdx.input.getX() < e.getX() + e.getTexture().getWidth() && 
					Game.HEIGHT - Gdx.input.getY() > e.getY() && Game.HEIGHT - Gdx.input.getY() < e.getY() + e.getTexture().getHeight()){
						target = e;
						System.out.println("Target set: "+ e.getClass().getSimpleName());
				}
			}
		}		
	}

	private void countTurn(float dt) {
		timeElapsed += dt;
		if(WAIT_TIME <= timeElapsed) {
			for(Character c : playerCharacters) {
				c.addCharge();
				if(c.isCharged()) {
					turnQueue.add(c);
					System.out.print(c.getClass().getSimpleName()+" charged!");
				}
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
			sb.draw(bg, 0, Game.HEIGHT - bg.getRegionHeight());
		sb.end();
		
		//Characters
		for(Character c : playerCharacters) {
			c.render(sb);
		}
		
		for(Button b : mainButtons) {
			b.render(sb);
		}
		
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				switch( b.getAction() ) {
					case ATTACK:
						for(Button b2 : subAttackButtons) {
							b2.render(sb);
						}
						break;
					case CAST:
						for(Button b2 : subCastButtons) {
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
			for(int i=0; i<playerCharacters.size; i++) {
				healthBarfills.get(i).setSize(62 * playerCharacters.get(i).getHp()/playerCharacters.get(i).getMaxHp(), healthBarfills.get(0).getHeight());
				healthBarbgs.get(i).draw(sb);
				healthBarfills.get(i).draw(sb);
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
		assets.unload(BATTLEATLAS);
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
		subAttackButtons = new Array<Button>();
		subCastButtons = new Array<Button>();
		
		warriorAttackButtons = new Array<Button>();
		mageAttackButtons = new Array<Button>();
		rogueAttackButtons = new Array<Button>();
		warriorCastButtons = new Array<Button>();
		mageCastButtons = new Array<Button>();
		rogueCastButtons = new Array<Button>();
		
		buttonsRegion = atlas.findRegion(BUTTONS_IMG).split(400,104);
		clickedButtonRegion = atlas.findRegion(CLICKED_BUTTONS_IMG).split(400, 104);
		notClickablebuttonsRegion = atlas.findRegion(NOT_CLICKABLE_BUTTONS_IMG).split(400, 104);
		
		//Creating button main buttons
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
		
		subBtop[0] = atlas.findRegion(SUBBUTTONTOP_IMG);
		subBtop[1] = atlas.findRegion(SUBBUTTONTOPHOVER_IMG);
		subBtop[2] = atlas.findRegion(SUBBUTTONTOP_NOTCLICKABLE_IMG);
		subBmid[0] = atlas.findRegion(SUBBUTTONMID_IMG);
		subBmid[1] = atlas.findRegion(SUBBUTTONMIDHOVER_IMG);
		subBmid[2] = atlas.findRegion(SUBBUTTONMID_NOTCLICKABLE_IMG);
		subBbot[0] = atlas.findRegion(SUBBUTTONBOTTOM_IMG);
		subBbot[1] = atlas.findRegion(SUBBUTTONBOTTOMHOVER_IMG);
		subBbot[2] = atlas.findRegion(SUBBUTTONBOTTOM_NOTCLICKABLE_IMG);
		
		//Create character's attack and cast buttons
		for(int j=0; j<playerCharacters.size; j++) {
			for(int i=0; i<playerCharacters.get(j).getAttackAbilities().size; i++) {
				//Warrior attack buttons
				if(playerCharacters.get(j) instanceof Warrior) { 
					warriorAttackButtons.add(new Button(mainButtons.get(0).getWidth() - subBtop[0].getRegionWidth() - 20, 
							(playerCharacters.get(j).getAttackAbilities().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(0).getY()/playerCharacters.get(j).getAttackAbilities().size, 
							subBtop[0].getRegionWidth(), 
							subBtop[0].getRegionHeight(), 
							playerCharacters.get(j).getAttackAbilities().get(i).toString(),
							playerCharacters.get(j).getAttackAbilities().get(i),
							subfont));
					
					if(i == 0) {
						warriorAttackButtons.get(i).setTextureRegion(subBtop);
					} else if(i < playerCharacters.get(j).getAttackAbilities().size-1){
						warriorAttackButtons.get(i).setTextureRegion(subBmid);
					} else {
						warriorAttackButtons.get(i).setTextureRegion(subBbot);
					}
				} 
				//Mage attack buttons:
				else if(playerCharacters.get(j) instanceof Mage) {
					mageAttackButtons.add(new Button(mainButtons.get(0).getWidth() - subBtop[0].getRegionWidth() - 20, 
							(playerCharacters.get(j).getAttackAbilities().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(0).getY()/playerCharacters.get(j).getAttackAbilities().size, 
							subBtop[0].getRegionWidth(), 
							subBtop[0].getRegionHeight(), 
							playerCharacters.get(j).getAttackAbilities().get(i).toString(),
							playerCharacters.get(j).getAttackAbilities().get(i),
							subfont));
					
					if(i == 0) {
						mageAttackButtons.get(i).setTextureRegion(subBtop);
					} else if(i < playerCharacters.get(j).getAttackAbilities().size-1){
						mageAttackButtons.get(i).setTextureRegion(subBmid);
					} else {
						mageAttackButtons.get(i).setTextureRegion(subBbot);
					}
				} 
				//Rogue attack buttons:
				else if(playerCharacters.get(j) instanceof Rogue) {
					rogueAttackButtons.add(new Button(mainButtons.get(0).getWidth() - subBtop[0].getRegionWidth() - 20, 
							(playerCharacters.get(j).getAttackAbilities().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(0).getY()/playerCharacters.get(j).getAttackAbilities().size, 
							subBtop[0].getRegionWidth(), 
							subBtop[0].getRegionHeight(), 
							playerCharacters.get(j).getAttackAbilities().get(i).toString(),
							playerCharacters.get(j).getAttackAbilities().get(i),
							subfont));
					
					if(i == 0) {
						rogueAttackButtons.get(i).setTextureRegion(subBtop);
					} else if(i < playerCharacters.get(j).getAttackAbilities().size-1){
						rogueAttackButtons.get(i).setTextureRegion(subBmid);
					} else {
						rogueAttackButtons.get(i).setTextureRegion(subBbot);
					}
				}
			}
			
			for(int i=0; i<playerCharacters.get(j).getSpells().size; i++) {
				//Warrior cast buttons:
				if(playerCharacters.get(j) instanceof Warrior) { 
					warriorCastButtons.add(new Button(mainButtons.get(1).getX() + 20, 
							(playerCharacters.get(j).getSpells().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(1).getY()/playerCharacters.get(j).getSpells().size, 
							subBtop[0].getRegionWidth(), 
							subBtop[0].getRegionHeight(), 
							playerCharacters.get(j).getSpells().get(i).toString(),
							playerCharacters.get(j).getSpells().get(i),
							subfont));
					
					if(i == 0) {
						warriorCastButtons.get(i).setTextureRegion(subBtop);
					} else if(i < playerCharacters.get(j).getSpells().size-1){
						warriorCastButtons.get(i).setTextureRegion(subBmid);
					} else {
						warriorCastButtons.get(i).setTextureRegion(subBbot);
					}
				} 
				//Mage cast buttons:
				else if(playerCharacters.get(j) instanceof Mage) { 
					mageCastButtons.add(new Button(mainButtons.get(1).getX() + 20, 
							(playerCharacters.get(j).getSpells().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(1).getY()/playerCharacters.get(j).getSpells().size, 
							subBtop[0].getRegionWidth(), 
							subBtop[0].getRegionHeight(), 
							playerCharacters.get(j).getSpells().get(i).toString(),
							playerCharacters.get(j).getSpells().get(i),
							subfont));
					
					if(i == 0) {
						mageCastButtons.get(i).setTextureRegion(subBtop);
					} else if(i < playerCharacters.get(j).getSpells().size-1){
						mageCastButtons.get(i).setTextureRegion(subBmid);
					} else {
						mageCastButtons.get(i).setTextureRegion(subBbot);
					}
				} 
				//Rogue cast buttons:
				else if(playerCharacters.get(j) instanceof Rogue) { 
					rogueCastButtons.add(new Button(mainButtons.get(1).getX() + 20, 
							(playerCharacters.get(j).getSpells().size - i) * subBtop[0].getRegionHeight() + mainButtons.get(1).getY()/playerCharacters.get(j).getSpells().size, 
							subBtop[0].getRegionWidth(), 
							subBtop[0].getRegionHeight(), 
							playerCharacters.get(j).getSpells().get(i).toString(),
							playerCharacters.get(j).getSpells().get(i),
							subfont));
					
					if(i == 0) {
						rogueCastButtons.get(i).setTextureRegion(subBtop);
					} else if(i < playerCharacters.get(j).getSpells().size-1){
						rogueCastButtons.get(i).setTextureRegion(subBmid);
					} else {
						rogueCastButtons.get(i).setTextureRegion(subBbot);
					}
				}
			}
		}
		
		//Item buttons:
		//BG:
		itembg = new Sprite(atlas.findRegion(ITEMBG_IMG));
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
		playerCharacters.get(MAGE).setXY(oldMagePosition.x, oldMagePosition.y);
		playerCharacters.get(WARRIOR).setXY(oldWarriorPosition.x, oldWarriorPosition.y);
		playerCharacters.get(ROGUE).setXY(oldRoguePosition.x, oldRoguePosition.y);
		playerCharacters.get(MAGE).setAttackCharge(0);
		playerCharacters.get(WARRIOR).setAttackCharge(0);
		playerCharacters.get(ROGUE).setAttackCharge(0);
	}
}
