package org.ssrad.apeshot.nodes;

import java.util.ArrayList;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICoinMaker;
import org.ssrad.apeshot.interfaces.ICoinTaker;
import org.ssrad.apeshot.interfaces.ICollidable;
import org.ssrad.apeshot.interfaces.IDestroyable;

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
		s = assetManager.loadModel("ring.obj");		

		// Simple glow material looks nicer than texture
		Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		m.setColor("Color", ColorRGBA.Red);
		m.setColor("GlowColor", ColorRGBA.Green);
		
		s.setMaterial(m);
		scale(1.4f);

		addLight();
		
		attachChild(s);
	}
	
	private void addLight() {
		PointLight l = new PointLight();
		l.setColor(ColorRGBA.Yellow);
		l.setRadius(40f);

		s.addLight(l);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);		
		rotate(0, FastMath.PI * tpf, 0);
	}
	
	public Spatial getSpatial() {
		return s;
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
		return s.getWorldBound();
	}

	@Override
	public int getCoins() {
		return 1;
	}

	@Override
	public void destroy() {
		game.getRootNode().detachChild(this);		
		active = false;
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
