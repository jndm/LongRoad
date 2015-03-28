package com.mygdx.game.elements;

import com.badlogic.gdx.utils.Array;

public abstract class Character {
	
	private Array<Equipment> equipments;
	
	public abstract void attack();
	public abstract void cast();
	public abstract void addEquipment(Equipment equipment);
}
