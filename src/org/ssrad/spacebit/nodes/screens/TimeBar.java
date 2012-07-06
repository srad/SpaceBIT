package org.ssrad.spacebit.nodes.screens;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.nodes.AbstractNode;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

public class TimeBar extends AbstractNode {

	private BitmapText time;
	
	public TimeBar(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		time = new BitmapText(game.getGuiFont(), false);
		time.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
		time.setColor(ColorRGBA.White);                             // font color
		time.setLocalTranslation(0, 0, 0.1f); // position

		attachChild(time);
	}
	
	@Override
	public void update(float tpf) {
		time.setText("Countdown " + (Game.GAME_TIME_SECONDS-(int)game.getTimer().getTimeInSeconds()));
	}
	
	@Override
	public void onCollision(AbstractNode collidedWith) {
	}

	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return null;
	}

}
