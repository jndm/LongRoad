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
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.handlers.Party;
import com.mygdx.game.helpers.Constants;

public class MainMenu extends GameState{

	private Stage stage;
	private Skin skin;
	private Table mastertable;
	private TextureAtlas atlas;
	private TextButton playButton;
	
	public MainMenu(final GameStateManager gsm) {
		super(gsm);
		assets.load(Constants.MAINMENUATLAS, TextureAtlas.class);
		assets.finishLoading();
		
		atlas = assets.get(Constants.MAINMENUATLAS);
		
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("mainmenu/mainmenuSkin.json"), atlas);
		
		mastertable = new Table(skin);
		mastertable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		playButton = new TextButton("Play", skin, "button");
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gsm.pushPlayState();
				System.out.println("cliiickd");
			}
		});
		mastertable.add(playButton).width(Game.VIRTUAL_WIDTH * 0.1875f);
		stage.addActor(mastertable);
	}

	@Override
	public void update(float dt) {
	}

	@Override
	public void render() {
		Gdx.input.setInputProcessor(stage);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(stage.getCamera().combined);
		stage.getViewport().apply();
		
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
