package org.ssrad.spacebit.game;

import java.io.Serializable;

/**
 * Stores the game settings which will be restores on the next game start.
 */
public class GameSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Volumes
     */
    private float soundVolume = 1, musicVolume = 1;

    /**
     * Is sound is activated
     */
    private boolean soundEnabled = true, musicEnabled = true;

    /**
     * GPU vsync option by default disabled
     */
    private boolean enableVSync = false;

    /**
     * 1024x768 is the default resolution
     */
    private int hResolution = 1024, vResolution = 768;

    private int bitsPerPixel = 0;

    private int samples = 0;

    /**
     * Default in window mode
     */
    private boolean fullScreen = false;

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume) {
        this.soundVolume = soundVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }

    public boolean isEnableVSync() {
        return enableVSync;
    }

    public void setEnableVSync(boolean enableVSync) {
        this.enableVSync = enableVSync;
    }

    public int gethResolution() {
        return hResolution;
    }

    public void sethResolution(int hResolution) {
        this.hResolution = hResolution;
    }

    public int getvResolution() {
        return vResolution;
    }

    public void setvResolution(int vResolution) {
        this.vResolution = vResolution;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public int getBitsPerPixel() {
        return bitsPerPixel;
    }

    public void setBitsPerPixel(int bitsPerPixel) {
        this.bitsPerPixel = bitsPerPixel;
    }

    public int getSamples() {
        return samples;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

}