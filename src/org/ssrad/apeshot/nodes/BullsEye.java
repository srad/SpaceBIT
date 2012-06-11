package org.ssrad.apeshot.nodes;

import org.ssrad.apeshot.game.Game;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

public class BullsEye extends ANode {

	public BullsEye(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		s = assetManager.loadModel("bullseye.obj");

		m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		m.setColor("Color", ColorRGBA.Black);
		m.setColor("GlowColor", ColorRGBA.Pink);

		s.setMaterial(m);
		s.move(-6f, -6.3f, 0f);
		
		scale(6f);
		attachChild(s);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
	}

}
