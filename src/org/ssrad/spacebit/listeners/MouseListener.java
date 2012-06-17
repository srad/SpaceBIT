package org.ssrad.spacebit.listeners;

import org.ssrad.spacebit.game.Game;

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
