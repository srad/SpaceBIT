package org.ssrad.spacebit.nodes;

import org.ssrad.spacebit.game.Game;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

public class BullsEye extends ANode {

	public BullsEye(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		spatial = assetManager.loadModel("bullseye.obj");

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Black);
		material.setColor("GlowColor", ColorRGBA.Pink);

		spatial.setMaterial(material);
		spatial.move(-6f, -6.3f, 0f);
		
		scale(6f);
		attachChild(spatial);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
	}

	@Override
	public void destroy() {
		game.getRootNode().detachChild(this);
	}

}
