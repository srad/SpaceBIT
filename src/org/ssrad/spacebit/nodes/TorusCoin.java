package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICoinGiver;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;


public class TorusCoin extends AbstractNode implements ICoinGiver, IDestroyable, IScoreGiver, ISpawnable {
	
	public TorusCoin(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {
		spatial = assetManager.loadModel("toruscoin/ring.obj");		

		// Simple glow material looks nicer than texture
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		material.setColor("Color", ColorRGBA.Yellow);
		material.setColor("GlowColor", ColorRGBA.Green);
		
		Texture tex_ml = assetManager.loadTexture("toruscoin/ring.png");
		material.setTexture("ColorMap", tex_ml);
		
		spatial.setMaterial(material);
		scale(1.4f);
		attachChild(spatial);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);		
		spatial.rotate(0, FastMath.PI * tpf, 0);
	}
	
	public Spatial getSpatial() {
		return spatial;
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{ add(game.getShip()); }};
	}

	@Override
	public int getCoins() {
		return 1;
	}

	@Override
	public boolean destroyOnCollision() {
		return true;
	}

	@Override
	public int getScore() {
		return 1;
	}

	@Override
	public boolean isScoreCounted() {
		return active == false;
	}
	
	@Override
	public boolean isReadyToSpawn() {
		return (new Random()).nextInt(20) > 10;
	}

	@Override
	public ArrayList<AbstractNode> getCollisionAvoiders() {
		return null;
	}

}
