package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.texture.Texture;

public class WarpCore extends AbstractNode implements IDestroyable, IScoreGiver, ISpawnable {

	Random random;

	public WarpCore(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		random = new Random();
		
		spatial = assetManager.loadModel("warpcore2/warpcore2.obj");
		
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex_ml = assetManager.loadTexture("warpcore2/tex.png");
		material.setTexture("ColorMap", tex_ml);
		spatial.setMaterial(material);
		
		setShadowMode(ShadowMode.Cast);
		
		spatial.scale(0.8f);
		attachChild(spatial);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
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
		return random.nextInt(20) > 17;
	}

	@Override
	public ArrayList<AbstractNode> getNodesPreventCollisionsWhenSpawn() {
		return null;
	}

}
