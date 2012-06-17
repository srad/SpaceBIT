package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.IScoreTaker;

import com.jme3.animation.LoopMode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionTrack;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.texture.Texture;

public class Ape extends ANode implements ICollidable, IDamageMaker, IDamageTaker, IDestroyable, IScoreGiver {
	
	private static int DEFAULT_HEALTH = 15;
	
	Random random;
	float timer = 0f;
	private int health = DEFAULT_HEALTH;
	
	ParticleEmitter fire;
	
	ArrayList<Banana> bananas = new ArrayList<Banana>();
	
	public Ape(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		random = new Random();
		spatial = assetManager.loadModel("affe.obj");

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex_ml = assetManager.loadTexture("affe.png");
		material.setTexture("ColorMap", tex_ml);
		spatial.setMaterial(material);
		
		rotate(-FastMath.PI/12, FastMath.PI, 0);
		addFire();
		scale(2.4f);
		
		setShadowMode(ShadowMode.CastAndReceive);

		attachChild(spatial);
		startRandomRotation();
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		timer += tpf;		
		fire.setLocalTranslation(spatial.getLocalTranslation().clone().add(0f, .5f, 0f));
		
//		if (bananas.size() > 0) {
//			// TODO: object not cleaned up, no clue why
//			// Update bananas
//			for (Iterator<Banana> it = bananas.iterator(); it.hasNext();) {
//				Banana banana = (Banana) it.next();
//				
//				if (!banana.isActive()) {
//					it.remove();
//					banana.destroy();
//				} else {
//					banana.update(tpf);
//				}
//			}
//		}
//		spawnRandomBananas();
	}
	
	private void startRandomRotation() {
		MotionPath path = new MotionPath();
		path.setCycle(true);
		
		path.addWayPoint(getLocalTranslation().add(-random.nextFloat()*5f + 0.3f, 0f, 0f));
		path.addWayPoint(getLocalTranslation().add(0f, 0f, -random.nextFloat()*5f + 0.3f));
		path.addWayPoint(getLocalTranslation().add(random.nextFloat()*5f + 0.3f, 0f, 0f));
		path.addWayPoint(getLocalTranslation().add(0f, 0f, random.nextFloat()*5f + 0.3f));

		MotionTrack motionControl = new MotionTrack(spatial,path);
		motionControl.setSpeed(6f);
		motionControl.setLoopMode(LoopMode.Cycle);
		motionControl.play();
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
			game.getUpdateables().addExplosion(new ShockWaveExplosion(game, banana.getLocalTranslation()));
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
		
		fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);

		Material mat_red = new Material(this.assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture", this.assetManager.loadTexture("Effects/Explosion/flame.png"));
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

		attachChild(fire);
		fire.move(0f, .5f, 0f);
		
	}
	
	private void addBanana(Banana banana) {
		bananas.add(banana);
		game.getRootNode().attachChild(banana);
		banana.setLocalTranslation(getLocalTranslation());
	}
	
	public ArrayList<Banana> getAllBananas() {
		return bananas;
	}

	@Override
	public int getDamage() {
		return -10;
	}

	@Override
	public ArrayList<ANode> collidesWith() {
		ArrayList<ANode> nodes = new ArrayList<ANode>();

		nodes.add(game.getShip());
		
		return nodes;
	}

	@Override
	public void onCollision(ANode collidedWith) {
		// Making damage
		if (collidedWith instanceof IDamageTaker) {
			// Send this damage
			((IDamageTaker) collidedWith).onDamage(getDamage());
		}
		// Taking damage
		if (collidedWith instanceof IDamageMaker) {
			
			IDamageMaker damageMaker = (IDamageMaker) collidedWith;
			onDamage(damageMaker.getDamage());
									
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
		return spatial.getWorldBound();
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
		
		if (destroyOnCollision()) {
			destroy();
		}
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public void destroy() {
		super.destroy();
		
		game.getUpdateables().addFireExplosion(new FireExplosion(game, getLocalTranslation()));
		removeAllBananas();
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

	@Override
	public int getScore() {
		return DEFAULT_HEALTH;
	}

}
