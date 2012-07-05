package org.ssrad.spacebit.controls;

import java.util.ArrayList;
import java.util.Iterator;

import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.nodes.AbstractSpaceBitControl;

import com.jme3.scene.control.AbstractControl;

abstract public class MyAbstractControl extends AbstractControl {
	
	protected void controlUpdate(float tpf) {
		// Out of sight, remove
		if (  game.getCamera().getLocation().z > spatial.getLocalTranslation().z) {
			enabled = false;
		}
		checkCollisions();
	}	

	private void checkCollisions() {
		if (this instanceof ICollidable) {
			ICollidable collision = (ICollidable) this;
			ArrayList<AbstractSpaceBitControl> colliders = collision
					.collidesWith();

			if (colliders != null) {
				for (Iterator<AbstractSpaceBitControl> it = colliders
						.iterator(); it.hasNext();) {

					AbstractSpaceBitControl anode = (AbstractSpaceBitControl) it
							.next();

					// Check intersection and invoke collision callback.
					if (collision.getBounds().intersects(
							anode.getSpatial().getWorldBound())
							&& anode.isActive()) {
						collision.onCollision(anode);
					}
				}
			}
		}
	}

}
