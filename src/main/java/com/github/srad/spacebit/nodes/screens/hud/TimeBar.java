package com.github.srad.spacebit.nodes.screens.hud;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.github.srad.spacebit.game.Game;
import com.github.srad.spacebit.nodes.entities.AbstractNode;

import java.util.ArrayList;

public class TimeBar extends AbstractNode {

  private BitmapText time;

  public TimeBar(Game game) {
    super(game);
  }

  @Override
  protected void init() {
    time = new BitmapText(game.getGuiFont(), false);
    time.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
    time.setColor(ColorRGBA.White);                             // font color
    time.setLocalTranslation(game.getSettings().getWidth() - 210f, -60f, 0.1f); // position

    attachChild(time);
  }

  @Override
  public void update(float tpf) {
    int countDown = Game.GAME_TIME_SECONDS - (int) game.getTimer().getTimeInSeconds();

    if (countDown < 20) {
      time.setColor(ColorRGBA.Red);
    }

    time.setText("Countdown " + countDown);
  }

  @Override
  public void onCollision(AbstractNode collidedWith) {
  }

  @Override
  public ArrayList<AbstractNode> collidesWith() {
    return null;
  }

}
