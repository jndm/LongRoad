package com.mygdx.game.handlers;

import java.util.Stack;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Party;
import com.mygdx.game.states.Battle;
import com.mygdx.game.states.GameState;
import com.mygdx.game.states.Menu;
import com.mygdx.game.states.OldBattle;
import com.mygdx.game.states.Play;

public class GameStateManager {
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 1;
	public static final int BATTLE = 2;
	public static final int MENU = 3;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(PLAY);
	}
	
	public Game game() { return game; }
	
	public void update(float dt) {
		gameStates.peek().update(dt);
	}
	
	public void render() {
		gameStates.peek().render();
	}
	
	private GameState getState(int state) {
		if(state == PLAY) return new Play(this);
		if(state == BATTLE) return new OldBattle(this);
		return null;
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushBattleState(int state, Array<Character> array, Party party) {
		gameStates.push(new Battle(this, array, party)); 
	}
	
	public void pushMainMenuState(int state, Party party) {
		gameStates.push(new Menu(this, party)); 
	}
	
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
	
	public GameState peekState() {
		return gameStates.peek();
	}
	
	public void resize(int w, int h) {
		gameStates.peek().resize(w, h);
	}
}