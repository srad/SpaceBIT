package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.game.Game;

import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;

public class GameOverScreen extends AbstractScreen implements ActionListener {

	Picture background;
	float timer;
	private BitmapText keys;
	
	public GameOverScreen(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		super.init();
		
		timer = 0f;
		
		background = new Picture("GameOver");
		
		background.setImage(game.getAssetManager(), "gameover.png", true);		
		background.setWidth(game.getSettings().getWidth());
		background.setHeight(game.getSettings().getHeight());
		
		background.setPosition(0, 0);
		game.getGuiNode().attachChild(background);
		
		// TEXT		
		keys = new BitmapText(game.getGuiFont(), false);          
		keys.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);
		keys.setColor(ColorRGBA.White);
		keys.setLocalTranslation(game.getSettings().getWidth() - 100, -5, 0.1f);

		attachChild(keys);
	}
	
	@Override
	protected void bindKeys() {	
		inputManager.addMapping("restart", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(this, new String[] {  "restart" });
	}
	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {

		if (name.equals("restart") && !keyPressed) {
			hide();
			game.getTitleScreen().show();
		}
	
	}

}
