package org.ssrad.spacebit.listeners;

import org.ssrad.spacebit.game.Game;

import com.jme3.input.controls.AnalogListener;

public class KeyboardAnalogListener extends AListener implements AnalogListener {
	
	public KeyboardAnalogListener(Game game) {
		super(game);
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {

		if (name.equals("left")) {
			game.getShip().left(tpf);
		}
		
		if (name.equals("right")) {
			game.getShip().right(tpf);
		}
		
		if (name.equals("up")) {
			game.getShip().up(tpf);
		}
		
		if (name.equals("down")) {
			game.getShip().down(tpf);
		}
		
	}
	
}
