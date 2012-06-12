package org.ssrad.apeshot.nodes;

import org.ssrad.apeshot.game.Game;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

public class HudScreen extends ANode {
	
	BitmapText health;
	Picture statusBackground;
	HealthBar healthBar;
	
	Node statusBar;

	public HudScreen(Game game) {
		super(game);
	}
	
	@Override
	public void init() {
		statusBar = new Node();
		
		health = new BitmapText(game.getGuiFont(), false);          
		game.setDisplayStatView(true);
		game.setDisplayFps(true);
		
		health = new BitmapText(game.getGuiFont(), false);          
		health.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
		health.setColor(ColorRGBA.Black);                             // font color
		health.setLocalTranslation(10, health.getLineHeight() + 60, 0.1f); // position
		statusBar.attachChild(health);
				
		healthBar = new HealthBar(game);
		healthBar.setLocalTranslation(10, 100, 0f);

		statusBar.attachChild(healthBar);
		
		statusBar.move(0, 0, -200f);
		game.getGuiNode().attachChild(statusBar);
	}
	
	@Override
	public void update(float tpf) {
		health.setText("Health:" + game.getShip().getHealth());
		healthBar.setPercentage((float) game.getShip().getHealth() / 100);
		healthBar.update(tpf);
	}
	
	@Override
	public void destroy() {
		game.getRootNode().detachChild(this);
	}

}
