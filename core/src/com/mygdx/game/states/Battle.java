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
import com.mygdx.game.elements.characters.Skeleton;
import com.mygdx.game.elements.characters.Warrior;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Button;
import com.mygdx.game.helpers.ButtonAction;

public class Battle extends GameState {
	
	private TextureRegion bg;
	private Sprite mainbuttonbg;
	private TextureRegion pointer;
	
	private Array<Sprite> barBgs;
	private Array<Sprite> healthBarfills;
	private Array<Sprite> manaBarfills;

	//Buttons
	private TextureAtlas atlas;
	
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
	private Character enemyTarget;
	
	private Vector2 oldMagePosition, oldWarriorPosition, oldRoguePosition;
	private Array<Character> enemies;
	private Array<Character> turnQueue;
	private Array<Item> items;
	private final float WAIT_TIME = 1f;
	private float timeElapsed = 0;
	private Skill activeAction;
	private final int ACTIONMOVEMAX = 300;
	private boolean steppedForvard = false;
	private boolean actionFinished = false;
	private boolean playersTurn = false;
	private int deadPlayerCharacters = 0;
	private int deadEnemies = 0;
	
	//Final variables for images
	private final String BACKGROUND_IMG 					= "background";
	private final String MAINBUTTONBG_IMG					= "buttonsbackg";
	
	private final String SUBBUTTONTOP_IMG 					= "subButtonTop";
	private final String SUBBUTTONMID_IMG 					= "subButtonMiddle";
	private final String SUBBUTTONBOTTOM_IMG 				= "subButtonBottom";
	private final String SUBBUTTONTOPHOVER_IMG 				= "subButtonTopHover";
	private final String SUBBUTTONMIDHOVER_IMG 				= "subButtonMiddleHover";
	private final String SUBBUTTONBOTTOMHOVER_IMG		 	= "subButtonBottomHover";
	private final String SUBBUTTONTOP_NOTCLICKABLE_IMG 		= "subButtonTopNotClickable";
	private final String SUBBUTTONMID_NOTCLICKABLE_IMG 		= "subButtonMiddleNotClickable";
	private final String SUBBUTTONBOTTOM_NOTCLICKABLE_IMG 	= "subButtonBottomNotClickable";

	private final String BUTTONS_IMG 						= "mainButton";
	private final String CLICKED_BUTTONS_IMG 				= "mainButtonClicked";
	private final String NOT_CLICKABLE_BUTTONS_IMG 			= "mainButtonNotclickable";
	
	private final String HEALTHBARBG_IMG 					= "healthbarbg";
	private final String HEALTHBARFILL_IMG 					= "healthbarfill";
	private final String MANABARFILL_IMG 					= "manabarfill";
	
	private final String POINTER_IMG						= "pointer";
	
	private final String BATTLEATLAS						= "background/battle/battleassets.pack";
	
	private BitmapFont fpsfont = new BitmapFont();
	private BitmapFont mainfont;
	private BitmapFont subfont;
	
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
		barBgs = new Array<Sprite>();
		healthBarfills = new Array<Sprite>();
		manaBarfills = new Array<Sprite>();
		pointer = atlas.findRegion(POINTER_IMG);

		playerCharacters = chars;
		
		oldMagePosition 	= new Vector2(playerCharacters.get(MAGE).getX(), 		playerCharacters.get(MAGE).getY());
		oldWarriorPosition 	= new Vector2(playerCharacters.get(WARRIOR).getX(), 	playerCharacters.get(WARRIOR).getY());
		oldRoguePosition 	= new Vector2(playerCharacters.get(ROGUE).getX(), 		playerCharacters.get(ROGUE).getY());
		
		playerCharacters.get(MAGE).setXY(playerCharacters.get(MAGE).getBattleposition().x, 			playerCharacters.get(MAGE).getBattleposition().y);
		playerCharacters.get(WARRIOR).setXY(playerCharacters.get(WARRIOR).getBattleposition().x, 	playerCharacters.get(WARRIOR).getBattleposition().y);
		playerCharacters.get(ROGUE).setXY(playerCharacters.get(ROGUE).getBattleposition().x, 		playerCharacters.get(ROGUE).getBattleposition().y);
		
		initEnemies();
		target = this.enemies.first();
		initButtons();
		initHpAndManaBars();
		
	}

	

	private void handleInput() {
		Button clickedMainButton = null;
		//Check if one of the main buttons is already clicked
		for(Button b : mainButtons) {
			if(b.isClicked()) {
				clickedMainButton = b;
			}
		}

		checkIfTargetClicked();
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
								activeAction = b2.getAction2();
								b2.setClicked(false);
							}
						}
						break;
						
					case ITEM:
						for(Button b2 : itemButtons){
							if(b2.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
								b2.getItem().use(turnQueue.first());
								b2.setText(b2.getItem().toString() + " x" + b2.getItem().getCount());
								checkIfNoMoreItems(b2);
								turnQueue.removeIndex(0);
								b2.setClicked(false);
							}
						}
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

	@Override
	public void update(float dt) {
		if(activeAction == null) { //If there is no action to be shown:
			if(turnQueue.size == 0) {	// If no-one has turn, charge it
				resetButtonClickable();
				countTurn(dt);
				turnQueue.shrink(); //For memory management
			} else {
				if(!turnQueue.first().isAlive()) {	//check if character is alive because it may have charged before it died
					turnQueue.removeIndex(0);
				} else {
					if(turnQueue.first() instanceof Warrior) {
						subAttackButtons = warriorAttackButtons;
						subCastButtons = warriorCastButtons;
						playersTurn = true;
					}else if (turnQueue.first() instanceof Mage) {
						subAttackButtons = mageAttackButtons;
						subCastButtons = mageCastButtons;
						playersTurn = true;
					}else if (turnQueue.first() instanceof Rogue) {
						subAttackButtons = rogueAttackButtons;
						subCastButtons = rogueCastButtons;
						playersTurn = true;
					} else {
						playersTurn = false;
					}
					
					if(playersTurn) {
						for(Button b : mainButtons) {
							b.setClickable(true);
						}	
						handleInput();
					} else {
						resetButtonClickable();
						handleAi();
					}
				}
			}
		}		
		else { //If there is an action to be shown wait for it to finish:
			if(playersTurn) {
				doActivePlayerAction(dt);			
			} 
			else {
				doActiveEnemyAction(dt);
			}
		}
	}

	private void swapTarget() {
		for(int i=0; i<enemies.size; i++) {
			if(enemies.get(i).isAlive()) {
				target = enemies.get(i);
				break;
			}
		}
	}

	private void doActiveEnemyAction(float dt) {
		//ACTION STEP: MOVE FORWARD -> DO ANIMATION -> CHECK IF PLAYERCHAR IS DEAD -> MOVE BACK -> CHECK IF ALL PLAYERCHARS ARE DEAD AND LOSE	
		//Move forward if character's x smaller than setted variable
		if(!steppedForvard){
			if(turnQueue.first().getX() > Game.WIDTH - ACTIONMOVEMAX){
				turnQueue.first().moveBackward(dt);	
			}else {
				steppedForvard = true;
				turnQueue.first().setMoving(false);
				turnQueue.first().setAttacking(true);	//Se attacking true so the animation will be played
			}
		}
		
		//When character has moved forvard do activeAction
		if(steppedForvard && !actionFinished) {
			activeAction.act(turnQueue.first(), enemyTarget);
			actionFinished = true;
		}
		
		//If character has moved enough and attack-animation has been played 
		//move back and set activeAction back to null and remove character from turnQueue
		if(steppedForvard && !turnQueue.first().isAttacking()) {
			if(enemyTarget.isAlive() && enemyTarget.getHp() < 0) {						//Check if playercharacter died
				System.out.println(enemyTarget.getClass().getSimpleName() + "died!");
				enemyTarget.setAlive(false);
				deadPlayerCharacters++;
			}
			if(turnQueue.first().getX() < turnQueue.first().getBattleposition().x) {
				turnQueue.first().move(dt);
			} else {
				resetAction();
				if(deadPlayerCharacters == playerCharacters.size) {	//If all characters are dead 
					System.out.println("YOU ARE DEAD!");
					gsm.popState();
				}
			}
		}
		
	}

	private void handleAi() {
		enemyTarget = turnQueue.first().raffleTarget(playerCharacters);
		activeAction = turnQueue.first().raffleSkillToUse();
	}

	private void doActivePlayerAction(float dt) {
		//ACTION STEPS: MOVE FORWARD -> DO ANIMATION -> CHECK IF ENEMY IS DEAD -> MOVE BACK -> CHECK IF ALL ENEMIES ARE DEAD AND WIN	
		//Move forward if character's x smaller than setted variable
		if(!steppedForvard){
			if(turnQueue.first().getX() < ACTIONMOVEMAX){
				turnQueue.first().move(dt);	
			}else {
				steppedForvard = true;
				turnQueue.first().setMoving(false);
				turnQueue.first().setAttacking(true);	//Set actFinished false so the animation will be played
			}
		}
		
		//When character has moved forvard do activeAction
		if(steppedForvard && !actionFinished) {
			activeAction.act(turnQueue.first(), target);
			actionFinished = true;
		}
		
		//If character has moved enough and attack-animation has been played 
		//move back and set activeAction back to null and remove character from turnQueue
		if(steppedForvard && !turnQueue.first().isAttacking()) {
			if(target.isAlive() && target.getHp() < 0) {						//Check if enemy died
					System.out.println(target.getClass().getSimpleName() + "died!");
					target.setAlive(false);
					deadEnemies++;
					swapTarget();
			}
			if(turnQueue.first().getX() > turnQueue.first().getBattleposition().x) {
				turnQueue.first().moveBackward(dt);
			} else {
				resetAction();				
				if(deadEnemies == enemies.size) {	//If all enemies are dead win battle
					System.out.println("WINNER!");
					gsm.popState();
				}
			}
		}
	}

	private void resetAction() {
		turnQueue.first().setMoving(false);
		steppedForvard = false;
		activeAction = null;
		actionFinished = false;
		turnQueue.removeIndex(0);
	}

	private void countTurn(float dt) {
		timeElapsed += dt;
		if(WAIT_TIME <= timeElapsed) {
			for(Character c : playerCharacters) {
				if(c.isAlive()) {
					c.addCharge();
					if(c.isCharged()) {
						turnQueue.add(c);
						System.out.println(c.getClass().getSimpleName()+" charged!");
					}
				}
			}
			for(Character c : enemies) {
				if(c.isAlive()) {
					c.addCharge();
					if(c.isCharged()) {
						turnQueue.add(c);
						System.out.println("Skeleton charged");
					}
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
			sb.draw(mainbuttonbg, 0, 0);
			for(int i=0; i<playerCharacters.size; i++){
				mainfont.draw(sb, playerCharacters.get(i).getName()+" ", 
						mainbuttonbg.getWidth() * 0.52f, 
						mainbuttonbg.getHeight() * (0.90f - i * 0.25f));
				subfont.drawMultiLine(sb, playerCharacters.get(i).getHp()+"/"+playerCharacters.get(i).getMaxHp()+"\n"+playerCharacters.get(i).getMana()+"/"+playerCharacters.get(i).getMaxMana(), 
						mainbuttonbg.getWidth() * 0.7f, 
						mainbuttonbg.getHeight() * (0.91f - i * 0.25f));
			}
			int k=0;
			for(int i=0; i<playerCharacters.size; i++) {
				healthBarfills.get(i).setSize(62 * playerCharacters.get(i).getHp()/playerCharacters.get(i).getMaxHp(), healthBarfills.get(0).getHeight());
				manaBarfills.get(i).setSize(62 * playerCharacters.get(i).getMana()/playerCharacters.get(i).getMaxMana(), healthBarfills.get(0).getHeight());
				barBgs.get(k).draw(sb);
				barBgs.get(k+1).draw(sb);
				healthBarfills.get(i).draw(sb);
				manaBarfills.get(i).draw(sb);
				k = k+2;
			}
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
						for(Button b2 : itemButtons) {
							b2.render(sb);
						}
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
			sb.draw(pointer, target.getX() + target.getTextureRegion().getRegionWidth()/2 - pointer.getRegionWidth()/2, target.getY() + target.getTextureRegion().getRegionHeight());
		sb.end();
		
		if(activeAction != null) {
			if(turnQueue.first() instanceof Warrior 
					|| turnQueue.first() instanceof Mage 
					|| turnQueue.first() instanceof Rogue) {
				activeAction.render(sb, target);
			}else {
				activeAction.render(sb, enemyTarget);
			}
		}
		
		sb.begin();
			fpsfont.draw(sb, "FPS: "+Gdx.graphics.getFramesPerSecond(), 730, 470);
		sb.end();
	}
	
	@Override
	public void dispose() {
		assets.unload(BATTLEATLAS);
		fpsfont.dispose();
		mainfont.dispose();
		resetCharacters();
	}
	
	private void initEnemies() {
		if(enemies.size == 1) { 
			enemies.get(0).setX(enemies.get(0).getBattleposition().x);
			enemies.get(0).setY(enemies.get(0).getBattleposition().y);
		} 
		else if (enemies.size == 2) { 
			for(int i=0; i < enemies.size; i++) {
				enemies.get(i).setX(enemies.get(i).getBattleposition().x);
				enemies.get(i).setY(enemies.get(i).getBattleposition().y);
			}
		} 
		else if (enemies.size == 3){ 
			for(int i=0; i < enemies.size; i++) {
				enemies.get(i).setX(enemies.get(i).getBattleposition().x);
				enemies.get(i).setY(enemies.get(i).getBattleposition().y);
			}
		}	
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
		
		mainbuttonbg = new Sprite(atlas.findRegion(MAINBUTTONBG_IMG));
		mainbuttonbg.setPosition(0, 0);
		TextureRegion[] mainButtonsRegion = new TextureRegion[3];
		mainButtonsRegion[0] = atlas.findRegion(BUTTONS_IMG);
		mainButtonsRegion[1] = atlas.findRegion(CLICKED_BUTTONS_IMG);
		mainButtonsRegion[2] = atlas.findRegion(NOT_CLICKABLE_BUTTONS_IMG);
		
		//Creating button main buttons
		for(int i=0; i<4; i++) {
			mainButtons.add(new Button(0, 
					(3-i) * mainButtonsRegion[0].getRegionHeight(), 
					mainButtonsRegion[0].getRegionWidth(), 
					mainButtonsRegion[0].getRegionHeight(),
					ButtonAction.values()[i].toString(),
					ButtonAction.values()[i],
					mainfont));

			mainButtons.get(i).setTextureRegion(mainButtonsRegion);
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
					warriorAttackButtons.add(new Button(mainButtons.get(0).getWidth()/2, 
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
					mageAttackButtons.add(new Button(mainButtons.get(0).getWidth()/2, 
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
					rogueAttackButtons.add(new Button(mainButtons.get(0).getWidth()/2, 
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
					warriorCastButtons.add(new Button(mainButtons.get(1).getWidth()/2, 
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
					mageCastButtons.add(new Button(mainButtons.get(1).getWidth()/2, 
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
					rogueCastButtons.add(new Button(mainButtons.get(1).getWidth()/2, 
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
		
		//Buttons:
		itemButtons = new Array<Button>();
		for(int i=0; i<items.size; i++) {
			itemButtons.add(new Button(mainButtons.get(2).getWidth()/2, 
					(items.size - i) * subBtop[0].getRegionHeight() + mainButtons.get(2).getY()/items.size, 
					subBtop[0].getRegionWidth(), 
					subBtop[0].getRegionHeight(), 
					items.get(i).toString()+" x"+items.get(i).getCount(),
					items.get(i),
					subfont));
			
			if(i == 0 && items.size > 1) {
				itemButtons.get(i).setTextureRegion(subBtop);
			} else if(i < items.size-1 || items.size == 1){
				itemButtons.get(i).setTextureRegion(subBmid);
			} else {
				itemButtons.get(i).setTextureRegion(subBbot);
			}
		}
	}
	
	private void initHpAndManaBars() {
		//Font for hp
		Texture fonttexture = new Texture(Gdx.files.internal("fonts/main.png"));
		fonttexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mainfont = new BitmapFont(Gdx.files.internal("fonts/main.fnt"), new TextureRegion(fonttexture), false);
		fonttexture = new Texture(Gdx.files.internal("fonts/sub.png"));
		fonttexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		subfont = new BitmapFont(Gdx.files.internal("fonts/sub.fnt"), new TextureRegion(fonttexture), false);
		
		int k=0;
		for(int i=0; i<playerCharacters.size; i++) {
			barBgs.add(new Sprite(atlas.findRegion(HEALTHBARBG_IMG)));
			barBgs.get(k).setPosition(mainbuttonbg.getWidth() * 0.82f, 
					mainbuttonbg.getHeight() * (0.90f - i * 0.25f) - barBgs.get(k).getHeight());
			
			healthBarfills.add(new Sprite(atlas.findRegion(HEALTHBARFILL_IMG)));
			healthBarfills.get(i).setPosition(barBgs.get(k).getX() + 1,
					barBgs.get(k).getY() + 1);
			
			k++;
			
			barBgs.add(new Sprite(atlas.findRegion(HEALTHBARBG_IMG)));
			barBgs.get(k).setPosition(mainbuttonbg.getWidth() * 0.82f, 
					barBgs.get(k-1).getY() - barBgs.get(k).getHeight());
			
			manaBarfills.add(new Sprite(atlas.findRegion(MANABARFILL_IMG)));
			manaBarfills.get(i).setPosition(barBgs.get(k).getX() + 1,
					barBgs.get(k).getY() + 1);
			
			k++;
		}
	}
	
	private void resetButtonClickable() {
		for(Button b : mainButtons) {
			b.setClickable(false);
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
	
	private void checkIfNoMoreItems(Button b2) {
		if(b2.getItem().getCount() == 0) {	//If items count is 0, reset item button line
			itemButtons.removeValue(b2, true);
			
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
			
			for(int i=0; i<itemButtons.size; i++) {
				itemButtons.get(i).setX(mainButtons.get(2).getWidth()/2); 
				itemButtons.get(i).setY((itemButtons.size - i) * subBtop[0].getRegionHeight() + mainButtons.get(2).getY()/itemButtons.size); 
				
				if(i == 0 && itemButtons.size > 1) {
					itemButtons.get(i).setTextureRegion(subBtop);
				} else if(i < itemButtons.size-1 || itemButtons.size == 1){
					itemButtons.get(i).setTextureRegion(subBmid);
				} else {
					itemButtons.get(i).setTextureRegion(subBbot);
				}
			}
		}
	}
	
	private void checkIfTargetClicked() {
		if(Gdx.input.justTouched()) {
			for(Character e : enemies) {
				if(Gdx.input.getX() > e.getX() 
					&& Gdx.input.getX() < e.getX() + e.getTextureRegion().getRegionWidth() 
					&& Game.HEIGHT - Gdx.input.getY() > e.getY() 
					&& Game.HEIGHT - Gdx.input.getY() < e.getY() + e.getTextureRegion().getRegionHeight()
					&& e.isAlive()) {
						target = e;
						System.out.println("Target set: "+ e.getClass().getSimpleName());
				}
			}
		}		
	}
	
	private void resetCharacters() {
		playerCharacters.get(MAGE).setXY(oldMagePosition.x, oldMagePosition.y);
		playerCharacters.get(WARRIOR).setXY(oldWarriorPosition.x, oldWarriorPosition.y);
		playerCharacters.get(ROGUE).setXY(oldRoguePosition.x, oldRoguePosition.y);
		for(Character c : playerCharacters) {
			c.setAttackCharge(0);
			c.setAttacking(false);
		}
	}

	@Override
	public void resize(int w, int h) {
		// TODO Auto-generated method stub
		
	}
}
