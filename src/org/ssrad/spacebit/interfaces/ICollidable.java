package org.ssrad.spacebit.interfaces;

import java.util.ArrayList;

import org.ssrad.spacebit.nodes.ANode;

import com.jme3.bounding.BoundingVolume;

/**
 * Anything that can collide with something must implement this interface.
 * 
 * @author Saman Sedighi Rad
 *
 */
public interface ICollidable {
	
	/** List of {@link ANode} which shall collide with the implementer. */
	public ArrayList<ANode> collidesWith();
	
	/** Callback for any collision with class types defined in {@link #collidesWith()}. */
	public void onCollision(ANode collidedWith);
	
	/** Bounds for collisions. */
	public BoundingVolume getBounds();
		
}
