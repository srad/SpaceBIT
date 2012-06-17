package org.ssrad.spacebit.nodes;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Heart extends ANode implements ICollidable, IDamageMaker, IDestroyable {
	
	public Heart(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		material.setColor("Color", ColorRGBA.Red);
		material.setColor("GlowColor", ColorRGBA.Red);
		
		spatial = assetManager.loadModel("heart.obj");		
		spatial.setMaterial(material);
		attachChild(spatial);
		addLight();
		
		setShadowMode(ShadowMode.Cast);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);		
		spatial.rotate(0, FastMath.PI * tpf * 2.5f, 0);
		light.setPosition(getLocalTranslation());
	}
	
	private void addLight() {
		light = new PointLight();
		light.setColor(ColorRGBA.Red);
		light.setRadius(20f);

		game.getRootNode().addLight(light);
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
		return spatial.getWorldBound();
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
	public boolean isDetroyable() {
		return true;
	}

}
