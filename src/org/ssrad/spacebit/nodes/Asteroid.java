package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDestroyable;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class Asteroid extends AbstractNode implements IDamageMaker, IDestroyable {

	Random random;
	
	public Asteroid(Game game) {
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
		
		spatial = this.assetManager.loadModel("asteroid/asteroid.obj");		
		material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
		
		material.setTexture("DiffuseMap", this.assetManager.loadTexture("asteroid/asteriod.png"));
		material.setTexture("NormalMap", this.assetManager.loadTexture("asteroid/asteriod_normals.png"));
		
		material.setBoolean("UseMaterialColors", true);
		material.setColor("Specular", ColorRGBA.Brown);
		material.setColor("Diffuse", ColorRGBA.Brown);
		material.setFloat("Shininess", 128f); // [1,128]
		
		spatial.setMaterial(material);
		scale(random.nextFloat() * 3f + 1f);

		attachChild(spatial);
		addLight();
		
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
	
	private void addLight() {
		light = new PointLight();
		light.setColor(ColorRGBA.White);
		light.setRadius(200f);
		
		game.getRootNode().addLight(light);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		spatial.rotate(FastMath.PI/(random.nextInt(5) + 5) * tpf, FastMath.PI/(random.nextInt(5) + 5) * tpf, FastMath.PI/(random.nextInt(5) + 5) * tpf);
		light.setPosition(spatial.getLocalTranslation().clone().addLocal(0, 5, 0));
	}
	
	@Override
	public int getDamage() {
		return -1000;
	}

	@Override
	public boolean destroyOnCollision() {
		return false;
	}

}
