package org.ssrad.spacebit.listeners;

import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.nodes.Laser;


import com.jme3.input.controls.ActionListener;

public class KeyBoardActionListener extends AListener implements ActionListener {

	public KeyBoardActionListener(Game game) {
		super(game);
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {

		if (name.equals("shoot") && !keyPressed) {
			game.getUpdateables().addLaser(new Laser(game));	
		}
		
		if (name.equals("bloom") && !keyPressed) {
			game.toggleBloom();
		}
		
		if (name.equals("shadow") && !keyPressed) {
			game.toggleShadow();
		}
		
		if ( (name.equals("level_1") || name.equals("level_2")) && !keyPressed ) {
			game.getTitleScreen().hide();
			game.setLevel(name.equals("level_1") ? GameLevel.LEVEL_ONE : GameLevel.LEVEL_TWO);
			game.run();
		}
		
		if (name.equals("quit") && !keyPressed) {
			game.stop();
		}
		
		if (name.equals("pause") && !keyPressed) {
			if (game.isRunning()) {
				game.pause();
				game.getGameMusic().play();
			} else {
				game.run();
				game.getGameMusic().play();
			}
		}
	
	}

}
