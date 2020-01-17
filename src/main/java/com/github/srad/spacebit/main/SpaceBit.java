package com.github.srad.spacebit.main;

import com.github.srad.spacebit.helpers.LogHelper;
import com.github.srad.spacebit.helpers.SettingsHelper;
import com.jme3.system.AppSettings;
import com.github.srad.spacebit.game.Game;
import com.github.srad.spacebit.game.GameSettings;

public class SpaceBit {

  private static Game game = null;

  public static void main(String[] args) {
    try {

      java.util.logging.Logger.getLogger("com.jme3").setLevel(java.util.logging.Level.OFF);

      final var settingsHelper = new SettingsHelper();
      final var gameSettings = settingsHelper.getGameSettings();
      game = getInstance(settingsHelper);

      final var settings = new AppSettings(true);
      settings.setSettingsDialogImage("screens/splash.png");
      settings.setResolution(gameSettings.gethResolution(), gameSettings.getvResolution());
      settings.setVSync(gameSettings.isEnableVSync());
      settings.setFullscreen(gameSettings.isFullScreen());
      settings.setDepthBits(gameSettings.getBitsPerPixel());
      settings.setSamples(gameSettings.getSamples());
      settings.setTitle("Space.BIT");

      game.setSettings(settings);
      game.start();
    } catch (Exception e) {
      e.printStackTrace();
      LogHelper.getLogger().error(e.getMessage());
    }
  }

  public static Game getInstance() {
    if (game == null) {
      game = new Game();
    }
    return game;
  }

  public static Game getInstance(SettingsHelper settingsHelper) {
    if (game == null) {
      game = new Game(new SettingsHelper());
    }
    return game;
  }

}
