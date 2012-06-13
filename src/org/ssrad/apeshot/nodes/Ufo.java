package org.ssrad.apeshot.nodes;

import java.util.ArrayList;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICollidable;
import org.ssrad.apeshot.interfaces.IDamageMaker;
import org.ssrad.apeshot.interfaces.IDamageTaker;
import org.ssrad.apeshot.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Ufo extends ANode implements IDestroyable, IDamageMaker, IDamageTaker, ICollidable {
	
	private int health = 20;

	public Ufo(Game game) {
		super(game);
	}
	
	ArrayList<Laser> lasers = new ArrayList<Laser>();

	@Override
	protected void init() {
		s = this.assetManager.loadModel("ufo2/ufo2.obj");	

		m = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
		m.setTexture("DiffuseMap", assetManager.loadTexture("ufo2/ufo2.png"));
		m.setTexture("NormalMap", assetManager.loadTexture("ufo2/ufo2_normals.png"));

		s.rotate(FastMath.PI, 0, 0);
		
		setShadowMode(ShadowMode.Cast);
		
		s.scale(2.6f);
		attachChild(s);
		
		light = new PointLight();
		light.setRadius(30f);
		light.setColor(ColorRGBA.Gray);
		game.getRootNode().addLight(light);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		s.rotate(0, FastMath.PI * tpf * 2.5f, 0);
		light.setPosition(getLocalTranslation().clone().add(0, 0, 0));
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public void destroy() {
		active = false;	
		
		game.getRootNode().removeLight(light);
		game.getRootNode().detachChild(this);
		game.addFireExplosion(new FireExplosion(game, getLocalTranslation()));		
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
	}

	@Override
	public int getDamage() {
		return -10;
	}

	@Override
	public ArrayList<ANode> collidesWith() {
		ArrayList<ANode> n = new ArrayList<ANode>();
		n.addAll(game.getLasers());
		n.add(game.getShip());
		
		return n;
	}

	@Override
	public void onCollision(ANode collidedWith) {
		if (collidedWith instanceof IDamageTaker) {
			// Send this damage
			((IDamageTaker) collidedWith).onDamage(getDamage());
		}
		if (collidedWith instanceof IDamageMaker) {
			
			IDamageMaker damageMaker = (IDamageMaker) collidedWith;
			onDamage(damageMaker.getDamage());
			
			// Check for ape's self destruction
			if (destroyOnCollision()) {
				destroy();
			}
			
			if (damageMaker instanceof IDestroyable) {
				IDestroyable destroyable = (IDestroyable) damageMaker;
				if (destroyable.destroyOnCollision()) {
					destroyable.destroy();
				}
			}
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return s.getWorldBound();
	}

}
