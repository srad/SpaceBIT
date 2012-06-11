package org.ssrad.apeshot.nodes;

import java.util.ArrayList;

import org.ssrad.apeshot.game.Game;
import org.ssrad.apeshot.interfaces.ICollidable;
import org.ssrad.apeshot.interfaces.IDamageMaker;
import org.ssrad.apeshot.interfaces.IDamageTaker;
import org.ssrad.apeshot.interfaces.IDestroyable;

import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;

public class Laser extends ANode implements ICollidable, IDamageMaker, IDestroyable {

	float moveDistance = 0f;

	public Laser(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {	
		Cylinder c = new Cylinder(8, 10, .15f, 4f);
		Geometry rocket = new Geometry("c", c);
		
		m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		m.setColor("Color", ColorRGBA.Red);
		m.setColor("GlowColor", ColorRGBA.Pink);

		rocket.setMaterial(m);

		attachChild(rocket);
		setLocalTranslation(game.getShip().getLocalTranslation());
		move(0, 0, 6f);
	}
	
	@Override
	public void update(float tpf) {
		float deltaMove = FastMath.exp(tpf) / 2.5f;
		
		moveDistance += deltaMove;
		
		move(0, 0, deltaMove);
		
		if (moveDistance > 80f) {
			destroy();
		}
	}

	@Override
	public ArrayList<ANode> collidesWith() {
		return null;
	}

	@Override
	public void onCollision(ANode collidedWith) {
		if (collidedWith instanceof IDamageTaker) {
			((IDamageTaker) collidedWith).onDamage(getDamage());
						
			active = !destroyOnCollision();
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return s.getWorldBound();
	}

	@Override
	public int getDamage() {
		return -5;
	}

	@Override
	public boolean destroyOnCollision() {
		return true;
	}

	@Override
	public void destroy() {
		game.getRootNode().detachChild(this);		
		active = false;
	}

	@Override
	public boolean isDetroyable() {
		return true;
	}

}
