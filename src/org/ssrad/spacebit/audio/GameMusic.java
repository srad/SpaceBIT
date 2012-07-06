package org.ssrad.spacebit.audio;

import org.ssrad.spacebit.audio.enums.SoundType;
import org.ssrad.spacebit.game.Game;

public class GameMusic extends GameAudio {

	/** Determine internally if the music is currently playing. */
	private Boolean isPlaying = false;

	public GameMusic(Game game) {
		super(game, game.getRootNode(), SoundType.MUSIC);
		setLoopMode(true);
	}

	/** Stops the music if it is playing. */
	public void stop() {
		this.isPlaying = false;
		super.stop();
	}

	/** If the music is playing it will stop. */
	@Override
	public void play() {
		if (!this.isPlaying) {
			super.play();
			this.isPlaying = true;
		}
	}

}