package org.ssrad.apeshot.listeners;

import org.apeshot.enums.GameLevel;
import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.nodes.Laser;


import com.jme3.input.controls.ActionListener;

public class KeyBoardActionListener extends AListener implements ActionListener {

	public KeyBoardActionListener(Game game) {
		super(game);
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {

		if (name.equals("shoot") && !keyPressed) {
			game.addProjectiles(new Laser(game));	
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
			game.pause();
		}
	
	}

}
