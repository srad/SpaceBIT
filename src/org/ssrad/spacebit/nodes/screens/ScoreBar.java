package org.ssrad.spacebit.nodes.screens;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.nodes.AbstractNode;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

public class ScoreBar extends AbstractNode {

	private BitmapText score;
	
	public ScoreBar(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		score = new BitmapText(game.getGuiFont(), false);          
		score.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
		score.setColor(ColorRGBA.White);                             // font color
		score.setLocalTranslation(0, -5, 0.1f); // position

		attachChild(score);
	}
	
	@Override
	public void update(float tpf) {
		score.setText("Score " + game.getShip().getScore());
	}
	
	@Override
	public void onCollision(AbstractNode collidedWith) {
	}

	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return null;
	}

}