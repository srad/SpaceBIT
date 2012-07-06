package org.ssrad.spacebit.game;

import java.util.logging.Level;

import org.ssrad.spacebit.audio.GameMusic;
import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.helpers.GameLogger;
import org.ssrad.spacebit.nodes.Ship;
import org.ssrad.spacebit.nodes.screens.GameOverScreen;
import org.ssrad.spacebit.nodes.screens.HudScreen;
import org.ssrad.spacebit.nodes.screens.LoadScreen;
import org.ssrad.spacebit.nodes.screens.TitleScreen;
import org.ssrad.spacebit.nodes.screens.WinScreen;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class Game extends SimpleApplication {
	
	public static int GAME_TIME_SECONDS = 200;
	public static int MUST_SCORE = 200;
	
	public final static boolean DEBUG = false;
	public final float SCROLL_SPEED = 6f;	

	private Ship ship;
	
	FilterPostProcessor fpp;
	
	private HudScreen hudScreen;	
	private TitleScreen titleScreen;
	private GameOverScreen gameOverScreen;
	private WinScreen winScreen;
	private LoadScreen loadScreen;
	
	private GameLevel level;
	
	/** Game paused/unpaused */
	boolean running = false;
	
	/** Game initilized */
	private boolean launched = false;
	
	// SHADOW
	private PssmShadowRenderer shadowRenderer;
	private boolean shadow = true;

	// BLOOM
	private BloomFilter bloomFilter;
	private boolean bloom = true;

	private LightScatteringFilter lsFilter;
	private boolean useLSFilter = true;

	private GameMusic gameMusic;

	Updateables updateables;

	private float timer;
	
	private boolean win = false;

	@Override
	public void simpleInitApp() {	
		running = false;
		titleScreen = new TitleScreen(this);
		loadScreen = new LoadScreen(this);
		winScreen = new WinScreen(this);
		loadScreen.hide();
		titleScreen.show();
	}
	
	public void init() {
		timer = 0f;
		running = true;
		launched = true;
		
		// SHADOW
		rootNode.setShadowMode(ShadowMode.Off);
		
		// Screens
		hudScreen = new HudScreen(this);
		gameOverScreen = new GameOverScreen(this);
		
		// SHIP
		ship = new Ship(this);
		rootNode.attachChild(ship);
		GameLogger.Log(Level.INFO, "Added ship");

	    // CAMERA		
		cam.setLocation(ship.clone().getLocalTranslation().add(0, 60f, -25f));
		cam.lookAt(ship.getLocalTranslation(), Vector3f.UNIT_Y);
        flyCam.setEnabled(false);
        GameLogger.Log(Level.INFO, "Adjusted camera");

        addEffects();
        
	    // AUDIO
	    gameMusic = new GameMusic(this);
	    gameMusic.setVolume(0.1f);
	    GameLogger.Log(Level.INFO, "Added and playing music");

		setUpLight();
		updateables = new Updateables(this);
	}
	
	private void addEffects() {
        // FILTERS
		fpp = new FilterPostProcessor(assetManager);
				
		bloomFilter = new BloomFilter(BloomFilter.GlowMode.Objects);
		fpp.addFilter(bloomFilter);
		
		lsFilter = new LightScatteringFilter();
		lsFilter.setEnabled(false);
		fpp.addFilter(lsFilter);
		
		viewPort.addProcessor(fpp);
		GameLogger.Log(Level.INFO, "Added bloom filter");

		shadowRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
	    shadowRenderer.setDirection(new Vector3f(0,0,20f)); // light direction
	    
	    viewPort.addProcessor(shadowRenderer);
	    
	    GameLogger.Log(Level.INFO, "Added sharow renderer");

        viewPort.addProcessor(fpp);
	    
	    rootNode.setShadowMode(ShadowMode.Off);

	}
	
	private void setUpLight() {
		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(500f));
		rootNode.addLight(al);
	}

	/**
     * Attaches a skybox to the root node.
     */
    public void addSkyBox() {
        Texture west, east, north, south, up, down;   
        
        if (level == GameLevel.LEVEL_ONE) {
	        west = assetManager.loadTexture("skybox/skybox_left2.png");
	        east = assetManager.loadTexture("skybox/skybox_right1.png");
	        north = assetManager.loadTexture("skybox/skybox_front5.png");
	        south = assetManager.loadTexture("skybox/skybox_back6.png");
	        up = assetManager.loadTexture("skybox/skybox_top3.png");
	        down = assetManager.loadTexture("skybox/skybox_bottom4.png");
        } else {
	        west = assetManager.loadTexture("skybox/stars_lila/skybox_lila_left2.png");
	        east = assetManager.loadTexture("skybox/stars_lila/skybox_lila_right1.png");
	        north = assetManager.loadTexture("skybox/stars_lila/skybox_lila_front5.png");
	        south = assetManager.loadTexture("skybox/stars_lila/skybox_lila_back6.png");
	        up = assetManager.loadTexture("skybox/stars_lila/skybox_lila_top3.png");
	        down = assetManager.loadTexture("skybox/stars_lila/skybox_lila_bottom4.png");        	
        }

        rootNode.attachChild(SkyFactory.createSky(assetManager, west, east, north, south, up, down));
    }

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		
		if (loadScreen.isActive()) {
			loadScreen.update(tpf);
		}
		else if (winScreen.isActive()) {
			winScreen.update(tpf);
		}
		else if (isRunning()) {
			// SHIP BEGIN
			if (ship.isActive()) {
				ship.update(tpf);
				hudScreen.update(tpf);
				
				// FAIL
				if ((int)getTimer().getTimeInSeconds() > GAME_TIME_SECONDS && ship.getScore() < MUST_SCORE) {
					ship.destroy();		
				}
				// WIN
				else if ((ship.getScore() >= MUST_SCORE) && ((int)getTimer().getTimeInSeconds() <= GAME_TIME_SECONDS)) {
					// LEVEL 2
					if (level == GameLevel.LEVEL_ONE) {
						ship.setScore(0);
						getTimer().reset();
						updateables.destroyObstacles();
						level = GameLevel.LEVEL_TWO;
						loadScreen.show();
					}
					// GAME WON
					else if (level == GameLevel.LEVEL_TWO) {
						running = false;
						win = true;
						winScreen.show();
					}
				}
			}
			// Ship destroyed...
			else {
				if (timer == 0f) {
					timer += tpf;
					toggleLSEffect();
				} else if (timer < 2f) {
					timer += tpf;
				}
				else if (timer > 2f) {
					if (ship.isDead()) {
						gameOverScreen.show();
						running = false;
					} else {
						updateables.destroyObstacles();
						toggleLSEffect();
						ship.reInit();
						timer = 0f;
					}
				}
			}
			// SHIP END	
	
			// Update all entities
			updateables.update(tpf);
	
			// Move cam		
			cam.setLocation(cam.getLocation().add(0, 0, SCROLL_SPEED * tpf));	
		}
	}

	public Ship getShip() {
		return ship;
	}
	
	public AppSettings getSettings() {
		return settings;
	}
	
	public void run() {
		running = true;
		addSkyBox();
		getGameMusic().play();
		hudScreen.show();
	}

	public void pause() {
		running = false;
		getGameMusic().stop();
		titleScreen.show();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public TitleScreen getTitleScreen() {
		return titleScreen;
	}

	public BitmapFont getGuiFont() {
		return guiFont;
	}
	
	public Updateables getUpdateables() {
		return updateables;
	}

	public GameLevel getLevel() {
		return level;
	}

	public void setLevel(GameLevel level) {
		this.level = level;
	}
	
	public Vector3f getCursorLocation() {
		Vector2f mousePositionScreen = inputManager.getCursorPosition();
		Vector3f mousePosition3d = cam.getWorldCoordinates(mousePositionScreen, 0).clone();
		mousePosition3d.y = 0f;
		
		return ship.getLocalTranslation();
	}
	
	public GameMusic getGameMusic() {
		return gameMusic;
	}

	public void toggleBloom() {
		if (bloom) {
			fpp.removeFilter(bloomFilter);
		} else {
			fpp.addFilter(bloomFilter);
		}
		bloom = !bloom;
	}
	
	public void toggleShadow() {
		if (shadow) {
			viewPort.removeProcessor(shadowRenderer);
		} else {
			viewPort.addProcessor(shadowRenderer);
		}
		shadow = !shadow;
	}
	
	public void toggleLSFilter() {
		useLSFilter = !useLSFilter;
	}
	
	public void toggleLSEffect() {
		if (useLSFilter && !lsFilter.isEnabled()) {
			lsFilter.setLightPosition(cam.getLocation().add(0, -100, 20));
			lsFilter.setBlurWidth(0.2f);
			lsFilter.setEnabled(true);			
		} else if (useLSFilter && lsFilter.isEnabled() || !useLSFilter) {
			lsFilter.setEnabled(false);
		}
	}

	public boolean isLaunched() {
		return launched;
	}

	public void setLaunched(boolean launched) {
		this.launched = launched;
	}
	
	public HudScreen getHudScreen() {
		return hudScreen;
	}

	public void load() {
		loadScreen.show();
	}
	
}
