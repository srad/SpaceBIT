package org.ssrad.spacebit.interfaces;

/**
 * Everything that can generate coins for a {@link ICoinTaker} must implement this interface.
 * 
 * @author Saman Sedighi Rad
 *
 */
public interface ICoinGiver {
	
	public int getCoins();

}
