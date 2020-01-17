package com.github.srad.spacebit.nodes.entities;

import com.jme3.math.FastMath;
import com.github.srad.spacebit.game.Game;

import java.util.ArrayList;

public class Rocket extends AbstractNode {

  public Rocket(Game game) {
    super(game);
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(float tpf) {
    super.update(tpf);
    spatial.rotate(0, FastMath.PI / 8 * tpf, 0);
  }

  @Override
  public ArrayList<AbstractNode> collidesWith() {
    // TODO Auto-generated method stub
    return null;
  }

}
