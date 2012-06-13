package org.ssrad.apeshot.nodes;

import org.ssrad.apeshot.game.Game;

import com.jme3.math.FastMath;

public class Rocket extends ANode {

	public Rocket(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		s.rotate(0, FastMath.PI/8 * tpf, 0);
	}
	

	@Override
	public void destroy() {
		game.getRootNode().detachChild(this);
	}

}
