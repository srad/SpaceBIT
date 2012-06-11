package org.ssrad.apeshot.nodes;

import java.util.ArrayList;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICollidable;
import org.ssrad.apeshot.interfaces.IDamageMaker;
import org.ssrad.apeshot.interfaces.IDamageTaker;
import org.ssrad.apeshot.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.texture.Texture;

public class Ufo extends ANode implements IDestroyable, IDamageMaker, IDamageTaker, ICollidable {
	
	private int health = 20;

	public Ufo(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		s = this.assetManager.loadModel("ufo/ufo.obj");	
		
		Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    Texture tex_ml = assetManager.loadTexture("ufo/ufo2.png");
	    m.setTexture("ColorMap", tex_ml);
		
//		m = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
//		m.setTexture("DiffuseMap", assetManager.loadTexture("ufo/ufo2.png"));
//		m.setTexture("NormalMap", assetManager.loadTexture("ufo/ufo_normals.png"));
//		
//		m.setBoolean("UseMaterialColors", true);
//		m.setColor("Specular", ColorRGBA.Green);
//		m.setColor("Diffuse", ColorRGBA.Pink);
//		m.setFloat("Shininess", 128f); // [1,128]

		s.rotate(FastMath.PI/10, 0, 0);
		
		setShadowMode(ShadowMode.CastAndReceive);
		
		scale(1.4f);
		attachChild(s);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		rotate(0, FastMath.PI * tpf * 1.4f, 0);
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public void destroy() {
		active = false;				
		game.addFireExplosion(new FireExplosion(game, getLocalTranslation()));
		game.getRootNode().detachChild(this);
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
	}

	@Override
	public int getDamage() {
		return -10;
	}

	@Override
	public ArrayList<ANode> collidesWith() {
		ArrayList<ANode> n = new ArrayList<ANode>();
		n.addAll(game.getLasers());
		n.add(game.getShip());
		
		return n;
	}

	@Override
	public void onCollision(ANode collidedWith) {
		if (collidedWith instanceof IDamageTaker) {
			// Send this damage
			((IDamageTaker) collidedWith).onDamage(getDamage());
		}
		if (collidedWith instanceof IDamageMaker) {
			
			IDamageMaker damageMaker = (IDamageMaker) collidedWith;
			onDamage(damageMaker.getDamage());
			
			// Check for ape's self destruction
			if (destroyOnCollision()) {
				destroy();
			}
			
			if (damageMaker instanceof IDestroyable) {
				IDestroyable destroyable = (IDestroyable) damageMaker;
				if (destroyable.destroyOnCollision()) {
					destroyable.destroy();
				}
			}
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return s.getWorldBound();
	}

}
