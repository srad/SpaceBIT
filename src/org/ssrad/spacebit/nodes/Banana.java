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
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Banana extends AbstractNode implements ICollidable, IDamageMaker, IDestroyable, IDamageTaker {
	
	float speed = 1f;
	int health = 1;
	
	public Banana(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		spatial = assetManager.loadModel("banana.obj");

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Brown);
		material.setColor("GlowColor", ColorRGBA.Yellow);

		spatial.setMaterial(material);
		spatial.move(-6f, -6.3f, 0f);
		
		setShadowMode(ShadowMode.Cast);
		
		scale(0.6f);
		attachChild(spatial);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		rotate(FastMath.PI * tpf, 0, 0);

		float deltaMove = FastMath.exp(speed * tpf) / 10;
		move(0, 0, -deltaMove);
	}

	@Override
	public ArrayList<AbstractNode> collidesWith() {
		ArrayList<AbstractNode> nodes = new ArrayList<AbstractNode>();
		nodes.addAll(game.getUpdateables().getLasers());
		nodes.add(game.getShip());
		
		return nodes;
	}

	@Override
	public BoundingVolume getBounds() {
		return spatial.getWorldBound();
	}

	@Override
	public int getDamage() {
		return -1;
	}

	public boolean destroyOnCollision() {
		return true;
	}

	@Override
	public void destroy() {
		super.destroy();
		game.getUpdateables().addShockWaveExplosion(new ShockWaveExplosion(game, getLocalTranslation()));
	}

	@Override
	public void onDamage(int damage) {
		health += damage;
	}

}
