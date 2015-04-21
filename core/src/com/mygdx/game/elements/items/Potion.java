package com.mygdx.game.elements.items;

import com.mygdx.game.elements.characters.Character;

public class Potion implements Item {
	private int effect = 10;
	private int count = 0;
	
	public Potion() {
		count += 1;
	}
	
	@Override
	public void use(Character user) {
		if(user.getHp() + effect < user.getMaxHp()) {
			user.setHp(user.getHp() + effect);
		} else {
			user.setHp(user.getMaxHp());
		}
		System.out.println("Potion used for "+user.getClass().getSimpleName());
		count -= 1;
	}
	
	@Override
	public void addCount(int c){
		count += c;
	}

	@Override
	public int getCount() {
		return count;
	}
	
	@Override
	public String toString() {
		return "Potion";
	}
}
