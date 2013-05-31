package org.ssrad.spacebit.main;

import com.jme3.system.AppSettings;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.game.GameSettings;
import org.ssrad.spacebit.helpers.LogHelper;
import org.ssrad.spacebit.helpers.SettingsHelper;

public class SpaceBit {

    private static Game game = null;

    public static void main(String[] args) {
        try {

            java.util.logging.Logger.getLogger("com.jme3").setLevel(java.util.logging.Level.OFF);

            SettingsHelper settingsHelper = new SettingsHelper();
            GameSettings gameSettings = settingsHelper.getGameSettings();
            game = getInstance(settingsHelper);

            AppSettings settings = new AppSettings(true);
            //settings.setSettingsDialogImage("splash.png");
            settings.setResolution(gameSettings.gethResolution(), gameSettings.getvResolution());
            settings.setVSync(gameSettings.isEnableVSync());
            settings.setFullscreen(gameSettings.isFullScreen());
            settings.setDepthBits(gameSettings.getBitsPerPixel());
            settings.setSamples(gameSettings.getSamples());
            settings.setTitle("Space.BIT");

            game.setSettings(settings);
            game.start();
        } catch (Exception e) {
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
