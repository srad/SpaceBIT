package org.ssrad.apeshot.nodes;

import java.util.ArrayList;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICollidable;
import org.ssrad.apeshot.interfaces.IDamageMaker;
import org.ssrad.apeshot.interfaces.IDamageTaker;
import org.ssrad.apeshot.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Ufo extends ANode implements IDestroyable, IDamageMaker, IDamageTaker, ICollidable {
	
	private int health = 20;

	public Ufo(Game game) {
		super(game);
	}
	
	ArrayList<Laser> lasers = new ArrayList<Laser>();

	@Override
	protected void init() {
		s = this.assetManager.loadModel("ufo4/ufo4.obj");	
		
//		Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//	    Texture tex_ml = assetManager.loadTexture("ufo/ufo2.png");
//	    m.setTexture("ColorMap", tex_ml);
//		
		m = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
		m.setTexture("DiffuseMap", assetManager.loadTexture("ufo4/ufo4.png"));
		m.setTexture("NormalMap", assetManager.loadTexture("ufo4/ufo4_normals.png"));

		s.rotate(FastMath.PI / 10, 0, 0);
		
		setShadowMode(ShadowMode.Cast);
		
		s.scale(2.5f);
		attachChild(s);
		
		PointLight l = new PointLight();
		l.setRadius(1000f);
		l.setColor(ColorRGBA.White);
		l.setPosition(s.getLocalTranslation());
		addLight(l);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		s.rotate(0, FastMath.PI * tpf * 2f, 0);
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
