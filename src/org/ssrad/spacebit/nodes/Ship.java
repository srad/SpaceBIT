package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Iterator;

import org.ssrad.spacebit.audio.GameAudio;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICoinTaker;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IExplodeable;
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
import com.jme3.system.AppSettings;

public class Ship extends AbstractNode implements IDamageTaker, ICoinTaker, IDestroyable, IDamageMaker, IScoreTaker, IExplodeable {

	ParticleEmitter centerJet;
	ParticleEmitter leftJet;
	ParticleEmitter rightJet;

	Boolean enableLeftJet = false, enableRightJet = false;
	
	private float moveFactor = 10f;
	
	public static final int MAX_HEALTH = 100;
	public static final int MAX_COINS = 100;
	private static final int MAX_LIVES = 5;
	
	private int coins = 0;
	private int health = 100;
	private int lives = 3;
	
	// AUDIO
	GameAudio laserSound;
	GameAudio heartSound;
	GameAudio coinSound;
	GameAudio explosionSound;
	
	private int score = 0;

	boolean left = false, right = false, up = false, down = false;

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
		light.setPosition(getLocalTranslation().add(0,0,5));

		ArrayList<AbstractNode> blackHoles = game.getUpdateables().get(BlackHole.class);

		for (Iterator<AbstractNode> iterator = blackHoles.iterator(); iterator.hasNext();) {
			BlackHole blackHole = (BlackHole) iterator.next();
			
			float d = (int) blackHole.getBounds().getCenter().subtract(getBounds().getCenter()).length();
			if (d < 18f) {
				// Move towards black holes
				Vector3f v = new Vector3f(blackHole.getWorldTranslation().multLocal(Vector3f.UNIT_Z));		
		        Vector3f blackHoleVec = v.mult(tpf/23f);

				move(blackHoleVec);
			}
		}
	}

	private void addShip() {
		spatial = this.assetManager.loadModel("ship6/ship6.obj");		
		material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
		
		material.setTexture("DiffuseMap", this.assetManager.loadTexture("ship6/ship6.png"));
		material.setTexture("NormalMap", this.assetManager.loadTexture("ship6/ship6_normals.png"));
		
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
		up = false;
		down = false;
		
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
		up = false;
		down = false;
		
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
		up = true;
		down = false;
		moveShip(getLocalTranslation().clone().add(0, 0, moveFactor * tpf));
	}
	
	public void down(float tpf) {
		down = true;
		up = false;
		moveShip(getLocalTranslation().clone().add(0, 0, -moveFactor * tpf));
	}
	
	/**
	 * Limits movements out of the screen.
	 * @param v New location.
	 */
	public void moveShip(Vector3f v) {
		int margin = 20;
		AppSettings screen = game.getSettings();
		
	    Vector3f screenCoords = game.getCamera().getScreenCoordinates(getLocalTranslation());
	    int sX1 = (int)Math.floor(screenCoords.getX());
	    int sY1 = (int)Math.floor(screenCoords.getY());
	    
	    // Don't move over screen edge
	    if ( ((sX1 <= margin) && left) ||
	    	 ((sX1 >= (screen.getWidth()-margin)) && right) ||
	    	 ((sY1 <= margin) && down) ||
	    	 ((sY1 >= (screen.getHeight()-margin)) && up)
	    	 ) {
	    	// SCREEN EDGE
	    } else {
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
		if (health < 0) {
			health = 0;
		}
	}

	@Override
	public void onCoins(int amount) {
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
			moveFactor += 3f;
		} else if (!enableRightJet) {
			enableRightJet = true;
			moveFactor += 3f;
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
			moveFactor -= 3f;
		} else if (enableRightJet) {
			enableRightJet = false;
			moveFactor -= 3f;
			rightJet.setCullHint(CullHint.Always);
		}
	}

	public boolean isDead() {
		return lives <= 0;
	}
	
	public void kill() {
		lives = 0;
		score = 0;
		active = false;
	}
	
	@Override
	public void onScore(int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}

	@Override
	public ArrayList<AbstractNode> collidesWith() {
		// Don't recursively depend on each others
		// collisions check, the other entities check
		// for collisions with this ship
		return null;
	}

	public void reInit() {
		active = true;
		health = MAX_HEALTH;
		moveFactor = 10f;
		
		game.getRootNode().attachChild(this);
		setLocalTranslation(new Vector3f(0, 0, game.getCamera().getLocation().z + 10f));
		addLight();
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	@Override
	public void onExplode() {
		game.getUpdateables().add(new FireExplosion(game, getLocalTranslation()));
	}

	@Override
	public boolean isExplodeable() {
		return true;
	}

}
