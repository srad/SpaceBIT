package org.ssrad.spacebit.nodes;

import java.util.ArrayList;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionTrack;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Ufo extends AbstractNode implements IDestroyable, IDamageMaker, IDamageTaker, IScoreGiver, ISpawnable {
	
	private int health = 20;
	private Random random;
	
	public Ufo(Game game) {
		super(game);
	}
	
	ArrayList<Laser> lasers = new ArrayList<Laser>();

	@Override
	protected void init() {
		random = new Random();
		spatial = this.assetManager.loadModel("ufo4/ufo4.obj");	

		material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");		
		material.setTexture("DiffuseMap", assetManager.loadTexture("ufo4/tex.png"));
		material.setTexture("NormalMap", assetManager.loadTexture("ufo4/normals.png"));

		spatial.rotate(FastMath.PI, 0, 0);
		
		setShadowMode(ShadowMode.Cast);
		
		spatial.scale(3.3f);
		spatial.rotate(-FastMath.PI/10, 0, FastMath.PI);
		attachChild(spatial);
		
		light = new PointLight();
		light.setRadius(40f);
		light.setColor(ColorRGBA.White);
		addLight(light);
		startRandomRotation();
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		spatial.rotate(0, FastMath.PI * tpf * 2.5f, 0);
	}
	
	private void startRandomRotation() {
		MotionPath path = new MotionPath();
		path.setCycle(true);
		
		Vector3f v = getLocalTranslation().clone();
		
		path.addWayPoint(v.addLocal(-random.nextFloat()*20f + 2f, 0f, 0f));
		path.addWayPoint(v.addLocal(0f, 0f, -random.nextFloat()*20f + 2f));
		path.addWayPoint(v.addLocal(random.nextFloat()*20f + 2f, 0f, 0f));
		path.addWayPoint(v.addLocal(0f, 0f, random.nextFloat()*20f + 2f));

		MotionTrack motionControl = new MotionTrack(spatial,path);
		motionControl.setSpeed(3f);
		motionControl.setLoopMode(LoopMode.Cycle);
		motionControl.play();
	}

	@Override
	public boolean destroyOnCollision() {
		return health <= 0;
	}

	@Override
	public void destroy() {
		super.destroy();
		game.getUpdateables().add(new FireExplosion(game, getLocalTranslation()));		
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
		
		n.addAll(game.getUpdateables().get(Laser.class));
		n.add(game.getShip());
		
		return n;
	}

	@Override
	public int getScore() {
		return 35;
	}

	@Override
	public boolean isScoreCounted() {
		return health <= 0;
	}
	
	@Override
	public boolean isReadyToSpawn() {
		return random.nextInt(20) > 16;
	}

	@Override
	public ArrayList<AbstractNode> getNodesPreventCollisionsWhenSpawn() {
		return game.getUpdateables().getAllObstracles();
	}

}
