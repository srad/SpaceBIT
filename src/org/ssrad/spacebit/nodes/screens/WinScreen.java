package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.game.Game;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.ui.Picture;

public class WinScreen extends AbstractScreen implements ActionListener {

	Picture background;
	float timer;

	public WinScreen(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		super.init();
		
		timer = 0f;
		
		background = new Picture("GameOver");
		
		background.setImage(game.getAssetManager(), "win.png", true);		
		background.setWidth(game.getSettings().getWidth());
		background.setHeight(game.getSettings().getHeight());
		
		background.setPosition(0, 0);
		game.getGuiNode().attachChild(background);
	}
	
	@Override
	protected void bindKeys() {	
		inputManager.addMapping("quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping("start", new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("start", new KeyTrigger(KeyInput.KEY_SPACE));
		
		inputManager.addListener(this, new String[] { "quit" });
	}
	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {

		if (name.equals("quit") && !keyPressed) {
			game.stop();
		}
		if (name.equals("start") && !keyPressed) {
			game.stop();
			game.getTitleScreen().show();
		}
	
	}
	
}
