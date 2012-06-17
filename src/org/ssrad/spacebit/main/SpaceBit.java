package org.ssrad.spacebit.main;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.helpers.GameLogger;

import com.jme3.system.AppSettings;

public class SpaceBit {

	private static Game game;

	public static void main(String[] args) {
		try {
			 Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
			Logger.getLogger("org.ssrad").setLevel(Level.ALL);
			
			AppSettings settings = new AppSettings(true);
			
			settings.setResolution(1024, 768);
			settings.setTitle("Space.BIT");
			
			game = new Game();
			game.setSettings(settings);
			//game.setShowSettings(false);
			game.start();			
		} catch (Exception e) {
			GameLogger.Log(Level.WARNING, e.getMessage());
		}
	}

	public static Game getInstance() {
		if (game == null) {
			game = new Game();
		}
		return game;
	}
	
}
