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
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;

public class Banana extends ANode implements ICollidable, IDamageMaker, IDestroyable, IDamageTaker {
	
	Spatial s;
	Material m;
	float speed = 1f;
	float moveDistance = 0f;
	float timer = 0f;
	int health = 1;
	
	public Banana(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		s = assetManager.loadModel("banana.obj");

		m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		m.setColor("Color", ColorRGBA.Brown);
		m.setColor("GlowColor", ColorRGBA.Yellow);

		s.setMaterial(m);
		s.move(-6f, -6.3f, 0f);
		
		setShadowMode(ShadowMode.Cast);
		
		scale(0.6f);
		attachChild(s);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		timer += tpf;
		
		rotate(FastMath.PI * tpf, 0, 0);

		float deltaMove = FastMath.exp(speed * timer) / 100;
		this.move(0, 0, -deltaMove);
	}

	@Override
	public ArrayList<ANode> collidesWith() {
		ArrayList<ANode> nodes = new ArrayList<ANode>();
		nodes.addAll(game.getLasers());
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
		return s.getWorldBound();
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
		game.getRootNode().detachChild(this);
		
		ShockWaveExplosion e = new ShockWaveExplosion(game);
		e.setLocalTranslation(getLocalTranslation());
		game.addExplosion(e);
		
		active = false;
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
