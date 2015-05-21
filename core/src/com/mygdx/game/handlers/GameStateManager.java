package com.mygdx.game.handlers;

import java.util.Stack;

import oldstuff.OldBattle;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.states.Battle;
import com.mygdx.game.states.GameState;
import com.mygdx.game.states.MainMenu;
import com.mygdx.game.states.Menu;
import com.mygdx.game.states.Play;

public class GameStateManager {
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int MAINMENU = 0;
	public static final int PLAY = 1;
	public static final int BATTLE = 2;
	public static final int MENU = 3;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushMainMenuState();
	}

	public Game game() { return game; }
	
	//Updating
	public void update(float dt) { gameStates.peek().update(dt); }
	//Rendering
	public void render() { gameStates.peek().render(); }
	//Resizing
	public void resize(int w, int h) { gameStates.peek().resize(w, h); }
	
	public void pushMainMenuState() {
		gameStates.push(new MainMenu(this));
	}
	public void pushBattleState(Battle battle) {
		gameStates.push(battle); 
	}
	
	public void pushMenuState(int state) {
		gameStates.push(new Menu(this)); 
	}
	
	public void pushPlayState() {
		gameStates.push(new Play(this));
	}
	
	public void returnMainMenuState() {
		while(!gameStates.isEmpty()){
			popState();
		};
		pushMainMenuState();
	}
	
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
}