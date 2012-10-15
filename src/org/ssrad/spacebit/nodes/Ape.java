package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IExplodeable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.animation.LoopMode;
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

public class Ape extends AbstractNode implements IDamageMaker, IDamageTaker, IDestroyable, IScoreGiver, ISpawnable, IExplodeable {
	
	private static int DEFAULT_HEALTH = 15;

	private Random random;
//	private float timer = 0f;
	private int health = DEFAULT_HEALTH;
	
	ParticleEmitter fire;
	
	ArrayList<Banana> bananas = new ArrayList<Banana>();
	
	public Ape(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		random = new Random();
		spatial = assetManager.loadModel("affe/affe.obj");

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex_ml = assetManager.loadTexture("affe/affe.png");
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
//		timer += tpf;	
		fire.setLocalTranslation(spatial.getLocalTranslation().add(0f, .5f, 0f));
		
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
//		if (timer > .7f) {
//			timer = 0f;
//			spawnRandomBananas();
//		}
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
			game.getUpdateables().add(new ShockWaveExplosion(game, banana.getLocalTranslation()));
			game.getRootNode().detachChild(banana);
		}
	}
	
	private void spawnRandomBananas() {
		if ((random.nextInt(10) > 7)) {
			Banana b = new Banana(game);
			addBanana(b);
			b.setLocalTranslation(getLocalTranslation().add(3f, 0, -7f));
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
		return -20;
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{ add(game.getShip()); }};
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public int getScore() {
		return 25;
	}

	@Override
	public boolean isScoreCounted() {
		return active == false;
	}

	@Override
	public boolean isReadyToSpawn() {
		return random.nextInt(20) > 14;
	}

	@Override
	public ArrayList<AbstractNode> getNodesPreventCollisionsWhenSpawn() {
		return game.getUpdateables().getAllObstracles();
	}

	@Override
	public void onExplode() {
		game.getUpdateables().add(new FireExplosion(game, getLocalTranslation()));
	}

	@Override
	public boolean isExplodeable() {
		return getLocalTranslation().z > game.getCamera().getLocation().z;
	}

}
