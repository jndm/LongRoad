package com.mygdx.game.handlers;

import java.util.Stack;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game;
import com.mygdx.game.states.Battle;
import com.mygdx.game.states.GameState;
import com.mygdx.game.states.Play;
import com.mygdx.game.elements.Enemy;
import com.mygdx.game.elements.Character;

public class GameStateManager {
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 1;
	public static final int BATTLE = 2;
	
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
		if(state == BATTLE) return new Battle(this);
		return null;
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushBattleState(int state, Array<Enemy> enemies, Array<Character> chars) {
		gameStates.push(new Battle(this, enemies, chars)); 
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
	
}