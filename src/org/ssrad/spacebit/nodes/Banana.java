package org.ssrad.spacebit.nodes;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;

public class Banana extends ANode implements ICollidable, IDamageMaker, IDestroyable, IDamageTaker {
	
	float speed = 1f;
	int health = 1;
	
	public Banana(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		spatial = assetManager.loadModel("banana.obj");

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Brown);
		material.setColor("GlowColor", ColorRGBA.Yellow);

		spatial.setMaterial(material);
		spatial.move(-6f, -6.3f, 0f);
		
		setShadowMode(ShadowMode.Cast);
		
		scale(0.6f);
		attachChild(spatial);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		rotate(FastMath.PI * tpf, 0, 0);

		float deltaMove = FastMath.exp(speed * tpf) / 100;
		move(0, 0, -deltaMove);
	}

	@Override
	public ArrayList<ANode> collidesWith() {
		ArrayList<ANode> nodes = new ArrayList<ANode>();
		nodes.addAll(game.getUpdateables().getLasers());
		nodes.add(game.getShip());
		
		return nodes;
	}

	@Override
	public void onCollision(ANode collidedWith) {
		// Destroy itself
		if (collidedWith instanceof IDamageTaker) {
			((IDamageTaker) collidedWith).onDamage(getDamage());
			
			if (destroyOnCollision()) {
				destroy();
			}
		}
		if (collidedWith instanceof IDestroyable) {
			IDestroyable destroyable = (IDestroyable) collidedWith;
			
			if (destroyable.destroyOnCollision()) {
				destroyable.destroy();
			}
		}
		if (collidedWith instanceof IDamageMaker) {
			IDamageMaker damager = (IDamageMaker) collidedWith;
			onDamage(damager.getDamage());
			if (destroyOnCollision()) {
				destroy();
			}
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return spatial.getWorldBound();
	}

	@Override
	public int getDamage() {
		return -1;
	}

	public boolean destroyOnCollision() {
		return true;
	}

	@Override
	public void destroy() {
		super.destroy();
		game.getUpdateables().addExplosion(new ShockWaveExplosion(game, getLocalTranslation()));
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
	}

}
