package org.ssrad.spacebit.nodes;

import org.ssrad.spacebit.game.Game;

import com.jme3.ui.Picture;

public class HudScreen extends ANode {
	
	HealthBar healthBar;
	HeartBar heartBar;
	CoinBar coinBar;
	
	Picture background;

	public HudScreen(Game game) {
		super(game);
	}
	
	@Override
	public void init() {
		game.setDisplayStatView(Game.DEBUG);
		game.setDisplayFps(Game.DEBUG);

		addCoinStatus();
		addHealthBar();
		addHeartBar();
		
		// TODO: Background looks meh
//		background = new Picture("HUD Picture");
//		background.setImage(assetManager, "hud_bg2.png", true);
//		background.setWidth(game.getSettings().getWidth());
//		background.setHeight(143);
//		background.setPosition(0, 0);
//		
//		game.getGuiNode().attachChild(background);
		game.getGuiNode().attachChild(this);
	}
	
	@Override
	public void update(float tpf) {
		healthBar.update(tpf);
		coinBar.update(tpf);
		heartBar.update(tpf);
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

}
