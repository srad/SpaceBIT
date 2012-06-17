package org.ssrad.spacebit.nodes;

import org.ssrad.spacebit.audio.GameAudio;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICoinTaker;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreTaker;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;

public class Ship extends ANode implements IDamageTaker, ICoinTaker, IDestroyable, IDamageMaker, IScoreTaker {

	ParticleEmitter centerJet;
	ParticleEmitter leftJet;
	ParticleEmitter rightJet;
	
	Boolean enableLeftJet = false, enableRightJet = false;
	
	private float moveFactor = 10f;
	
	public static final int MAX_HEALTH = 100;
	public static final int MAX_COINS = 100;
	private static final int MAX_LIVES = 5;
	
	private static final float SHIP_MARGIN = 5f;
	
	private int coins = 0;
	private int health = 100;
	private int lives = 2;
	
	// AUDIO
	GameAudio laserSound;
	GameAudio heartSound;
	GameAudio coinSound;
	GameAudio explosionSound;
	
	private int score = 0;

	boolean left = false, right = false;

	public Ship(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {
		addShip();
		addJets();
		addLight();
		
		setShadowMode(ShadowMode.Cast);
	}

	@Override
	public void update(float tpf) {	
		move(0, 0, scrollSpeed * tpf);
		light.setPosition(getLocalTranslation().clone().add(0,0,5));
	}

	private void addShip() {
		spatial = this.assetManager.loadModel("ship6.obj");		
		material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
		
		material.setTexture("DiffuseMap", this.assetManager.loadTexture("ship6.png"));
		material.setTexture("NormalMap", this.assetManager.loadTexture("ship6_normals.png"));
		
		material.setBoolean("UseMaterialColors", true);
		material.setColor("Specular", ColorRGBA.White);
		material.setColor("Diffuse", ColorRGBA.White);
		material.setFloat("Shininess", 50f); // [1,128]
		
		spatial.setMaterial(material);
		scale(2.5f);
		spatial.rotate(0, FastMath.PI, FastMath.PI/2);
		rotate(0, 0 , FastMath.PI/2);
		
		attachChild(spatial);
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
		
		leftJet.setCullHint(CullHint.Always);
		rightJet.setCullHint(CullHint.Always);
	}
	
	private void addLight() {
		light = new PointLight();
		light.setColor(ColorRGBA.White);
		light.setRadius(200f);
		light.setPosition(spatial.getLocalTranslation().clone().addLocal(-5, 5, -3));
		
		game.getRootNode().addLight(light);
	}
	
	public Spatial getSpatial() {
		return spatial;
	}	
	
	public void incCoins() {
		coins += 1;
	}
	
	public void incLives() {
		incLives(1);
	}
	
	public void incLives(int count) {
		lives += count;
		if (lives > MAX_LIVES) {
			lives = MAX_LIVES;
		}
	}
	
	public void incCoins(int amount) {
		this.coins += amount;
		if (coins == MAX_COINS) {
			incLives();
			coins = 0;
		}
	}
	
	public int getCoins() {
		return coins;
	}
	
	public int getHealth() {
		return health;
	}

	public void left(float tpf) {
		if (!left) {
			float rotFactor = 1f;
			if (right) {
				right = false;
				rotFactor = 2f;
			}
			left = true;
			rotate(0, 0, -FastMath.PI/12 * rotFactor);
		}
		moveShip(getLocalTranslation().clone().add(moveFactor * tpf, 0, 0));
	}

	public void right(float tpf) {
		if (!right) {
			float rotFactor = 1f;
			if (left) {
				left = false;
				rotFactor = 2f;
			}
			right = true;
			rotate(0, 0, FastMath.PI/12 * rotFactor);
		}
		moveShip(getLocalTranslation().clone().add(-moveFactor * tpf, 0, 0));
	}
	
	public void up(float tpf) {
		moveShip(getLocalTranslation().clone().add(0, 0, moveFactor * tpf));
	}
	
	public void down(float tpf) {
		moveShip(getLocalTranslation().clone().add(0, 0, -moveFactor * tpf));
	}
	
	/**
	 * Limits movements out of the screen.
	 * @param v New location.
	 */
	public void moveShip(Vector3f v) {
		if (!(
				v.z < game.getCamera().getLocation().z + SHIP_MARGIN
				|| ((getLocalTranslation().x) > 31f && !right)
				|| ((getLocalTranslation().x) < -31f && !left)
				)
			) {
			setLocalTranslation(v);
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
		
		if (damage < 0) {
			unwarp();
		}		
		if (health > MAX_HEALTH) {
			health = MAX_HEALTH;
		}
	}

	@Override
	public void takeCoins(int amount) {
		incCoins(amount);
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public void destroy() {
		super.destroy();
		incLives(-1);
		game.getUpdateables().addFireExplosion(new FireExplosion(game, getLocalTranslation()));
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

	public int getLives() {
		return lives;
	}

	/**
	 * Enables the jets one by one.
	 */
	public void warp() {
		if (!enableLeftJet) {
			enableLeftJet = true;
			leftJet.setCullHint(CullHint.Never);
			moveFactor *= 1.3;
		} else if (!enableRightJet) {
			enableRightJet = true;
			moveFactor *= 1.3;
			rightJet.setCullHint(CullHint.Never);
		}
	}
	
	/**
	 * Disables the jets one by one.
	 */
	public void unwarp() {
		if (enableLeftJet) {
			enableLeftJet = false;
			leftJet.setCullHint(CullHint.Always);
			moveFactor *= 0.7;
		} else if (enableRightJet) {
			enableRightJet = false;
			moveFactor *= 0.7;
			rightJet.setCullHint(CullHint.Always);
		}
	}

	public boolean isDead() {
		return lives <= 0;
	}
	
	@Override
	public void onScore(int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}

}
