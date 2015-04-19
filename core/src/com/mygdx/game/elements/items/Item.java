package com.mygdx.game.elements.items;

import com.mygdx.game.elements.characters.Character;

public interface Item {
	public void use(Character user);
	public void addCount(int c);
	public int getCount();
	public String toString();
}
