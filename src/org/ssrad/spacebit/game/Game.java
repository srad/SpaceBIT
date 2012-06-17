package org.ssrad.spacebit.game;

import java.util.logging.Level;

import org.ssrad.spacebit.audio.GameMusic;
import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.helpers.GameLogger;
import org.ssrad.spacebit.listeners.KeyBoardActionListener;
import org.ssrad.spacebit.listeners.KeyboardAnalogListener;
import org.ssrad.spacebit.listeners.MouseListener;
import org.ssrad.spacebit.nodes.HudScreen;
import org.ssrad.spacebit.nodes.Ship;
import org.ssrad.spacebit.nodes.TitleScreen;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class Game extends SimpleApplication {
	
	public final static boolean DEBUG = false;

	public final float SCROLL_SPEED = 6f;
	
	private HudScreen hudScreen;	

	private Ship ship;
	
	FilterPostProcessor fpp;
	
	TitleScreen titleScreen;
	
	private GameLevel level;
	
	Boolean isRunning = false;
	
	private PssmShadowRenderer pssmRenderer;
	

	private GameMusic gameMusic;
	
	Updateables updateables;

	@Override
	public void simpleInitApp() {		
		hudScreen = new HudScreen(this);

		ship = new Ship(this);
		rootNode.attachChild(ship);
		GameLogger.Log(Level.INFO, "Added ship");
		
		getCamera().setLocation(ship.clone().getLocalTranslation().add(0, 60f, -25f));
		getCamera().lookAt(ship.getLocalTranslation(), Vector3f.UNIT_Y);
        flyCam.setEnabled(false);
        GameLogger.Log(Level.INFO, "Adjusted camera");
                
		fpp = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		fpp.addFilter(bloom);
		viewPort.addProcessor(fpp);
		GameLogger.Log(Level.INFO, "Added bloom filter");
		
		pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
	    pssmRenderer.setDirection(new Vector3f(0,0,20f)); // light direction
	    viewPort.addProcessor(pssmRenderer);
	    GameLogger.Log(Level.INFO, "Added sharow renderer");

	    rootNode.setShadowMode(ShadowMode.Off);
	    
	    // AUDIO
	    gameMusic = new GameMusic(this);
	    gameMusic.setVolume(0.4f);
	    gameMusic.play();
	    GameLogger.Log(Level.INFO, "Added and playing music");

		initKeys();
		setUpLight();

		titleScreen = new TitleScreen(this);
		updateables = new Updateables(this);
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
    	System.err.println("add sky");
        Texture west, east, north, south, up, down;   
        
        if (level == GameLevel.LEVEL_ONE) {
	        west = assetManager.loadTexture("skybox_left2.png");
	        east = assetManager.loadTexture("skybox_right1.png");
	        north = assetManager.loadTexture("skybox_front5.png");
	        south = assetManager.loadTexture("skybox_back6.png");
	        up = assetManager.loadTexture("skybox_top3.png");
	        down = assetManager.loadTexture("skybox_bottom4.png");
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

	private void initKeys() {
		// Remove ALL bindings
		inputManager.clearMappings();
		
		// You can map one or several inputs to one named action
		inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));
				
		inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));

		inputManager.addMapping("level_1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("level_2", new KeyTrigger(KeyInput.KEY_2));

		inputManager.addMapping("quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
		
		inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

		inputManager.addListener(new KeyBoardActionListener(this), new String[] { "pause", "shoot", "level_1", "level_2", "quit" });
		inputManager.addListener(new KeyboardAnalogListener(this), new String[] { "up", "down", "left", "right" });
		inputManager.addListener(new MouseListener(this), new String[] { "shoot" });
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
				
//		if (titleScreen.isActive()) {
//			titleScreen.update(tpf);
//		}
	
		if (!isRunning) {
			return;
		}

		// SHIP
		// Only thing we update here
		if (!ship.isActive()) {
			ship.destroy();
			if (ship.isDead()) {
				// TODO: goto game over screen
			} else {
				ship = new Ship(this);
			}
		}
		ship.update(tpf);
		// SHIP
		
		hudScreen.update(tpf);

		// Update all entities
		updateables.update(tpf);
		
		// Move cam		
		cam.setLocation(cam.getLocation().add(0, 0, SCROLL_SPEED * tpf));		
	}

	public Ship getShip() {
		return ship;
	}
	
	public AppSettings getSettings() {
		return settings;
	}
	
	public void run() {
		titleScreen.hide();
		isRunning = true;
		addSkyBox();
	}

	public void pause() {
		isRunning = false;
		titleScreen.show();
	}
	
	public boolean isRunning() {
		return isRunning;
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

}
