package org.apeshot.main;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.ssrad.apeshot.game.Game;

import com.jme3.system.AppSettings;

public class ApeShot {

	private static Game game;

	public static void main(String[] args) {
		Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
		
		AppSettings settings = new AppSettings(true);

		settings.setResolution(1024, 768);
		settings.setSamples(16);
		settings.setTitle("ApeShot");

		game = new Game();
		

		game.setSettings(settings);
//		game.setShowSettings(false);

		game.start();
	}

	public static Game getInstance() {
		if (game == null) {
			game = new Game();
		}
		return game;
	}
	
}
