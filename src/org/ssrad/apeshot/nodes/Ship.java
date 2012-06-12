package org.ssrad.apeshot.nodes;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICoinTaker;
import org.ssrad.apeshot.interfaces.IDamageMaker;
import org.ssrad.apeshot.interfaces.IDamageTaker;
import org.ssrad.apeshot.interfaces.IDestroyable;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;

public class Ship extends ANode implements IDamageTaker, ICoinTaker, IDestroyable, IDamageMaker {

	ParticleEmitter centerJet;
	ParticleEmitter leftJet;
	ParticleEmitter rightJet;
	
	private int points = 0;
	private int health = 100;
	
	boolean left = false, right = false;

	public Ship(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {
		addShip();
		addJets();
		addLight();
		
		setShadowMode(ShadowMode.Off);
	}

	@Override
	public void update(float tpf) {
		move(0, 0, scrollSpeed * tpf);
	}
	
	private void addShip() {
		s = this.assetManager.loadModel("ship6.obj");		
		m = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
		
		m.setTexture("DiffuseMap", this.assetManager.loadTexture("ship6.png"));
		m.setTexture("NormalMap", this.assetManager.loadTexture("ship6_normals.png"));
		
		m.setBoolean("UseMaterialColors", true);
		m.setColor("Specular", ColorRGBA.White);
		m.setColor("Diffuse", ColorRGBA.White);
		m.setFloat("Shininess", 50f); // [1,128]
		
		s.setMaterial(m);
		scale(2.5f);
		s.rotate(0, FastMath.PI, FastMath.PI/2);
		rotate(0, 0 , FastMath.PI/2);
		
		this.attachChild(s);
	}
	
	private void addJets() {
		// Center jet
		centerJet = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		
		Material mat_red = new Material(this.assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture", this.assetManager.loadTexture("Effects/Explosion/flame.png"));
		centerJet.setMaterial(mat_red);

		centerJet.setImagesX(2);
		centerJet.setImagesY(2);
		
		centerJet.setEndColor(ColorRGBA.Yellow);
		centerJet.setStartColor(ColorRGBA.Red);
		
		centerJet.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
		
		centerJet.setStartSize(2f);
		centerJet.setEndSize(0.2f);
		centerJet.setGravity(0, 0, 0);
		centerJet.setLowLife(0.4f);
		centerJet.setHighLife(1f);
		
		centerJet.getParticleInfluencer().setVelocityVariation(0.4f);
		
		attachChild(centerJet);
		centerJet.move(0, 0, -1.5f);
		
		// Left jet
		leftJet = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		
		mat_red = new Material(this.assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture", this.assetManager.loadTexture("Effects/Explosion/flame.png"));
		leftJet.setMaterial(mat_red);

		leftJet.setImagesX(2);
		leftJet.setImagesY(2);
		
		leftJet.setEndColor(ColorRGBA.Red);
		leftJet.setStartColor(ColorRGBA.Green);
		
		leftJet.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
		
		leftJet.setStartSize(1f);
		leftJet.setEndSize(0.1f);
		leftJet.setGravity(0, 0, 0);
		leftJet.setLowLife(0.4f);
		leftJet.setHighLife(1f);
		
		leftJet.getParticleInfluencer().setVelocityVariation(0.4f);
		
		attachChild(leftJet);
		leftJet.move(0f, -1.3f, -.6f);
		
		rightJet = leftJet.clone();
		attachChild(rightJet);
		rightJet.move(0, 2.75f, 0);
	}
	
	private void addLight() {
		PointLight pl = new PointLight();
		pl.setColor(ColorRGBA.White);
		pl.setRadius(200f);
		pl.setPosition(s.getLocalTranslation().clone().addLocal(3, 2, 3));
		
		s.addLight(pl);
	}
	
	public Spatial getSpatial() {
		return s;
	}	
	
	public void incPoints() {
		points += 1;
	}
	
	public void incPoints(int points) {
		this.points += points;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getHealth() {
		return health;
	}

	public void left() {
		if (!left) {
			float rotFactor = 1f;
			if (right) {
				right = false;
				rotFactor = 2f;
			}
			left = true;
			rotate(0, 0, -FastMath.PI/12 * rotFactor);
		}
	}

	public void right() {
		if (!right) {
			float rotFactor = 1f;
			if (left) {
				left = false;
				rotFactor = 2f;
			}
			right = true;
			rotate(0, 0, FastMath.PI/12 * rotFactor);
		}
	}

	public void resetRotation() {
		float sign = 1f;
		
		if (left) {
			left = false;
		}
		if (right) {
			right = false;
			sign = -sign;
		}
		rotate(0, 0, FastMath.PI/12 * sign);
	}

	/**
	 * Notice that we have something that can give
	 * health and take health. So "damage" is added.
	 */
	@Override
	public void onDamage(int damage) {
		health += damage;
	}

	@Override
	public void takeCoins(int amount) {
		points += amount;
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public void destroy() {
		active = false;
		
		FireExplosion e = new FireExplosion(game);
		e.setLocalTranslation(getLocalTranslation());
		game.addFireExplosion(e);
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

	@Override
	public int getDamage() {
		// Damage on collision, let's say we make
		// as much damage as we have health yet.
		return -health;
	}
	
}
