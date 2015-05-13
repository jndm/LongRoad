package com.mygdx.game.helpers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.items.Item;
import com.mygdx.game.states.Menu;

public class ItemClickListener extends ClickListener{
	private Item item;
	private Menu menu;
	private TextButton tb;
	private Array<Character> playerCharacters;
	
	public ItemClickListener(Item item, Menu menu, TextButton tb, Array<Character> playerCharacters) {
		this.item = item;
		this.menu = menu;
		this.tb = tb;
		this.playerCharacters = playerCharacters;
	}
	
	public void clicked(InputEvent event, float x, float y) {
		if(tb.isChecked()) {
			showInnerTable();
		} else {
			menu.clearExtraStuffFromStage();
		}
	}

	private void showInnerTable() {
		menu.clearExtraStuffFromStage();
		//Create table
		final Table table = new Table(menu.getSkin());
		table.columnDefaults(0).width(100).height(40);
		
		//Set table position right
		Vector2 pos = tb.localToStageCoordinates(new Vector2(tb.getX(),tb.getY()));
		table.setBounds(pos.x - tb.getX() + tb.getWidth() * 0.5f, pos.y - tb.getY(), 100, 40*3);
		
		//Create buttons
		final TextButton use = new TextButton("Use", menu.getSkin(), "itemsmallbutton");
		use.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if(use.isChecked()) {
					showChooseCharacterTable(table);	//Show character selection table 
				} else {
					menu.getStage().getActors().removeIndex(2);	// Remove character selection table
				}
			};
		});
		
		TextButton desc = new TextButton("Description", menu.getSkin(), "itemsmallbutton");
		desc.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				popUpDescriptionNote();
			};
		});
		
		TextButton delete = new TextButton("Delete", menu.getSkin(), "itemsmallbutton");
		delete.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				popUpDeleteNote();
			};
		});
		
		ButtonGroup<Button> bg = new ButtonGroup<Button>(use, desc, delete);
		bg.setMaxCheckCount(1);
		bg.setMinCheckCount(0);
		bg.setUncheckLast(true);
		
		//Add items to table
		table.add(use);
		table.row();
		table.add(desc);
		table.row();
		table.add(delete);

		table.debug();
		menu.getStage().addActor(table);
	}

	protected void showChooseCharacterTable(Table t) {
		//Table for buttons
		final Table table = new Table(menu.getSkin());
		table.columnDefaults(0).width(100).height(40);
		
		//Create buttons 
		for(final Character c : playerCharacters) {
			final TextButton button = new TextButton(c.getName(), menu.getSkin(), "itemsmallbutton");
			button.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					item.use(c);
					menu.clearExtraStuffFromStage();
					tb.setChecked(false);
				};
			});
			table.add(button);
			table.row();
		}
				
		//Set table position correctly
		//Vector2 pos = t.localToStageCoordinates(new Vector2(t.getX(), t.getY()));
		table.setBounds(t.getX() - t.getWidth(), t.getY() + t.getHeight()*0.2f, 100, 3*40);
		System.out.println("x: "+t.getX()+" y: "+t.getY()+"\npos x:"+t.getWidth()+" pos y:"+t.getHeight());
		
		table.debug();
		menu.getStage().addActor(table);
	}

	protected void popUpDescriptionNote() {
		System.out.println("Description pop-up!");
	}

	protected void popUpDeleteNote() {
		System.out.println("Delete pop-up!");
	}
}
