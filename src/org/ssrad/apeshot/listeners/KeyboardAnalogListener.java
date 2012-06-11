package org.ssrad.apeshot.listeners;

import org.ssrad.apeshot.game.Game;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;

public class KeyboardAnalogListener extends AListener implements AnalogListener {

	private static final float SHIP_MARGIN = 5f;
	
	public KeyboardAnalogListener(Game game) {
		super(game);
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		
		Vector3f shipLocation = game.getShip().clone().getLocalTranslation();
		Vector3f newLocation = null;
		
		float moveOffset = 0.1f;
		
		if (name.equals("left")) {
			newLocation = shipLocation.add(moveOffset, 0, 0);
			game.getShip().left();
		}
		
		if (name.equals("right")) {
			newLocation = shipLocation.add(-moveOffset, 0, 0);
			game.getShip().right();
		}
		
		if (name.equals("up")) {
			newLocation = shipLocation.add(0, 0, moveOffset);
		}
		
		if (name.equals("down")) {
			newLocation = shipLocation.add(0, 0, -moveOffset);
		}
				
		//System.err.println(game.getCamera().getLeft() + ", " + game.getShip().getLocalTranslation());
		
		if (newLocation != null) {
			// Don't walk over screen edge
			if (newLocation.z < game.getCamera().getLocation().z + SHIP_MARGIN
				|| ((game.getShip().getLocalTranslation().x) > 31d && !name.equals("right"))
				|| ((game.getShip().getLocalTranslation().x) < -31d && !name.equals("left"))
					) {
				newLocation = null;
			} else {
				game.getShip().setLocalTranslation(newLocation);
			}
		}
		
	}
	
}
