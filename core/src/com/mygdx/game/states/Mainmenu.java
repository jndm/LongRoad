package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.helpers.Button;
import com.mygdx.game.helpers.MainmenuButtonActions.SideButtonAction;

public class Mainmenu extends GameState {

	private TextureAtlas atlas;
	
	private Sprite bigbox;
	private Sprite smallbox;
	private Sprite bg;
	
	private Array<Button> sideButtons;

	private final String MENUATLAS 							= "menu/menuassets.pack";
	//Final variables for backgrounds
	private final String BACKGROUND_IMG 					= "background";
	private final String BIGBOX_IMG							= "bigbox";
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
		bg = new Sprite(atlas.findRegion(BACKGROUND_IMG));
		bg.setPosition(0, 0);
		bigbox = new Sprite(atlas.findRegion(BIGBOX_IMG));
		bigbox.setPosition(Game.WIDTH * 0.25f, Game.HEIGHT * 0.08f);
		smallbox = new Sprite(atlas.findRegion(SMALLBOX_IMG));
		smallbox.setPosition(Game.WIDTH * 0.025f, Game.HEIGHT * 0.08f);
		initSideButtons();
	}

	@Override
	public void update(float dt) {
		checkIfButtonClicked();
	}

	private void checkIfButtonClicked() {
		if(Gdx.input.justTouched()) {
			for(Button b : sideButtons) {
				if(b.isMouseOnButton(Gdx.input.getX(), Gdx.input.getY())) {
					System.out.println("Clicked: "+b.getSideButtonAction().toString());
				}
			}
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(hudCam.combined);
		
		sb.begin();
			bg.draw(sb);
			bigbox.draw(sb);
			smallbox.draw(sb);
		sb.end();
		
		for(Button b : sideButtons) {
			b.render(sb);
		}
	}

	@Override
	public void dispose() {
		
	}
	
	private void initSideButtons() {
		sideButtons = new Array<Button>();
		
		Texture fonttexture = new Texture(Gdx.files.internal("fonts/subButton.png"));
		fonttexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		BitmapFont sidefont = new BitmapFont(Gdx.files.internal("fonts/subButton.fnt"), new TextureRegion(fonttexture), false);	
		
		TextureRegion[] sideButtonsRegion = new TextureRegion[3];
		sideButtonsRegion[0] = atlas.findRegion(SIDEBUTTON_IMG);
		sideButtonsRegion[1] = atlas.findRegion(SIDEBUTTON_CLICKED_IMG);
		sideButtonsRegion[2] = atlas.findRegion(SIDEBUTTON_NOT_CLICKABLE_IMG);
		
		for(int i=0; i<4; i++) {
			sideButtons.add(new Button((int)(Game.WIDTH * 0.025),
					(int) (Game.HEIGHT * 0.92) - sideButtonsRegion[0].getRegionHeight() * (i+1),
					sideButtonsRegion[0].getRegionWidth(),
					sideButtonsRegion[0].getRegionHeight(),
					SideButtonAction.values()[i].toString(),
					SideButtonAction.values()[i],
					sidefont));
			sideButtons.get(i).setTexture(sideButtonsRegion);
		}
	}
}
