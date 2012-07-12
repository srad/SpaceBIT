package org.ssrad.spacebit.nodes;

import java.util.ArrayList;

import org.ssrad.spacebit.audio.GameAudio;
import org.ssrad.spacebit.audio.enums.SoundType;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreTaker;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;

public class Laser extends AbstractNode implements IDamageMaker, IDestroyable, IScoreTaker {

	float moveDistance = 0f;
	float timer = 0.5f;
	
	GameAudio audio;

	public Laser(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {	
		Cylinder c = new Cylinder(8, 10, .15f, 4f);
		spatial = new Geometry("c", c);
		
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Red);
		material.setColor("GlowColor", ColorRGBA.Pink);		

		spatial.setMaterial(material);

		attachChild(spatial);
		setLocalTranslation(game.getShip().getLocalTranslation());
		move(0, 0, 6f);
		
		audio = new GameAudio(game, this, SoundType.LASER);		
		//audio.setVolume(2f);
		audio.play();
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		timer += tpf;

		float deltaMove = FastMath.exp(timer) / 2.5f;
		
		moveDistance += deltaMove ;
		
		move(0, 0, deltaMove);
		
		if (moveDistance > 80f) {
			active = false;
		}
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{
			addAll(game.getUpdateables().get(Ape.class));
			addAll(game.getUpdateables().get(Ufo.class));
			addAll(game.getUpdateables().get(Planet.class));
		}};
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
	public void onScore(int score) {
		// The laser represent the ship
		// so it sends its score to the ship
		game.getShip().onScore(score);
	}

}
