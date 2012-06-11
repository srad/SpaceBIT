package org.ssrad.apeshot.nodes;

import java.util.Iterator;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICollidable;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public abstract class ANode extends Node {
	
	protected Game game;
	protected AssetManager assetManager;
	protected boolean active = true;
	protected float scrollSpeed = 0f;
		
	Spatial s;
	Material m;
	
	public ANode(Game game) {
		this.game = game;
		this.assetManager = game.getAssetManager();
		this.scrollSpeed = game.SCROLL_SPEED;
		init();
	}
	
	protected abstract void init();
	
	public void update(float tpf) {
		checkCollisions();
		
		// Out of sight, remove
		if (game.getCamera().getLocation().z > getLocalTranslation().z) {
			active = false;
		}
	}

	private void checkCollisions() {
		if (this instanceof ICollidable) {
			ICollidable collisionData = (ICollidable) this;			
			
			for (Iterator<ANode> it = collisionData.collidesWith().iterator(); it.hasNext();) {
				
				ANode anode = (ANode) it.next();
			
				// Check intersection and invoke collision callback.
				if (collisionData.getBounds().intersects(anode.getWorldBound())) {
					collisionData.onCollision(anode);
				}		
			}
		}
	}

	public boolean isActive() {
		return active;
	}
	
}
