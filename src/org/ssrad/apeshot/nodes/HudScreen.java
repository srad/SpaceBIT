package org.ssrad.apeshot.nodes;

import org.ssrad.apeshot.game.Game;

import com.jme3.font.BitmapText;

public class HudScreen extends ANode {
	
	HealthBar healthBar;
	HeartBar heartBar;
	CoinBar coinBar;

	public HudScreen(Game game) {
		super(game);
	}
	
	@Override
	public void init() {
		game.setDisplayStatView(false);
		game.setDisplayFps(false);

		addCoinStatus();
		addHealthBar();
		addHeartBar();
		
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

	@Override
	public void destroy() {
		game.getRootNode().detachChild(this);
	}

}
