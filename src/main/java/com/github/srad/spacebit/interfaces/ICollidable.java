package com.github.srad.spacebit.interfaces;

import com.jme3.bounding.BoundingVolume;
import com.github.srad.spacebit.nodes.entities.AbstractNode;

import java.util.ArrayList;

/**
 * Anything that can collide with something must implement this interface.
 *
 * @author srad
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
