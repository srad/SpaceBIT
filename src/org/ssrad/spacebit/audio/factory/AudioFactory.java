package org.ssrad.spacebit.audio.factory;

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
		// TODO: choose random track order, for 3 different music
		case MUSIC:
			audioFilePath = "8bp088_04_nullsleep_valentine_final.ogg";
			break;
		case COINS:
			audioFilePath = "laser.wav";
			break;
		case EXPLOSION:
			audioFilePath = "";
			break;
		case HEARTS:
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
