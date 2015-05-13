package com.mygdx.game.states;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.elements.items.Potion;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.InventoryTable;
import com.mygdx.game.helpers.ItemClickListener;

public class Menu extends GameState {

	private TextureAtlas atlas;
	
	private Array<Character> playerCharacters;
	private Stage stage;
	private Skin skin;
	private InventoryTable inventorytable;
	private Table mastertable, innertable, statustable, equipmenttable, talentstable;
	private TextButton statusButton, inventoryButton, equipmentButton, talentsButton;
	private Button exitButton;

	private final String MENUATLAS = "menu/menuassets.pack";
		
	protected Menu(GameStateManager gsm) {
		super(gsm);
	}
	
	public Menu(final GameStateManager gsm, Array<Character> chars, Array<Item> items) {
		super(gsm);	
		playerCharacters = chars;
		assets.load(MENUATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(MENUATLAS);
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("menu/menuSkin.json"), atlas);
		
		mastertable = new Table(skin);
		mastertable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		exitButton = new Button(skin.getDrawable("exitbutton"));
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gsm.popState();
			}
		});
		
		initStatusTable();
		initInventoryTable();
		initEquipmentTable();
		initTalentsTable();
		initSideButtons();
		initMasterTable();
		
		Gdx.input.setInputProcessor(stage);	
		stage.addActor(mastertable);
	}

	@Override
	public void update(float dt) {
		stage.act(dt);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(hudCam.combined);

		stage.draw();
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		assets.unload(MENUATLAS);
	}
	
	private void initTalentsTable() {
		talentstable = new Table();
		talentstable.add().width(550);
	}

	private void initEquipmentTable() {
		equipmenttable = new Table();
		equipmenttable.add().width(550);	
	}

	private void initInventoryTable() {
		inventorytable = new InventoryTable(this, playerCharacters);
	}
	
	private void initStatusTable() {
		statustable = new Table(skin);
		//Creating table for every playable character
		for(Character c : playerCharacters) {
			Table status = new Table(skin);
			status.setBackground("statusbg");
			status.add(new Label(c.getName(), skin, "statusboxheader")).top().left().width(250).spaceBottom(5);
			status.add(new Label("Level: ??", skin, "statusboxtext")).top().left().width(250).spaceBottom(5);
			status.row();
			status.add(new Label("Hp:"+c.getHp()+"/"+c.getMaxHp(), skin, "statusboxtext")).left();
			status.add(new Label("Str: "+c.getStrength(), skin, "statusboxtext")).left();
			status.row();
			status.add(new Label("Mana: "+c.getMana()+"/"+c.getMaxMana(), skin, "statusboxtext")).left();
			status.add(new Label("Agi: "+c.getAgility(), skin, "statusboxtext")).left();
			status.row();
			status.add(new Label("Experience: ??", skin, "statusboxtext")).left();
			status.add(new Label("Int: "+c.getIntelligence(), skin, "statusboxtext")).left();
			status.row();
			status.add();
			status.add(new Label("Attk.s: "+c.getAttackSpeed(), skin, "statusboxtext")).left();
			status.debug();
			statustable.add(status).top();
			statustable.row();
		}
	}
	
	private void initSideButtons() {
		statusButton = new TextButton("Status", skin, "sidebutton");
		statusButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeTable(statustable);
			}
		});
		
		inventoryButton = new TextButton("Inventory", skin, "sidebutton");
		inventoryButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeTable(inventorytable.getInventoryTable());
			}	
		});
		
		equipmentButton = new TextButton("Equipment", skin, "sidebutton");
		equipmentButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeTable(equipmenttable);
			}
		});
		
		talentsButton = new TextButton("Talents", skin, "sidebutton");
		talentsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeTable(talentstable);
			}			
		});
	}
	
	private void initMasterTable() {
		
		innertable = statustable; //Set statustable to be first innertable
		
		//Table for putting smallbox's text correctly
		Table smallbox = new Table(skin);
		smallbox.columnDefaults(0).width(155);
		smallbox.setBackground("smallbox");
		smallbox.add(new Label("Time played:\t??", skin, "smallboxtext")).left().top().padBottom(2);
		smallbox.row();
		smallbox.add(new Label("Waves cleared\nthis run:\t??", skin, "smallboxtext")).left().top().padBottom(2);
		smallbox.row();
		smallbox.add(new Label("Waves cleared\ntotal:\t??", skin, "smallboxtext")).left().top().padBottom(2);
		smallbox.row();
		smallbox.add(new Label("Lenght travelled:\t??", skin, "smallboxtext")).left().top().padBottom(2);
		smallbox.debug();
		
		//Creating a buttongroup of sidebuttons to make them togglable
		ButtonGroup<TextButton> rightButtonGroup = new ButtonGroup<TextButton>(statusButton, inventoryButton, equipmentButton, talentsButton);
		rightButtonGroup.setMaxCheckCount(1);
		rightButtonGroup.setMinCheckCount(1);
		rightButtonGroup.setUncheckLast(true);
		rightButtonGroup.setChecked("Status");
		
		//Table for putting sidebuttons on top of each other and into one cell of mastertable
		Table rightside = new Table(skin);
		rightside.add(statusButton);
		rightside.row();
		rightside.add(inventoryButton);
		rightside.row();
		rightside.add(equipmentButton);
		rightside.row();
		rightside.add(talentsButton);
		rightside.row();
		rightside.add(smallbox).spaceTop(9);
		
		//Setting up the master table
		mastertable.setBackground("background");
		mastertable.add();
		mastertable.add(exitButton).top().right().space(10);
		mastertable.row();
		mastertable.add(rightside).top().spaceRight(20);
		mastertable.add(innertable).width(550).height(399).fill();	
		
		mastertable.setDebug(false);
	}
	
	private void changeTable(Table t) {
		clearExtraStuffFromStage();
		Cell<Table> cell = mastertable.getCell(innertable);
		innertable = t;
		cell.setActor(innertable);
	}
	
	public void clearExtraStuffFromStage() {
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
	
	@Override
	public void resize(int w, int h) {
		System.out.println("Resizing: new w:"+w+" new h:"+h);
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Skin getSkin() {
		return skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
	}

}
