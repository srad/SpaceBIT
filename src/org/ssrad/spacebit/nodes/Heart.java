package org.ssrad.spacebit.nodes;

import java.util.ArrayList;

import org.ssrad.spacebit.audio.GameAudio;
import org.ssrad.spacebit.audio.enums.SoundType;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IAudible;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;

public class Heart extends AbstractNode implements IDamageMaker, IDestroyable, IScoreGiver, IAudible {
	
	public Heart(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		material.setColor("Color", ColorRGBA.Red);
		material.setColor("GlowColor", ColorRGBA.Red);
		
		spatial = assetManager.loadModel("heart.obj");		
		spatial.setMaterial(material);
		attachChild(spatial);
		addLight();
		
		setShadowMode(ShadowMode.Cast);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);		
		spatial.rotate(0, FastMath.PI * tpf * 2.5f, 0);
		light.setPosition(getLocalTranslation());
	}
	
	private void addLight() {
		light = new PointLight();
		light.setColor(ColorRGBA.Red);
		light.setRadius(20f);

		game.getRootNode().addLight(light);
	}

	@SuppressWarnings("serial")
	@Override
	public ArrayList<AbstractNode> collidesWith() {
		return new ArrayList<AbstractNode>() {{ add(game.getShip()); }};
	}

	@Override
	public int getDamage() {
		return 7;
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
		return active == false;
	}

	@Override
	public boolean playSoundOnDestroy() {
		return true;
	}

	@Override
	public void playAudio() {
		GameAudio audio = new GameAudio(game, this, SoundType.HEART);
		//audio.setVolume(1f);
		audio.play();
	}

}
