package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Planet extends AbstractNode implements IDamageMaker, IDamageTaker, IDestroyable, IScoreGiver {
	
	Random random;
	float scale;
	int health;
	
	public Planet(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		random = new Random();
		
		spatial = this.assetManager.loadModel("planet/planet.obj");	

		material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
		material.setTexture("DiffuseMap", assetManager.loadTexture("planet/planet.png"));
		material.setTexture("NormalMap", assetManager.loadTexture("planet/planet_normals.png"));

		spatial.rotate(-FastMath.PI * 0.9f, 0, FastMath.PI/(random.nextFloat() * 10f + 7f));
		move((random.nextFloat() * 5f), (random.nextFloat() * 5f), (random.nextFloat() * 5f));
		
		setShadowMode(ShadowMode.Off);
		
		this.scale = random.nextFloat() * 3f + 2f;
		spatial.scale(this.scale);
		attachChild(spatial);
		
		// Health depends on size
		this.health = (int) Math.round(this.scale) * 15;
		
		light = new PointLight();
		light.setRadius(30f);
		light.setColor(ColorRGBA.Brown);
		game.getRootNode().addLight(light);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		spatial.rotate(0, FastMath.PI * tpf, 0);
		light.setPosition(getLocalTranslation());
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{ add(game.getShip()); }};
	}
	
	@Override
	public void destroy() {
		super.destroy();
		game.getUpdateables().addShockWaveExplosion(new ShockWaveExplosion(game, getLocalTranslation()));
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

}
