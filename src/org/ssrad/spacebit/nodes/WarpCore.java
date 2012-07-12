package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class WarpCore extends AbstractNode implements IDestroyable, IScoreGiver, ISpawnable {

	Random random;
		
	public WarpCore(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		random = new Random();
		
		spatial = this.assetManager.loadModel("warpcore/warpcore.obj");	

		material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
		material.setTexture("DiffuseMap", assetManager.loadTexture("warpcore/warpcore.png"));
		material.setTexture("NormalMap", assetManager.loadTexture("warpcore/warpcore.png"));
		material.setTexture("GlowMap", assetManager.loadTexture("warpcore/warpcore_specular.png"));
		material.setColor("GlowColor", ColorRGBA.White);

		setShadowMode(ShadowMode.Cast);
		
		spatial.scale(0.8f);
		attachChild(spatial);

		light = new PointLight();
		light.setRadius(30f);
		light.setColor(ColorRGBA.White);
		game.getRootNode().addLight(light);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		light.setPosition(getLocalTranslation());
		spatial.rotate(FastMath.PI/(random.nextInt(5) + 5) * tpf, FastMath.PI/(random.nextInt(5) + 5) * tpf, FastMath.PI/(random.nextInt(5) + 5) * tpf);
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{ add(game.getShip()); }};
	}

	@Override
	public void onCollision(AbstractNode collidedWith) {
		active = false;
		game.getShip().warp();
	}

	@Override
	public boolean destroyOnCollision() {
		return true;
	}

	@Override
	public int getScore() {
		return 5;
	}

	@Override
	public boolean isScoreCounted() {
		return true;
	}
	
	@Override
	public boolean isReadyToSpawn() {
		return random.nextInt(20) > 18;
	}

	@Override
	public ArrayList<AbstractNode> getCollisionAvoiders() {
		return null;
	}

}
