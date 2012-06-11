package org.ssrad.apeshot.nodes;

import org.ssrad.apeshot.game.Game;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;

public class HudScreen extends ANode {
	
	BitmapText health;
	Picture statusBackground;
	

	public HudScreen(Game game) {
		super(game);
	}
	
	@Override
	public void init() {
		health = new BitmapText(game.getGuiFont(), false);          
		game.setDisplayStatView(false);
		game.setDisplayFps(false);
		
		health = new BitmapText(game.getGuiFont(), false);          
		health.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.4f);      // font size
		health.setColor(ColorRGBA.White);                             // font color
		health.setLocalTranslation(10, health.getLineHeight() + 70, 0.1f); // position
		game.getGuiNode().attachChild(health);
		
		statusBackground = new Picture("status");
		statusBackground.setImage(assetManager, "hud.png", true);
		
		statusBackground.setWidth(game.getSettings().getWidth() - 10);
		statusBackground.setHeight(102);
		
		statusBackground.setPosition(0, 8);
		
		game.getGuiNode().attachChild(statusBackground);
	}
	
	@Override
	public void update(float tpf) {
		health.setText("Health:" + game.getShip().getHealth());
	}

}
