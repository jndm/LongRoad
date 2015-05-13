package com.mygdx.game.elements.skills;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.elements.characters.Character;

public abstract class Skill {
	
	protected float elapsedTime = 9999;
	protected int dmg = 0;
	protected Animation animation;
	private BitmapFont font = new BitmapFont();
	private float fontx, fonty;
	protected float manareq;
	
	public abstract void act(Character actor, Character target);
	
	public void render(SpriteBatch sb, Character target) {
		if(!animation.isAnimationFinished(elapsedTime)) {	
			sb.begin();
			elapsedTime += Gdx.graphics.getDeltaTime();
			sb.draw(animation.getKeyFrame(elapsedTime, false), target.getX(), target.getY() + target.getTextureRegion().getRegionHeight()/3);
			font.draw(sb, dmg+"", fontx, fonty++);
			sb.end();
		}	
	}
	
	protected void startAnimation(Character target) {
		fontx = target.getX() + target.getTextureRegion().getRegionWidth()/2 - font.getBounds(dmg+"").width/2;
		fonty = target.getY() + target.getTextureRegion().getRegionHeight() * 0.8f;
		elapsedTime = 0;
	}
	
	public void createAnimation(TextureRegion[] tr) {
		animation = new Animation(1/3f, tr);
	}
	
	public boolean haveMana(Character c) {
		if(c.getMana() >= manareq) {
			return true;
		} else {
			return false;
		}
	}
}
