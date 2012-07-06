package org.ssrad.spacebit.audio.factory;

import java.util.Random;

import org.ssrad.spacebit.audio.enums.SoundType;
import org.ssrad.spacebit.main.SpaceBit;

import com.jme3.audio.AudioNode;

public class AudioFactory {

	/**
	 * Factory main method.
	 * 
	 * @param soundType
	 *            {@link SoundType} of returned {@link AudioNode}.
	 * @return {@link AudioNode}
	 */
	public static AudioNode create(SoundType soundType) {
		String audioFilePath = "";

		switch (soundType) {
		case MUSIC:
			// Pick a random track
			Random r = new Random();
			audioFilePath = (r.nextInt(3)+1) + ".ogg";
			break;
		case COINS:
			audioFilePath = "laser.wav";
			break;
		case EXPLOSION:
			audioFilePath = "explosion.wav";
			break;
		case HEART:
			audioFilePath = "shortpitch.wav";
			break;
		case LASER:
			audioFilePath = "laser.wav";
			break;
		default:
			return null;
		}
		return new AudioNode(SpaceBit.getInstance().getAssetManager(), audioFilePath, false);
	}

}
