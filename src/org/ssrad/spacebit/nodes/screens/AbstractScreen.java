package org.ssrad.spacebit.nodes.screens;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.nodes.AbstractNode;

import com.jme3.input.InputManager;

public abstract class AbstractScreen extends AbstractNode {
	
	InputManager inputManager;

	public AbstractScreen(Game game) {
		super(game);
	}

	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return null;
	}
	
	@Override
	protected void init() {
		game.getGuiNode().detachAllChildren();
		game.getInputManager().clearMappings();
		
		inputManager = game.getInputManager();
		bindKeys();

		game.setDisplayStatView(Game.DEBUG);
		game.setDisplayFps(Game.DEBUG);	
	}
	
	/** Not abstract because some node don't need bindings. */
	protected void bindKeys() {
	}

	@Override
	public void onCollision(AbstractNode collidedWith) {
	}
		
	public void hide() {
		active = false;
	}
	
	public void show() {
		init();		
		active = true;
		game.setScreen(this);
	}

}
