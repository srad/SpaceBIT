package org.ssrad.spacebit.nodes;

import org.ssrad.spacebit.game.Game;

import com.jme3.ui.Picture;

/**
 * Title screen node.
 * 
 * @author Saman Sedighi Rad
 *
 */
public class TitleScreen {
	
	Picture screenImage;
	Game game;
	boolean active = true;

	public TitleScreen(Game game) {
		this.game = game;
		init();
	}

	protected void init() {
		screenImage = new Picture("Test");
		
		screenImage.setImage(game.getAssetManager(), "title-screen2.png", true);		
		screenImage.setWidth(game.getSettings().getWidth());
		screenImage.setHeight(game.getSettings().getHeight());
		
		screenImage.setPosition(0, 0);
		game.getGuiNode().attachChild(screenImage);
	}
	
	public void update(float tpf) {
	}
	
	public void hide() {
		game.getGuiNode().detachChild(screenImage);
		active = false;
	}
	
	public void show() {
		game.getGuiNode().detachAllChildren();
		game.getGuiNode().attachChild(screenImage);
		active = true;
	}
	
	public boolean isActive() {
		return active;
	}

}
