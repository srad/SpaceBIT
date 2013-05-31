package org.ssrad.spacebit.interfaces;

import com.jme3.bounding.BoundingVolume;
import org.ssrad.spacebit.nodes.AbstractNode;

import java.util.ArrayList;

/**
 * Anything that can collide with something must implement this interface.
 *
 * @author Saman Sedighi Rad
 */
public interface ICollidable {

    /**
     * List of {@link AbstractNode} which shall collide with the implementer.
     */
    public ArrayList<AbstractNode> collidesWith();

    /**
     * Callback for any collision with class types defined in {@link #collidesWith()}.
     */
    public void onCollision(AbstractNode collidedWith);

    /**
     * Bounds for collisions.
     */
    public BoundingVolume getBounds();

}
