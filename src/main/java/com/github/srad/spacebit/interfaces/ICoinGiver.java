package com.github.srad.spacebit.interfaces;

/**
 * Everything that can generate coins for a {@link ICoinTaker} must implement this interface.
 *
 * @author srad
 */
public interface ICoinGiver {

  public int getCoins();

}
