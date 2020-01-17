package com.github.srad.spacebit.interfaces;

/**
 * Everthing whatever can accumulate coins must implement this.
 *
 * @author srad
 */
public interface ICoinTaker {

  public void onCoins(int amount);

}
