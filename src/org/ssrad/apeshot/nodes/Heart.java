package org.ssrad.apeshot.nodes;

import java.util.ArrayList;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICollidable;
import org.ssrad.apeshot.interfaces.IDamageMaker;
import org.ssrad.apeshot.interfaces.IDamageTaker;
import org.ssrad.apeshot.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;

public class Heart extends ANode implements ICollidable, IDamageMaker, IDestroyable {
	
	Spatial s;
	Material m;

	public Heart(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		m.setColor("Color", ColorRGBA.Red);
		m.setColor("GlowColor", ColorRGBA.Red);
		
		s = assetManager.loadModel("heart.obj");		
		s.setMaterial(m);
		attachChild(s);
		
		setShadowMode(ShadowMode.Cast);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);		
		s.rotate(0, FastMath.PI * tpf * 2.5f, 0);
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<ANode> collidesWith() {
		return new ArrayList<ANode>() {{ add(game.getShip()); }};
	}

	@Override
	public void onCollision(ANode collidedWith) {
		if (collidedWith instanceof IDamageTaker) {
			// Notice that heats make "positive" damage
			((IDamageTaker) collidedWith).onDamage(getDamage());
			
			destroy();
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return s.getWorldBound();
	}

	@Override
	public int getDamage() {
		return 3;
	}

	@Override
	public boolean destroyOnCollision() {
		return true;
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

}
