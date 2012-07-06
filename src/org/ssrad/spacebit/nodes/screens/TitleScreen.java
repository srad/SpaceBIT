package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.game.Game;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.ui.Picture;

/**
 * Title screen node.
 * 
 * @author Saman Sedighi Rad
 *
 */
public class TitleScreen extends AbstractScreen implements ActionListener {
	
	Picture background;
	boolean active = true;

	public TitleScreen(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {
		super.init();
		
		background = new Picture("Test");
		
		background.setImage(game.getAssetManager(), "title-screen2.png", true);		
		background.setWidth(game.getSettings().getWidth());
		background.setHeight(game.getSettings().getHeight());
		
		background.setPosition(0, 0);
		game.getGuiNode().attachChild(background);
	}
	
	public void update(float tpf) {
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	protected void bindKeys() {
		// You can map one or several inputs to one named action
		inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
		
		inputManager.addMapping("level_1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("level_2", new KeyTrigger(KeyInput.KEY_2));
		
		inputManager.addMapping("quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(this, new String[] { "pause", "level_1", "level_2", "quit" });
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {

		if (!name.equals("quit") && !keyPressed && !game.isLaunched()) {
			game.init();
		}

		if ( (name.equals("level_1") || name.equals("level_2")) && !keyPressed ) {
			game.setLevel(name.equals("level_1") ? GameLevel.LEVEL_ONE : GameLevel.LEVEL_TWO);
			game.load();
		}

		if (name.equals("quit") && !keyPressed) {
			game.stop();
		}
		
		if (name.equals("pause") && !keyPressed) {
			if (game.isRunning()) {
				game.pause();
			} else {
				game.run();
			}
		}
	
	}

}
