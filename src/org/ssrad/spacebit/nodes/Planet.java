package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;

import com.jme3.bounding.BoundingVolume;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Planet extends ANode implements ICollidable, IDamageMaker {
	
	Random random;
	
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
		
		spatial.scale(3.0f);
		attachChild(spatial);
		
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
	public ArrayList<ANode> collidesWith() {
		return new ArrayList<ANode>() {{ add(game.getShip()); }};
	}

	@Override
	public void onCollision(ANode collidedWith) {
		if (collidedWith instanceof IDamageTaker) {
			((IDamageTaker) collidedWith).onDamage(getDamage());
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return spatial.getWorldBound();
	}

	@Override
	public int getDamage() {
		return 1000;
	}

}
