package com.github.srad.spacebit.nodes.screens;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.ui.Picture;
import com.github.srad.spacebit.game.Game;

public class CopyrightScreen extends AbstractScreen implements ActionListener {

  Picture background;

  public CopyrightScreen(Game game) {
    super(game);
  }

  @Override
  protected void init() {
    super.init();

    background = new Picture("Test");

    background.setImage(game.getAssetManager(), "screens/copyright-screen.png", true);
    background.setWidth(game.getSettings().getWidth());
    background.setHeight(game.getSettings().getHeight());

    background.setPosition(0, 0);
    game.getGuiNode().attachChild(background);
  }

  @Override
  protected void bindKeys() {
    inputManager.addMapping("back", new KeyTrigger(KeyInput.KEY_ESCAPE));
    inputManager.addListener(this, new String[]{"back"});
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (name.equals("back") && !keyPressed) {
      hide();
      game.getTitleScreen().show();
    }

  }

}
