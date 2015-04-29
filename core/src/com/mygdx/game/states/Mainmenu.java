package com.mygdx.game.states;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Button;

public class Mainmenu extends GameState {
	
	private TextureAtlas atlas;
	
	private Sprite bixbox;
	private Sprite smallbox;
	
	private Array<Button> sideButtons;

	private final String MENUATLAS = "menu/menuassets.pack";
	//Final variables for backgrounds
	private final String BACKGROUND_IMG 					= "background";
	private final String SIDEBOX_IMG						= "bigbox";
	private final String SMALLBOX_IMG						= "smallbox";
	
	//Final variables for buttons
	private final String SIDEBUTTON_IMG 					= "sideButton";
	private final String SIDEBUTTON_CLICKED_IMG				= "sideButtonClicked";
	private final String SIDEBUTTON_NOT_CLICKABLE_IMG		= "sideButtonNotClickable";
	
		
	protected Mainmenu(GameStateManager gsm) {
		super(gsm);
	}
	
	public Mainmenu(GameStateManager gsm, Array<Character> chars, Array<Item> items) {
		super(gsm);	
		assets.load(MENUATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(MENUATLAS);
		initSideButtons();
	}

	@Override
	public void update(float dt) {
		
	}

	@Override
	public void render() {
		
	}

	@Override
	public void dispose() {
		
	}
	
	private void initSideButtons() {
		sideButtons = new Array<Button>();
		TextureRegion[] sideButtonsRegion = new TextureRegion[3];
		sideButtonsRegion[0] = atlas.findRegion(SIDEBUTTON_IMG);
		sideButtonsRegion[1] = atlas.findRegion(SIDEBUTTON_CLICKED_IMG);
		sideButtonsRegion[2] = atlas.findRegion(SIDEBUTTON_NOT_CLICKABLE_IMG);
		for(int i=0; i<4; i++) {
			
		}
	}

}
