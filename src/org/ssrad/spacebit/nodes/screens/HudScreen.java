package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.nodes.Laser;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;

public class HudScreen extends AbstractScreen implements AnalogListener, ActionListener {
	
	HealthBar healthBar;
	HeartBar heartBar;
	CoinBar coinBar;
	ScoreBar scoreBar;
	
	boolean init;

	public HudScreen(Game game) {
		super(game);
		
		addCoinStatus();
		addHealthBar();
		addHeartBar();
		addScoreBar();
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
	}
	
	private void addScoreBar() {
		scoreBar = new ScoreBar(game);
		scoreBar.setLocalTranslation(750, 50, 0f);
		
		attachChild(scoreBar);
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
		inputManager.addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));
				
		inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
		
		inputManager.addMapping("bloom", new KeyTrigger(KeyInput.KEY_F1));
		inputManager.addMapping("shadow", new KeyTrigger(KeyInput.KEY_F2));

		inputManager.addMapping("quit", new KeyTrigger(KeyInput.KEY_ESCAPE));

		inputManager.addListener(this, new String[] { "pause", "shoot", "quit", "shadow", "bloom" });
		inputManager.addListener(this, new String[] { "up", "down", "left", "right" });
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {

		if (name.equals("shoot") && !keyPressed) {
			game.getUpdateables().addLaser(new Laser(game));	
		}
		
		if (name.equals("bloom") && !keyPressed) {
			game.toggleBloom();
		}
		
		if (name.equals("shadow") && !keyPressed) {
			game.toggleShadow();
		}
		
		if (name.equals("quit") && !keyPressed) {
			game.stop();
		}
		
		if (name.equals("pause") && !keyPressed) {
			if (game.isRunning()) {
				game.pause();
				game.getGameMusic().play();
			} else {
				game.run();
				game.getGameMusic().play();
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
