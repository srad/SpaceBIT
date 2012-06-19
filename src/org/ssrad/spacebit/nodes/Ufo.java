package org.ssrad.spacebit.nodes;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Ufo extends AbstractNode implements IDestroyable, IDamageMaker, IDamageTaker {
	
	private int health = 20;

	public Ufo(Game game) {
		super(game);
	}
	
	ArrayList<Laser> lasers = new ArrayList<Laser>();

	@Override
	protected void init() {
		spatial = this.assetManager.loadModel("ufo2/ufo2.obj");	

		material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
		material.setTexture("DiffuseMap", assetManager.loadTexture("ufo2/ufo2.png"));
		material.setTexture("NormalMap", assetManager.loadTexture("ufo2/ufo2_normals.png"));

		spatial.rotate(FastMath.PI, 0, 0);
		
		setShadowMode(ShadowMode.Cast);
		
		spatial.scale(2.6f);
		attachChild(spatial);
		
		light = new PointLight();
		light.setRadius(30f);
		light.setColor(ColorRGBA.Gray);
		game.getRootNode().addLight(light);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		spatial.rotate(0, FastMath.PI * tpf * 2.5f, 0);
		light.setPosition(getLocalTranslation().clone().add(0, 0, 0));
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public void destroy() {
		super.destroy();
		game.getUpdateables().addFireExplosion(new FireExplosion(game, getLocalTranslation()));		
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
	}

	@Override
	public int getDamage() {
		return -30;
	}

	@Override
	public ArrayList<AbstractNode> collidesWith() {
		ArrayList<AbstractNode> n = new ArrayList<AbstractNode>();
		
		n.addAll(game.getUpdateables().getLasers());
		n.add(game.getShip());
		
		return n;
	}

}
