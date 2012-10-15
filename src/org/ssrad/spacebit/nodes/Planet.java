package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IExplodeable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.texture.Texture;

public class Planet extends AbstractNode implements IDamageMaker, IDamageTaker, IDestroyable, IScoreGiver, ISpawnable, IExplodeable {
	
	Random random;
	float scale;
	int health;
	
	public Planet(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		random = new Random();
		
		spatial = assetManager.loadModel("planet/planet.obj");

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex_ml = assetManager.loadTexture("planet/planet.png");
		material.setTexture("ColorMap", tex_ml);
		spatial.setMaterial(material);

		spatial.rotate(-FastMath.PI * 0.9f, 0, FastMath.PI/(random.nextFloat() * 10f + 7f));
		spatial.rotate(FastMath.PI, 0, 0);
		
		move((random.nextFloat() * 5f), (random.nextFloat() * 5f), (random.nextFloat() * 5f));
		
		setShadowMode(ShadowMode.Off);
		
		this.scale = random.nextFloat() * 3f + 2f;
		spatial.scale(this.scale);
		attachChild(spatial);
		
		// Health depends on size
		this.health = (int) Math.round(this.scale) * 15;
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		spatial.rotate(0, FastMath.PI * tpf, 0);
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{ add(game.getShip()); }};
	}
	
	@Override
	public int getDamage() {
		return -health;
	}

	@Override
	public int getScore() {
		return (int) Math.round(this.scale) * 10;
	}

	@Override
	public boolean isScoreCounted() {
		return active == false;
	}

	@Override
	public boolean destroyOnCollision() {
		return this.health <= 0f;
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
	}
	
	@Override
	public boolean isReadyToSpawn() {
		return random.nextInt(30) > 27;
	}

	@Override
	public ArrayList<AbstractNode> getNodesPreventCollisionsWhenSpawn() {
		return game.getUpdateables().getAllObstracles();
	}

	@Override
	public void onExplode() {
		game.getUpdateables().add(new ShockWaveExplosion(game, getLocalTranslation()));		
	}

	@Override
	public boolean isExplodeable() {
		return getLocalTranslation().z > game.getCamera().getLocation().z;
	}

}
