package org.ssrad.apeshot.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICollidable;
import org.ssrad.apeshot.interfaces.IDamageMaker;
import org.ssrad.apeshot.interfaces.IDamageTaker;
import org.ssrad.apeshot.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.texture.Texture;

public class Ape extends ANode implements ICollidable, IDamageMaker, IDamageTaker, IDestroyable {
	
	Random random = new Random();
	float timer = 0f;
	private int health = 15;
	
	ArrayList<Banana> bananas = new ArrayList<Banana>();
	
	public Ape(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		s = assetManager.loadModel("affe.obj");

		m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex_ml = assetManager.loadTexture("affe.png");
		m.setTexture("ColorMap", tex_ml);
		s.setMaterial(m);
		
		rotate(-FastMath.PI/12, FastMath.PI, 0);
		addFire();
		scale(2.4f);
		
		setShadowMode(ShadowMode.CastAndReceive);
		
		attachChild(s);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		timer += tpf;
		
		// Update bananas
		for (Iterator<Banana> it = bananas.iterator(); it.hasNext();) {
			Banana banana = (Banana) it.next();
			
			if (!banana.isActive()) {
				it.remove();
				banana.destroy();
			} else {
				banana.update(tpf);
			}
		}
		spawnRandomBananas();
	}
	
	/**
	 * Removes all current ape's bananas.
	 * Required for example when colliding with
	 */
	public void removeAllBananas() {
		// Update bananas
		for (Iterator<Banana> it = bananas.iterator(); it.hasNext();) {
			Banana banana = (Banana) it.next();
			it.remove();
			game.getRootNode().detachChild(banana);
		}
	}
	
	private void spawnRandomBananas() {
		if (random.nextInt(10) > 5 && (timer % 1d <= 0.01d)) {
			Banana b = new Banana(game);
			addBanana(b);
			b.setLocalTranslation(getLocalTranslation().clone().add(3f, 0, -7f));
		}
	}
	
	private void addFire() {
		
		ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);

		Material mat_red = new Material(this.assetManager,
				"Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture",
				this.assetManager.loadTexture("Effects/Explosion/flame.png"));
		fire.setMaterial(mat_red);

		fire.setImagesX(2);
		fire.setImagesY(2);

		fire.setEndColor(ColorRGBA.Yellow);
		fire.setStartColor(ColorRGBA.Brown);

		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, -15f));

		fire.setStartSize(4.5f);
		fire.setEndSize(.3f);
		fire.setGravity(0, 0, 0);
		fire.setLowLife(0.4f);
		fire.setHighLife(1f);

		fire.getParticleInfluencer().setVelocityVariation(0.4f);

		this.attachChild(fire);
		fire.move(0f, .5f, 0f);
		
	}
	
	private void addBanana(Banana banana) {
		bananas.add(banana);
		game.getRootNode().attachChild(banana);
		banana.setLocalTranslation(getLocalTranslation());
	}

	@Override
	public int getDamage() {
		return -10;
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

	@Override
	public void onDamage(int damage) {
		health += damage;
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0f;
	}

	@Override
	public void destroy() {
		FireExplosion e = new FireExplosion(game);
		e.setLocalTranslation(getLocalTranslation());
		game.addFireExplosion(e);
		
		removeAllBananas();
		
		game.getRootNode().detachChild(this);		
		active = false;
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

}
