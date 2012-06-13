package org.ssrad.apeshot.listeners;

import org.ssrad.apeshot.game.Game;

import com.jme3.input.controls.ActionListener;

public class MouseListener extends AListener implements ActionListener {

	public MouseListener(Game game) {
		super(game);
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
		if (name.equals("shoot")) {
		}
	}

}
