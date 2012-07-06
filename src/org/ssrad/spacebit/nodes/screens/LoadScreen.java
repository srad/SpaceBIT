package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.audio.GameMusic;
import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.game.Game;

import com.jme3.ui.Picture;

public class LoadScreen extends AbstractScreen {

	Picture background;
	float timer;
	boolean started;
	
	public LoadScreen(Game game) {
		super(game);
	}
	@Override
	protected void init() {
		super.init();
		started = false;
		
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
		if ((timer > 1f) && !started) {
			onStartGame();
		}
	}
	private void onStartGame() {
		started = true;
		hide();
				
		if (game.getShip() != null) {
			game.getShip().setScore(0);
			game.getShip().setLives(2);
			game.getShip().setCoins(0);
			game.getUpdateables().destroyObstacles();
		}
		game.getTimer().reset();
		
		if (!game.isLaunched()) {
			game.init();
		}
		if (game.getGameMusic() != null) {
			game.getGameMusic().detachFromParent();
		}
		game.setGameMusic(new GameMusic(game));		

		game.run();
	}

}
