package org.ssrad.spacebit.nodes;

import java.util.ArrayList;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;

public class Asteroid extends ANode implements ICollidable, IDamageMaker, IDestroyable {
	
	public Asteroid(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		spatial = assetManager.loadModel("asteroid.obj");		
		
		// Lighted material
		Material m = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
		
		m.setTexture("DiffuseMap", this.assetManager.loadTexture("asteroid.png"));
		m.setTexture("NormalMap", this.assetManager.loadTexture("asteroid_normals.png"));
		
		m.setBoolean("UseMaterialColors", true);
		m.setColor("Specular", ColorRGBA.Yellow);
		m.setColor("Diffuse", ColorRGBA.White);
		m.setFloat("Shininess", 128f); // [1,128]//

		spatial.setMaterial(m);
		scale(1.4f);

		attachChild(spatial);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		rotate(FastMath.PI/8 * tpf, FastMath.PI/4 * tpf, FastMath.PI/2 * tpf);
	}

	@Override
	public int getDamage() {
		return -2;
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
		active = false;
	}

	@Override
	public BoundingVolume getBounds() {
		return spatial.getWorldBound();
	}

	public boolean destroyOnCollision() {
		return false;
	}

	@Override
	public void destroy() {
		// TODO: Asteroid
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

}
