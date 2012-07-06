package org.ssrad.spacebit.audio;

import org.ssrad.spacebit.audio.enums.SoundType;
import org.ssrad.spacebit.audio.factory.AudioFactory;
import org.ssrad.spacebit.game.Game;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class GameAudio {

	/** Parent node to which this AudioNode will be attached */
	private Node parentNode;

	/** Is taken from the {@link Game#getInstance()} */
	private AssetManager assetManager;

	/** Actual {@link AudioNode} which holds the sound. */
	private AudioNode audioNode;

	/** One of the predefined {@link SoundType} we have. */
	private SoundType soundType;

	/**
	 * Expects the parent node to which it will be attached and the
	 * {@link SoundType} that will be used.
	 * 
	 * @param parentNode
	 *            Will be attached to this.
	 * @param soundType
	 *            Which sound or music is represented by this AudioNode.
	 */
	public GameAudio(Game game, Node parentNode, SoundType soundType) {
		this.assetManager = game.getAssetManager();
		this.parentNode = parentNode;

		this.audioNode = AudioFactory.create(soundType);
		this.setSoundType(soundType);

		audioNode.setPositional(true);
		this.setPosition(Vector3f.ZERO);

		audioNode.setLooping(false);
		audioNode.setVolume(10);

		this.parentNode.attachChild(audioNode);
	}

	/**
	 * Plays the audio.
	 */
	public void play() {
		this.audioNode.play();
	}

	/**
	 * Stops the sound play back.
	 */
	public void stop() {
		this.audioNode.stop();
	}

	public boolean isPositional() {
		return this.audioNode.isPositional();
	}

	public void setPositional(boolean positional) {
		this.audioNode.setPositional(positional);
	}

	public Vector3f getPosition() {
		return this.audioNode.getLocalTranslation();
	}

	public void setPosition(Vector3f position) {
		this.audioNode.setLocalTranslation(position);
	}

	public SoundType getSoundType() {
		return soundType;
	}

	public void setSoundType(SoundType soundType) {
		this.soundType = soundType;
	}

	public boolean isLoopMode() {
		return this.audioNode.isLooping();
	}

	public void setLoopMode(boolean loopMode) {
		this.audioNode.setLooping(loopMode);
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public float getVolume() {
		return this.audioNode.getVolume();
	}

	public void setVolume(float volume) {
		this.audioNode.setVolume(volume);
	}
	
	public void detachFromParent() {
		this.parentNode.detachChild(audioNode);
	}

}