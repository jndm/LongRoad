package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Party;
import com.mygdx.game.handlers.GameStateManager;

public class MainMenu extends GameState{

	private Party party;
	private Stage stage;
	private Skin skin;
	private Table mastertable;
	private TextureAtlas atlas;
	
	private final String MAINMENUATLAS = "mainmenu/mainmenuassets.pack";
	
	public MainMenu(final GameStateManager gsm) {
		super(gsm);
		assets.load(MAINMENUATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(MAINMENUATLAS);
		
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("mainmenu/mainmenuSkin.json"), atlas);
		
		mastertable = new Table(skin);
		mastertable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		TextButton playButton = new TextButton("Play", skin, "button");
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gsm.pushPlayState();
			}
		});
		mastertable.add(playButton).width(Game.VIRTUAL_WIDTH * 0.1875f);
		stage.addActor(mastertable);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void update(float dt) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int w, int h) {
		stage.getViewport().update(w, h, true);
		viewport.update(w, h, true);
		viewport2.update(w, h, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
