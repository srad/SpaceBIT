package org.ssrad.spacebit.interfaces;

/**
 * Anything that can make damage must implement this interface.
 *
 * @author Saman Sedighi Rad
 */
public interface IDamageTaker {

    /**
     * The amount of damage that shall be send.
     * Notice this valus can be positive for hearts and begative for enemies and stuff.
     *
     * @param damage
     */
    public void onDamage(int damage);

}
