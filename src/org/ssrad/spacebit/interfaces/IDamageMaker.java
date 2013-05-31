package org.ssrad.spacebit.interfaces;

/**
 * Anything that can take damage must implement this interface.
 *
 * @author Saman Sedighi Rad
 */
public interface IDamageMaker {

    /**
     * Returns the damage that an object can make, i.e. in case of a collision
     * of certain nodes, spatials or whatever.
     */
    public int getDamage();

}
