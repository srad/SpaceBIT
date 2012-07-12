package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;

public class BlackHole extends AbstractNode implements IDamageMaker, ISpawnable {

	Random random;
	float size;
	
	public BlackHole(Game game) {
		super(game);
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{ add(game.getShip()); }};
	}

	@Override
	protected void init() {
		random = new Random();
		
		spatial = this.assetManager.loadModel("blackhole/blackhole2.obj");	
		
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex_ml = assetManager.loadTexture("blackhole/blackhole2.png");
		material.setTexture("ColorMap", tex_ml);

		spatial.setMaterial(material);

		spatial.setMaterial(material);
		size = random.nextFloat() * 2f + 2f;
		scale(size);
		attachChild(spatial);
		
		// Add particles
		ParticleEmitter particle = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 100);
		
		Material mat_red = new Material(this.assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture", this.assetManager.loadTexture("Effects/Explosion/flame.png"));
		particle.setMaterial(mat_red);

		particle.setImagesX(2);
		particle.setImagesY(2);
		
		particle.setEndColor(ColorRGBA.White);
		particle.setStartColor(ColorRGBA.Black);
		
		particle.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
		
		particle.setStartSize(0.1f);
		particle.setEndSize(1.0f);
		particle.setGravity(0, 0, 0);
		particle.setLowLife(0.5f);
		particle.setHighLife(.6f);
		
		particle.getParticleInfluencer().setVelocityVariation(0.3f);
		
		attachChild(particle);
		spatial.move(0, -1f, 0);
		particle.move(1f, 1f, 1f);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		rotate(0, 2 * FastMath.PI * tpf, 0);
	}
	
	@Override
	public int getDamage() {
		return -((int) size);
	}

	@Override
	public boolean isReadyToSpawn() {
		return random.nextInt(20) > 19;
	}

	@Override
	public ArrayList<AbstractNode> getCollisionAvoiders() {
		return game.getUpdateables().getAllObstracles();
	}

}
