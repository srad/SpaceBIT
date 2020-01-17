package com.github.srad.spacebit.nodes.screens;

import com.github.srad.spacebit.game.Game;
import com.github.srad.spacebit.nodes.entities.Laser;
import com.github.srad.spacebit.nodes.screens.hud.*;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;

public class HudScreen extends AbstractScreen implements AnalogListener, ActionListener {

  HealthBar healthBar;
  HeartBar heartBar;
  CoinBar coinBar;
  ScoreBar scoreBar;
  TimeBar timeBar;

  boolean init;

  public HudScreen(Game game) {
    super(game);

    addCoinStatus();
    addHealthBar();
    addHeartBar();
    addScoreBar();
    addTimeBar();
  }

  @Override
  public void init() {
    super.init();
    game.getGuiNode().attachChild(this);
  }

  @Override
  public void update(float tpf) {
    healthBar.update(tpf);
    coinBar.update(tpf);
    heartBar.update(tpf);
    scoreBar.update(tpf);
    timeBar.update(tpf);
  }

  private void addScoreBar() {
    scoreBar = new ScoreBar(game);
    scoreBar.setLocalTranslation(game.getSettings().getWidth() - 200, game.getSettings().getHeight() - 10, 0f);

    attachChild(scoreBar);
  }

  private void addTimeBar() {
    timeBar = new TimeBar(game);
    timeBar.setLocalTranslation(10, game.getSettings().getHeight() - 10, 0);

    attachChild(timeBar);
  }

  private void addHeartBar() {
    heartBar = new HeartBar(game);
    heartBar.setLocalTranslation(550, 50, 0f);

    attachChild(heartBar);
  }

  private void addCoinStatus() {
    coinBar = new CoinBar(game);
    coinBar.setLocalTranslation(310, 50, 0f);

    attachChild(coinBar);
  }

  private void addHealthBar() {
    healthBar = new HealthBar(game);
    healthBar.setLocalTranslation(15, 50, 0f);

    attachChild(healthBar);
  }

  @Override
  protected void bindKeys() {
    inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
    inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_ESCAPE));
    inputManager.addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));

    inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));

    inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));

    inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));

    inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));

    inputManager.addMapping("bloom", new KeyTrigger(KeyInput.KEY_F1));
    inputManager.addMapping("shadow", new KeyTrigger(KeyInput.KEY_F2));
    inputManager.addMapping("ls", new KeyTrigger(KeyInput.KEY_F3));

    inputManager.addListener(this, new String[]{"pause", "shoot", "shadow", "bloom", "ls"});
    inputManager.addListener(this, new String[]{"up", "down", "left", "right"});
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (name.equals("shoot") && !keyPressed) {
      game.getEntities().add(new Laser(game));
    }

    if (name.equals("bloom") && !keyPressed) {
      game.toggleBloom();
    }

    if (name.equals("shadow") && !keyPressed) {
      game.toggleShadow();
    }

    if (name.equals("ls") && !keyPressed) {
      game.toggleLSFilter();
    }

    if (name.equals("quit") && !keyPressed) {
      game.stop();
    }

    if (name.equals("pause") && !keyPressed) {
      if (game.isRunning()) {
        game.pause();
      } else {
        game.run();
      }
    }

  }

  @Override
  public void onAnalog(String name, float value, float tpf) {

    if (name.equals("left")) {
      game.getShip().left(tpf);
    }

    if (name.equals("right")) {
      game.getShip().right(tpf);
    }

    if (name.equals("up")) {
      game.getShip().up(tpf);
    }

    if (name.equals("down")) {
      game.getShip().down(tpf);
    }

  }

}
