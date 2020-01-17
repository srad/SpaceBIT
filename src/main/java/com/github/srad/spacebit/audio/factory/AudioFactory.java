package com.github.srad.spacebit.audio.factory;

import com.github.srad.spacebit.audio.enums.SoundType;
import com.github.srad.spacebit.main.SpaceBit;
import com.jme3.audio.AudioNode;

import java.util.Random;

public class AudioFactory {

  /**
   * Factory main method.
   *
   * @param soundType {@link SoundType} of returned {@link AudioNode}.
   * @return {@link AudioNode}
   */
  public static AudioNode create(SoundType soundType) {
    String audioFilePath;

    switch (soundType) {
    case MUSIC:
      // Pick a random track
      Random r = new Random();
      audioFilePath = "music/" + (r.nextInt(3) + 1) + ".ogg";
      break;
    case COINS:
    case LASER:
      audioFilePath = "sounds/laser.wav";
      break;
    case EXPLOSION:
      audioFilePath = "sounds/explosion.wav";
      break;
    case HEART:
      audioFilePath = "sounds/shortpitch.wav";
      break;
    default:
      return null;
    }
    return new AudioNode(SpaceBit.getInstance().getAssetManager(), audioFilePath, false);
  }

}
