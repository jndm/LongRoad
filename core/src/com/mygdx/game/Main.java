package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainmenuScreen;

public class Main extends Game {
	
	@Override
	public void create () {
		//AssetManager.load();
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
	}
}
