package com.mygdx.game.helpers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.elements.characters.Character;
import com.mygdx.game.elements.characters.Party;
import com.mygdx.game.elements.items.Potion;
import com.mygdx.game.states.Menu;
import com.mygdx.game.states.Play;

public class InventoryTable {
	
	private Menu menu;
	private Array<Character> playerCharacters;
	private ScrollPane all_content;
	private Table table;
	
	public InventoryTable(Menu menu, Array<Character> playerChars) {
		this.menu = menu;
		this.playerCharacters = playerChars;
		table = createTable();
	}
	
	public Table getInventoryTable() {
		if(table == null) {
			table = createTable();
		} 
		return table;
	}
	
	private Table createTable() {
		Table masterInventoryTable = new Table(menu.getSkin());
		// Create the tab buttons
	    Table group = new Table(menu.getSkin());      
	    final Button all_tab = new TextButton("All", menu.getSkin(), "inventorytabbutton");
	    final Button weapons_tab = new TextButton("Weapons", menu.getSkin(), "inventorytabbutton");
	    final Button armors_tab	= new TextButton("Armors", menu.getSkin(), "inventorytabbutton");
	    final Button potions_tab = new TextButton("Potions", menu.getSkin(), "inventorytabbutton");
	    group.row().width(137.5f);
	    group.add(all_tab);
	    group.add(weapons_tab);
	    group.add(armors_tab);
	    group.add(potions_tab);
	    masterInventoryTable.add(group);
	    masterInventoryTable.row();
	    
	    // Create the tab content.
	    Stack content = new Stack();
	    //Create item table
	    final Table temp_table = new Table(menu.getSkin());
	    temp_table.add().colspan(6).height(5).top();
	    temp_table.row(); 
	    
	    //Create button group for item buttons
	    ButtonGroup<Button> toggle = new ButtonGroup<Button>();
	    toggle.setMinCheckCount(0);
	    toggle.setMaxCheckCount(1);
	    toggle.setUncheckLast(true);
	    
	    //Create item buttons
	    for(int i=0; i<Party.items.size; i++) {
	    	if(i>1 && i%2==0) {
	    		temp_table.row();
	    	}
	    	temp_table.add().width(50).height(50).top();	//Spot for icon 
	    	final TextButton tmp = new TextButton(Party.items.get(i).toString()+" x"+Party.items.get(i).getCount(), menu.getSkin(), "itembutton"); //Button with item name
	    	//final TextButton tmp = new TextButton("asdasdasd", menu.getSkin(), "itembutton");
	    	tmp.addListener(new ItemClickListener(new Potion(), menu, tmp, playerCharacters)); //Add custom listener to item button
	    	toggle.add(tmp);
	    	temp_table.add(tmp).spaceRight(5).width(220).height(50);
	    }
	    temp_table.row();
	    temp_table.add().colspan(4).expand().fill();	//Add extra row to fill table in case there is not enough items
	    temp_table.debug();
	    all_content = new ScrollPane(temp_table);	//Add make item table scrollable
	    all_content.setScrollingDisabled(true, false);
	    
	    final Table weapons_content = new Table(menu.getSkin());
	    final Table armors_content = new Table(menu.getSkin()); 
	    final Table potions_content = new Table(menu.getSkin());
	    content.addActor(all_content);
	    content.addActor(weapons_content);
	    content.addActor(armors_content);
	    content.addActor(potions_content);
	
	    masterInventoryTable.add(content).expand().fill().top();
	
	    // Listen to changes in the tab button checked states
	    // Set visibility of the tab content to match the checked state
	    ChangeListener tab_listener = new ChangeListener(){
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	    		menu.clearExtraStuffFromStage();
	        	all_content.setVisible(all_tab.isChecked());
	        	weapons_content.setVisible(weapons_tab.isChecked());
	        	armors_content.setVisible(armors_tab.isChecked());
	        	potions_content.setVisible(potions_tab.isChecked());
	        }
	    };
	    
	    all_tab.addListener(tab_listener);
	    weapons_tab.addListener(tab_listener);
	    armors_tab.addListener(tab_listener);
	    potions_tab.addListener(tab_listener); 	
	    
	    // Let only one tab button be checked at a times
	    ButtonGroup<Button> tabs = new ButtonGroup<Button>(all_tab, weapons_tab, armors_tab, potions_tab);
	    tabs.setChecked("All");
	    tabs.setMinCheckCount(1);
	    tabs.setMaxCheckCount(1);
	    //masterInventoryTable.debug();
	    return masterInventoryTable;
	}
	
}
