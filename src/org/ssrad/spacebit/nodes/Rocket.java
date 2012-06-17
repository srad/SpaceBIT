package org.ssrad.spacebit.nodes;

import org.ssrad.spacebit.game.Game;

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
		spatial.rotate(0, FastMath.PI/8 * tpf, 0);
	}

}
