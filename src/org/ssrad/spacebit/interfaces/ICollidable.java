package org.ssrad.spacebit.interfaces;

import java.util.ArrayList;

import org.ssrad.spacebit.nodes.AbstractNode;

import com.jme3.bounding.BoundingVolume;

/**
 * Anything that can collide with something must implement this interface.
 * 
 * @author Saman Sedighi Rad
 *
 */
public interface ICollidable {
	
	/** List of {@link AbstractNode} which shall collide with the implementer. */
	public ArrayList<AbstractNode> collidesWith();
	
	/** Callback for any collision with class types defined in {@link #collidesWith()}. */
	public void onCollision(AbstractNode collidedWith);
	
	/** Bounds for collisions. */
	public BoundingVolume getBounds();

}
