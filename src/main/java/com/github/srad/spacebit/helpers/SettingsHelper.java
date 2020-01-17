package com.github.srad.spacebit.helpers;

import com.github.srad.spacebit.game.GameSettings;
import org.apache.logging.log4j.Level;

import java.io.*;

public class SettingsHelper {

  private String GAME_HOME_FOLDER = ".spacebit";
  private String SETTINGS_FILE = "settings.dat";

  private GameSettings gameSettings = null;
  private FileInputStream savedFile;

  private String settingsFile;
  private String gameDir;

  public SettingsHelper() {
    try {
      boolean success = false;
      String homeDir = System.getProperty("user.home");
      setGameDir(homeDir + "/" + GAME_HOME_FOLDER);
      settingsFile = getGameDir() + "/" + SETTINGS_FILE;

      File folder = new File(getGameDir());

      if (!folder.exists()) {
        success = folder.mkdir();
      } else {
        success = true;
      }
      if (success) {
        // LOAD FILE
        try {
          savedFile = new FileInputStream(settingsFile);
        } catch (FileNotFoundException e) {
          try {
            setGameSettings(new GameSettings());
            savedFile = new FileInputStream(settingsFile);
          } catch (IOException IOe) {
            LogHelper.getLogger().error("Failed load settings: " + IOe.getMessage());
          }
        }

        try {
          ObjectInputStream save;
          save = new ObjectInputStream(savedFile);
          this.gameSettings = (GameSettings) save.readObject();
        } catch (Exception e) {
          LogHelper.getLogger().error(e.getMessage());
        }
      }
    } catch (Exception e) {
      LogHelper.getLogger().log(Level.ERROR, "Failed to create home folder: " + e.getMessage());
    }
  }

  /**
   * Also saves the settings.
   *
   * @param gameSettings
   */
  public void setGameSettings(GameSettings gameSettings) {
    this.gameSettings = gameSettings;

    try {
      FileOutputStream saveFile = new FileOutputStream(settingsFile);
      ObjectOutputStream save = new ObjectOutputStream(saveFile);

      save.writeObject(this.gameSettings);
      save.close();
    } catch (FileNotFoundException e) {
      LogHelper.getLogger().error(e.getMessage());
    } catch (IOException e) {
      LogHelper.getLogger().error(e.getMessage());
    }
  }

  public GameSettings getGameSettings() throws FileNotFoundException {
    if (this.gameSettings == null) {
      return new GameSettings();
    }
    return this.gameSettings;
  }

  public String getGameDir() {
    return gameDir;
  }

  private void setGameDir(String gameDir) {
    this.gameDir = gameDir;
  }

}
