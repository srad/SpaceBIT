package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.game.Game;

import com.jme3.ui.Picture;

public class LoadScreen extends AbstractScreen {

	Picture background;
	float timer;
	
	public LoadScreen(Game game) {
		super(game);
	}
	@Override
	protected void init() {
		super.init();
		
		timer = 0f;		
		background = new Picture("Load");
		
		String bgImage = "load_level_1.png";
		if (game.getLevel() == GameLevel.LEVEL_TWO) {
			bgImage = "load_level_2.png";
		}
		background.setImage(game.getAssetManager(), bgImage, true);		
		background.setWidth(game.getSettings().getWidth());
		background.setHeight(game.getSettings().getHeight());
		
		background.setPosition(0, 0);
		game.getGuiNode().attachChild(background);
	}

	@Override
	public void update(float tpf) {
		timer += tpf;
		if (timer > 3f) {
			hide();
			game.run();
		}
	}

}
