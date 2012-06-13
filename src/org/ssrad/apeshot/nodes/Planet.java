package org.ssrad.apeshot.nodes;

import java.util.Random;

import org.ssrad.apeshot.game.Game;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Planet extends ANode {
	
	Random random;
	
	public Planet(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		random = new Random();
		
		s = this.assetManager.loadModel("planet/planet.obj");	

		m = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
		m.setTexture("DiffuseMap", assetManager.loadTexture("planet/planet.png"));
		m.setTexture("NormalMap", assetManager.loadTexture("planet/planet_normals.png"));

		s.rotate(-FastMath.PI * 0.9f, 0, FastMath.PI/(random.nextFloat() * 10f + 7f));
		move((random.nextFloat() * 5f), (random.nextFloat() * 5f), (random.nextFloat() * 5f));
		
		setShadowMode(ShadowMode.Off);
		
		s.scale(3.0f);
		attachChild(s);
		
		light = new PointLight();
		light.setRadius(30f);
		light.setColor(ColorRGBA.Brown);
		game.getRootNode().addLight(light);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		s.rotate(0, FastMath.PI * tpf, 0);
		light.setPosition(getLocalTranslation());
	}

	@Override
	public void destroy() {
		active = false;
		game.getRootNode().detachChild(this);
		game.getRootNode().removeLight(light);
	}

}
