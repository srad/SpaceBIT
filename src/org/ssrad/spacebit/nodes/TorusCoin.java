package org.ssrad.spacebit.nodes;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICoinMaker;
import org.ssrad.spacebit.interfaces.ICoinTaker;
import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;


public class TorusCoin extends ANode implements ICollidable, ICoinMaker, IDestroyable {
	
	public TorusCoin(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {
		spatial = assetManager.loadModel("ring.obj");		

		// Simple glow material looks nicer than texture
		Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		m.setColor("Color", ColorRGBA.Red);
		m.setColor("GlowColor", ColorRGBA.Green);
		
		spatial.setMaterial(m);
		scale(1.4f);

		addLight();
		
		attachChild(spatial);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);		
		spatial.rotate(0, FastMath.PI * tpf, 0);
		light.setPosition(getLocalTranslation());	
	}
	
	private void addLight() {
		light = new PointLight();
		light.setColor(ColorRGBA.Yellow);
		light.setRadius(20f);
		
		game.getRootNode().addLight(light);
	}
	
	public Spatial getSpatial() {
		return spatial;
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<ANode> collidesWith() {
		return new ArrayList<ANode>() {{ add(game.getShip()); }};
	}

	@Override
	public void onCollision(ANode collidedWith) {
		if (collidedWith instanceof ICoinTaker) {
			((ICoinTaker) collidedWith).takeCoins(getCoins());
			destroy();
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return spatial.getWorldBound();
	}

	@Override
	public int getCoins() {
		return 1;
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

	@Override
	public boolean destroyOnCollision() {
		return true;
	}

}
