package com.mygdx.game.states;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Mage;
import com.mygdx.game.elements.characters.Party;
import com.mygdx.game.elements.characters.Rogue;
import com.mygdx.game.elements.characters.Warrior;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.skills.Skill;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.SkillTextButton;

public class Battle extends GameState {

	private Sprite pointer;
	
	private Array<Character> enemies;
	private Array<Character> chars;
	private Array<Item> items;
	private Array<Character> turnQueue;
	private Skill activeAction;
	private Character target;
	private Character enemyTarget;
	private Vector2 oldMagePosition, oldWarriorPosition, oldRoguePosition;
	
	//Needed for playing action
	private final float ACTIONMOVEMAX = Game.WIDTH * 0.375f;
	private boolean steppedForvard = false;
	private boolean actionFinished = false;
	private int deadPlayerCharacters = 0;
	private int deadEnemies = 0;

	//For charging
	private final float WAIT_TIME = 1f;
	private float timeElapsed = 0;
	
	private TextureAtlas atlas;
	private Stage stage;
	private Skin skin;
	
	private Table mastertable, mainButtonTable, sideTable;
	private Array<Table> attackTables;
	private Array<Table> castTables;
	private ButtonGroup<TextButton> mainbuttongroup;
	private Array<ButtonGroup<TextButton>> attackbuttongroup;
	private Array<ButtonGroup<TextButton>> castbuttongroup;
	
	private final int MAGE = 0;
	private final int WARRIOR = 1;
	private final int ROGUE = 2;
	
	private final String BATTLEATLAS = "battle/battleassets.pack";
	private BitmapFont fpsfont = new BitmapFont();
	
	protected Battle(GameStateManager gsm) {
		super(gsm);
	}

	public Battle(GameStateManager gsm, Array<Character> enemies, Party party) {
		super(gsm);
		this.enemies = enemies;
		this.chars = party.getCharacters();
		
		assets.load(BATTLEATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(BATTLEATLAS);
		
		//Setup bottom table
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("battle/battleSkin.json"), atlas);

		mastertable = new Table(skin);
		mastertable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mastertable.setPosition(0, 0);
		
		initMainTable();
		initSideTable();
		initAttackTables();
		initCastTables();
		
		for(Character c : chars) {
			checkManaLeftAndDisableButtons(c);
		}
		mastertable.add(new Image(atlas.findRegion("background"))).colspan(2).fill().expand();
		mastertable.row();
		mastertable.add(mainButtonTable).left();
		mastertable.add().fillX().expandX();
		mastertable.debug();
		stage.addActor(mastertable);
		
		Gdx.input.setInputProcessor(stage);	
		stage.addActor(mastertable);
		
		//Setup other stuff
		initEnemies();
		target = this.enemies.first();
		turnQueue = new Array<Character>();
		pointer = new Sprite(atlas.findRegion("pointer"));
		pointer.setPosition(target.getX() + target.getTextureRegion().getRegionWidth()/2 - pointer.getWidth()/2, 
				target.getY() + target.getTextureRegion().getRegionHeight());
		
		oldMagePosition 	= new Vector2(chars.get(MAGE).getX(), chars.get(MAGE).getY());
		oldWarriorPosition 	= new Vector2(chars.get(WARRIOR).getX(), chars.get(WARRIOR).getY());
		oldRoguePosition 	= new Vector2(chars.get(ROGUE).getX(), chars.get(ROGUE).getY());
		
		chars.get(MAGE).setXY(chars.get(MAGE).getBattleposition().x, chars.get(MAGE).getBattleposition().y);
		chars.get(WARRIOR).setXY(chars.get(WARRIOR).getBattleposition().x, chars.get(WARRIOR).getBattleposition().y);
		chars.get(ROGUE).setXY(chars.get(ROGUE).getBattleposition().x, chars.get(ROGUE).getBattleposition().y);	
	}
	
	@Override
	public void update(float dt) {
		
		for(Character c : chars) {
			checkManaLeftAndDisableButtons(c);
		}
		
		if(activeAction == null) { //If there is no action to be shown:
			if(turnQueue.size == 0) {	// If no-one has turn, charge it
				setMainButtonsDisabled(true);
				countTurn(dt);
				turnQueue.shrink(); //For memory management
			} else {
				if(!turnQueue.first().isAlive()) {	//check if character is alive because it may have charged before it died
					turnQueue.removeIndex(0);
				} else { //Set all main buttons disabled or enabled depending on whose turn it is			
					if(isPlayersTurn()) {
						setMainButtonsDisabled(false);
					} else {
						setMainButtonsDisabled(true);
						handleAi();
					}
				}
			}
		}	
		else { //If there is an action to be shown wait for it to finish:
			setMainButtonsDisabled(true);
			if(isPlayersTurn()) {
				doActivePlayerAction(dt);
			} 
			else {
				doActiveEnemyAction(dt);
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
				if(deadPlayerCharacters == chars.size) {	//If all characters are dead 
					System.out.println("YOU ARE DEAD!");
					gsm.popState();
				}
			}
		}
		
	}

	private void handleAi() {
		enemyTarget = turnQueue.first().raffleTarget(chars);
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

	private void checkManaLeftAndDisableButtons(Character c) {
		int index = 10;
		if(c instanceof Warrior) {
			index = WARRIOR;
		} else if(c instanceof Rogue) {
			index = ROGUE;
		} else if(c instanceof Mage) {
			index = MAGE;
		}
		if(index != 10) {
			for(TextButton b : attackbuttongroup.get(index).getButtons()) {
				((SkillTextButton) b).setDisabledIfNotEnoughMana(c);
			}
			
			for(TextButton b : castbuttongroup.get(index).getButtons()) {
				((SkillTextButton) b).setDisabledIfNotEnoughMana(c);
			}
		}
	}
	
	private void setMainButtonsDisabled(boolean b) {
		for(TextButton tb : mainbuttongroup.getButtons()) {
			tb.setDisabled(b);
		}
	}

	private boolean isPlayersTurn() {
		if(turnQueue.first() instanceof Warrior ||
				 turnQueue.first() instanceof Rogue ||
				 turnQueue.first() instanceof Mage) {
			return true;
		} else {
			return false;
		}
	}

	private void countTurn(float dt) {
		timeElapsed += dt;
		if(WAIT_TIME <= timeElapsed) {
			for(Character c : chars) {
				if(c.isAlive()) {
					c.addCharge();
					if(c.isCharged()) {
						turnQueue.add(c);
						System.out.println(c.getName()+" charged!");
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		sb.setProjectionMatrix(stage.getCamera().combined);
		stage.getViewport().apply();
		stage.act();
		stage.draw();
		
		sb.setProjectionMatrix(hudCam.combined);
		viewport2.apply();
		
		if(activeAction == null) {
			sb.begin();
				pointer.draw(sb);
			sb.end();
		}
		
		//Characters
		for(Character c : chars) {
			c.render(sb);
		}
		
		for(Character c : enemies) {
			c.render(sb);
		}

		if(activeAction != null) {
			if(isPlayersTurn()) {
				activeAction.render(sb, target);
			}else {
				activeAction.render(sb, enemyTarget);
			}
		}
		sb.begin();
			fpsfont.draw(sb, "FPS: "+Gdx.graphics.getFramesPerSecond(), 730, 470);
		sb.end();
	}
	
	private void swapTarget() {
		for(int i=0; i<enemies.size; i++) {
			if(enemies.get(i).isAlive()) {
				target = enemies.get(i);
				pointer.setPosition(target.getX() + target.getTextureRegion().getRegionWidth()/2 - pointer.getWidth()/2, 
						target.getY() + target.getTextureRegion().getRegionHeight());
				break;
			}
		}
	}

	@Override
	public void resize(int w, int h) {
		stage.getViewport().update(w, h, true);
		viewport2.update(w, h, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		assets.unload(BATTLEATLAS);
		resetCharacters();
	}
	
	private void resetCharacters() {
		chars.get(MAGE).setXY(oldMagePosition.x, oldMagePosition.y);
		chars.get(WARRIOR).setXY(oldWarriorPosition.x, oldWarriorPosition.y);
		chars.get(ROGUE).setXY(oldRoguePosition.x, oldRoguePosition.y);
		for(Character c : chars) {
			c.setAttackCharge(0);
			c.setAttacking(false);
		}
	}
	
	
	/* INITING BATTLE VARIABLES START */
	
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
	
	private void initAttackTables() {
		attackTables = new Array<Table>();
		attackbuttongroup = new Array<ButtonGroup<TextButton>>();
		
		for(final Character c : chars) {
			Table table = new Table(skin);
			ButtonGroup<TextButton> bg = new ButtonGroup<TextButton>();
			bg.setMinCheckCount(0);
			int i = 0;
			for(final Skill s : c.getAttackAbilities()) { //Start of inner for
				TextButton tb = null;
				if( i == 0 && c.getAttackAbilities().size > 1) {
					tb = new SkillTextButton(s.toString(), skin, "subbuttontop", s);				
				} else if( i == c.getAttackAbilities().size-1 && c.getSpells().size > 1) {
					tb = new SkillTextButton(s.toString(), skin, "subbuttonbot", s);
				} else {
					tb = new SkillTextButton(s.toString(), skin, "subbuttonmid", s);			
				} 
				tb.addListener(new SkillClickListener(tb, s, c));
				bg.add(tb);
				table.add(tb).width(mainButtonTable.getCells().get(0).getPrefWidth() * 0.5f).height(mainButtonTable.getCells().get(0).getPrefHeight() * 0.9f);
				table.row();
				i++;
			} //end of inner for
			attackbuttongroup.add(bg);
			attackTables.add(table);
		}// end of for
	}

	private void initCastTables() {
		castTables = new Array<Table>();
		castbuttongroup = new Array<ButtonGroup<TextButton>>();
		
		for(final Character c : chars) {
			Table table = new Table(skin);
			ButtonGroup<TextButton> bg = new ButtonGroup<TextButton>();
			bg.setMinCheckCount(0);
			int i = 0;
			for(final Skill s : c.getSpells()) { //Start of inner for
				TextButton tb = null;
				if( i == 0 && c.getSpells().size > 1) {
					tb = new SkillTextButton(s.toString(), skin, "subbuttontop", s);				
				} else if( i == c.getSpells().size-1 && c.getSpells().size > 1) {
					tb = new SkillTextButton(s.toString(), skin, "subbuttonbot", s);
				} else {
					tb = new SkillTextButton(s.toString(), skin, "subbuttonmid", s);			
				} 
				tb.addListener(new SkillClickListener(tb, s, c));
				bg.add(tb);
				table.add(tb).width(mainButtonTable.getCells().get(0).getPrefWidth() * 0.5f).height(mainButtonTable.getCells().get(0).getPrefHeight() * 0.9f);
				table.row();
				i++;
			} //end of inner for
			castbuttongroup.add(bg);
			castTables.add(table);
		}// end of for
	}
	/* Listener for subbuttons */
	public class SkillClickListener extends ClickListener {
		
		private TextButton tb;
		private Skill s;
		private Character c;
		
		public SkillClickListener(TextButton tb, Skill s, Character c) {
			this.tb = tb;
			this.s = s;
			this.c = c;
		}
		
		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("ASDASDA");
			if(!tb.isDisabled()) {
				activeAction = s;
				hideExtraTables();
				setCharactersButtonsUnchecked(c);
				System.out.println("ASDASDA");
			}
		}
	}
	
	protected void setCharactersButtonsUnchecked(Character c) {
		int index = 10;
		if(c instanceof Warrior) {
			index = WARRIOR;
		} else if(c instanceof Rogue) {
			index = ROGUE;
		} else if(c instanceof Mage) {
			index = MAGE;
		}
		if(index != 10) {
			if(mainbuttongroup.getChecked() != null) {
				mainbuttongroup.getChecked().setChecked(false);
			}
			
			if(castbuttongroup.get(index).getChecked() != null) {
				castbuttongroup.get(index).getChecked().setChecked(false);
			}
			
			if(attackbuttongroup.get(index).getChecked() != null) {
				attackbuttongroup.get(index).getChecked().setChecked(false);
			}
		}
	}
	
	private void initMainTable() {
		mainButtonTable = new Table(skin);
		
		//Creating attack button:
		final TextButton attackb = new TextButton("Attack", skin, "mainbutton");
		attackb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!attackb.isDisabled()) {		//check if disabled
					if(attackb.isChecked()) {	//check if click made button checked
						hideExtraTables();
						if(turnQueue.first() instanceof Warrior) {
							showAttackTable(attackTables.get(WARRIOR));
						} else if(turnQueue.first() instanceof Rogue) {
							showAttackTable(attackTables.get(ROGUE));
						} else if(turnQueue.first() instanceof Mage) {
							showAttackTable(attackTables.get(MAGE));
						}
					} else {
						hideExtraTables();
					}
				}
			}
		});
		//Creating cast button:
		final TextButton castb = new TextButton("Cast", skin, "mainbutton");	
		castb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!castb.isDisabled()) {    //check if disabled                 
					if(castb.isChecked()) {  //check if click made button checked
						hideExtraTables();
						if(turnQueue.first() instanceof Warrior) {
							showCastTable(castTables.get(WARRIOR));
						} else if(turnQueue.first() instanceof Rogue) {
							showCastTable(castTables.get(ROGUE));
						} else if(turnQueue.first() instanceof Mage) {
							showCastTable(castTables.get(MAGE));
						}
					} else {
						hideExtraTables();
					}
				}
			}
		});
		//Creating item button:
		final TextButton itemb = new TextButton("Item", skin, "mainbutton");
		itemb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!itemb.isDisabled()) {
					
				}
			}
		});
		//Creating run button:
		final TextButton runb = new TextButton("Run", skin, "mainbutton");
		runb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!runb.isDisabled()) {
					gsm.popState();
				}
			}
		});
		
		//Buttongroup for mainbuttons
		mainbuttongroup = new ButtonGroup<TextButton>(attackb, castb, itemb, runb);
		mainbuttongroup.setMaxCheckCount(1);
		mainbuttongroup.setMinCheckCount(0);
		mainbuttongroup.setUncheckLast(true);
		
		mainButtonTable.columnDefaults(0).width(mastertable.getWidth() * 0.41f).height(mastertable.getHeight() * 0.42f * 0.251f).top();
		mainButtonTable.add(attackb);
		mainButtonTable.row();
		mainButtonTable.add(castb);
		mainButtonTable.row();
		mainButtonTable.add(itemb);
		mainButtonTable.row();
		mainButtonTable.add(runb);	
	}
	

	protected void hideExtraTables() {
		if(stage.getActors().size > 1) {
			Iterator<Actor> i = stage.getActors().iterator();
			i.next();
			while (i.hasNext()) {
			   Actor s = i.next();
			   i.remove();
			}
		}
		stage.getActors().shrink();
	}
	
	protected void showAttackTable(Table t) {
		float tableHeight = 0;
		for(Cell c : t.getCells()) {
			tableHeight += c.getMinHeight();
		}
		Vector2 pos = mainbuttongroup.getChecked().localToStageCoordinates(new Vector2(0,0));
		t.setPosition(pos.x + mainbuttongroup.getChecked().getWidth(), pos.y + tableHeight/4);
		System.out.println("pos x:" +(pos.x + mainbuttongroup.getChecked().getWidth())+ " pos y:"+(pos.y + tableHeight));
		stage.addActor(t);
	}

	protected void showCastTable(Table t) {
		float tableHeight = 0;
		for(Cell c : t.getCells()) {
			tableHeight += c.getMinHeight();
		}
		Vector2 pos = mainbuttongroup.getChecked().localToStageCoordinates(new Vector2(0,0));
		t.setPosition(pos.x + mainbuttongroup.getChecked().getWidth(), pos.y + tableHeight/4);
		System.out.println("pos x:" +mainbuttongroup.getChecked().getX()+ " pos y:"+tableHeight );
		stage.addActor(t);
	}
	
	private void initSideTable() {
		
	}
}
