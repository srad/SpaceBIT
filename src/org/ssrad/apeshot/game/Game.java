package org.ssrad.apeshot.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.apeshot.enums.GameLevel;
import org.ssrad.apeshot.listeners.KeyBoardActionListener;
import org.ssrad.apeshot.listeners.KeyboardAnalogListener;
import org.ssrad.apeshot.listeners.MouseListener;
import org.ssrad.apeshot.nodes.Ape;
import org.ssrad.apeshot.nodes.BullsEye;
import org.ssrad.apeshot.nodes.FireExplosion;
import org.ssrad.apeshot.nodes.Heart;
import org.ssrad.apeshot.nodes.HudScreen;
import org.ssrad.apeshot.nodes.Laser;
import org.ssrad.apeshot.nodes.Planet;
import org.ssrad.apeshot.nodes.Ship;
import org.ssrad.apeshot.nodes.ShockWaveExplosion;
import org.ssrad.apeshot.nodes.Star;
import org.ssrad.apeshot.nodes.TitleScreen;
import org.ssrad.apeshot.nodes.TorusCoin;
import org.ssrad.apeshot.nodes.Ufo;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class Game extends SimpleApplication {
	
	public final static boolean DEBUG = false;

	public final float SCROLL_SPEED = 6f;
	
	private HudScreen hudScreen;	

	private Ship ship;
	
	private Random random = new Random();
	
	private BullsEye bullsEye;
	
	FilterPostProcessor fpp;
	
	TitleScreen titleScreen;
	// Enemies
	private ArrayList<Ape> apes = new ArrayList<Ape>();
	private ArrayList<Ufo> ufos = new ArrayList<Ufo>();
	
	
	private ArrayList<Laser> lasers = new ArrayList<Laser>();
	private ArrayList<TorusCoin> torusCoins = new ArrayList<TorusCoin>();
	private ArrayList<Heart> hearts = new ArrayList<Heart>();
	
	private ArrayList<ShockWaveExplosion> shockWaveExplosions = new ArrayList<ShockWaveExplosion>();
	private ArrayList<FireExplosion> fireExplosions = new ArrayList<FireExplosion>();
	
	private ArrayList<Star> stars = new ArrayList<Star>();
	private ArrayList<Planet> planets = new ArrayList<Planet>();

	private GameLevel level;
	
	Boolean isRunning = false;
	
	private PssmShadowRenderer pssmRenderer;
	
	PointLight light;
		
	@Override
	public void simpleInitApp() {	
		hudScreen = new HudScreen(this);

		ship = new Ship(this);
		rootNode.attachChild(ship);
		
		getCamera().setLocation(ship.clone().getLocalTranslation().add(0, 60f, -25f));
		getCamera().lookAt(ship.getLocalTranslation(), Vector3f.UNIT_Y);
        flyCam.setEnabled(false);
                
		fpp = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		fpp.addFilter(bloom);
		viewPort.addProcessor(fpp);
		
		pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
	    pssmRenderer.setDirection(new Vector3f(0,0,20f)); // light direction
	    viewPort.addProcessor(pssmRenderer);

	    rootNode.setShadowMode(ShadowMode.Off);

		initKeys();
		setUpLight();

		titleScreen = new TitleScreen(this);
	}

	private void setUpLight() {
		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(500f));
		rootNode.addLight(al);
		
		light = new PointLight();
		light.setRadius(100f);
		light.setColor(ColorRGBA.White);
		rootNode.addLight(light);
	}

	/**
     * Attaches a skybox to the root node.
     */
    public void addSkyBox() {
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

		inputManager.addMapping("quit", new KeyTrigger(KeyInput.KEY_Q));
		
		inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

		inputManager.addListener(new KeyBoardActionListener(this), new String[] { "pause", "shoot", "level_1", "level_2", "quit" });
		inputManager.addListener(new KeyboardAnalogListener(this), new String[] { "up", "down", "left", "right" });
		inputManager.addListener(new MouseListener(this), new String[] { "shoot" });
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		if (DEBUG) {
			System.err.println(ufos.size() + ", " + fireExplosions.size() + ", " + shockWaveExplosions.size() + ", " + torusCoins.size() + ", " + hearts.size());
		}
				
		if (titleScreen.isActive()) {
			titleScreen.update(tpf);
		}
	
		if (!isRunning) {
			return;
		}

		if (!ship.isActive()) {
			ship.destroy();
		}
		ship.update(tpf);
		light.setPosition(ship.getLocalTranslation().clone().add(0,0,5));

		hudScreen.update(tpf);
		
//		Vector2f cursor = inputManager.getCursorPosition().clone();
//		Vector3f currentPos = ship.getWorldTranslation().clone();
//		float v = currentPos.distance(new Vector3f(cursor.x + cam.getLocation().x, ship.getLocalTranslation().y, cursor.y + cam.getLocation().z));
//		
		//ship.lookAt(new Vector3f(cursor.x, ship.getLocalTranslation().y, cursor.y), Vector3f.UNIT_Y);
				
		// Update lasers
		for (Iterator<Laser> it = lasers.iterator(); it.hasNext();) {
			Laser laser = (Laser) it.next();
			if (!laser.isActive()) {
				it.remove();
				laser.destroy();
			} else {
				laser.update(tpf);
			}
		}

		// Update rings
		for (Iterator<TorusCoin> it = torusCoins.iterator(); it.hasNext();) {
			TorusCoin t = (TorusCoin) it.next();
			if (!t.isActive()) {
				it.remove();
				t.destroy();
				ship.incCoins();
			} else {
				t.update(tpf);
			}
		}
		
		// Update ape enemies
		for (Iterator<Ape> it = apes.iterator(); it.hasNext();) {
			Ape ape = (Ape) it.next();
			if (!ape.isActive()) {
				it.remove();
				ape.destroy();
			} else {
				ape.update(tpf);
			}
		}
		
		// Update ufo enemies
		for (Iterator<Ufo> it = ufos.iterator(); it.hasNext();) {
			Ufo ufo = (Ufo) it.next();
			if (!ufo.isActive()) {
				it.remove();
				ufo.destroy();
			} else {
				ufo.update(tpf);
			}
		}
		
		// Update hearts
		for (Iterator<Heart> it = hearts.iterator(); it.hasNext();) {
			Heart heart = (Heart) it.next();
			if (!heart.isActive()) {
				it.remove();
				heart.destroy();
			} else {
				heart.update(tpf);
			}
		}
		
		// Update stars
		for (Iterator<Star> it = stars.iterator(); it.hasNext();) {
			Star star = (Star) it.next();
			if (!star.isActive()) {
				it.remove();
				star.destroy();
			} else {
				star.update(tpf);
			}
		}
		
		// TODO:
		// Update planets
//		for (Iterator<Planet> it = planets.iterator(); it.hasNext();) {
//			Planet planet = (Planet) it.next();
//			if (!planet.isActive()) {
//				it.remove();
//				planet.destroy();
//			} else {
//				planet.update(tpf);
//			}
//		}

		// Update explosions
		for (Iterator<ShockWaveExplosion> it = shockWaveExplosions.iterator(); it.hasNext();) {
			ShockWaveExplosion explosion = (ShockWaveExplosion) it.next();
			if (!explosion.isActive()) {
				it.remove();
				explosion.destroy();
			} else {
				explosion.update(tpf);
			}
		}
		
		// Update explosions of type 2
		for (Iterator<FireExplosion> it = fireExplosions.iterator(); it.hasNext();) {
			FireExplosion explosion = (FireExplosion) it.next();
			if (!explosion.isActive()) {
				it.remove();
				explosion.destroy();
			} else {
				explosion.update(tpf);
			}
		}
		
		spawnRandomTorusCoins();
		spawnRandomHeart();
		spawnRandomStars();
		// TODO:
		//spawnRandomPlanet();
		
		if (level == GameLevel.LEVEL_ONE) {
			spawnRandomApes();
		} else if (level == GameLevel.LEVEL_TWO) {
			spawnRandomUfos();
		}
		
		// Move cam		
		cam.setLocation(cam.getLocation().add(0, 0, SCROLL_SPEED * tpf));		
	}

	private void spawnRandomTorusCoins() {
		if (random.nextInt(10) > 2 && (getTimer().getTimeInSeconds() % 1f <= 0.01f)) {
			TorusCoin t = new TorusCoin(this);
			Vector3f position = ship.getLocalTranslation().clone().add(0f, 0f, 50f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			t.setLocalTranslation(position);
			addTorusCoin(t);
		}
	}

	private void spawnRandomPlanet() {
		if (random.nextInt(10) > 7 && (getTimer().getTimeInSeconds() % 1f <= 0.01f)) {
			Planet planet = new Planet(this);
			Vector3f position = ship.getLocalTranslation().clone().add(0f, 0f, 50f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			planet.setLocalTranslation(position);
			addPlanet(planet);
		}
	}	
	
	private void spawnRandomStars() {
		if ((random.nextInt(10) > 6) && getTimer().getTimeInSeconds()  % 1f <= 0.1f) {
			Star star = new Star(this);
			Vector3f position = ship.getLocalTranslation().clone().add(0f, 0f, random.nextFloat() * 70f + 40f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 40f;
			star.setLocalTranslation(position);
			addStar(star);
		}
	}
	
	private void spawnRandomApes() {
		if ((random.nextInt(10) > 4) && getTimer().getTimeInSeconds() % 1f <= 0.01f) {

			Ape newApe = new Ape(this);
			
			Vector3f position = ship.getLocalTranslation().clone().add(0f, 0f, 50f);			
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;

			newApe.setLocalTranslation(position);			
			newApe.setCullHint(CullHint.Always);
			
			// Check for free space
			for (Iterator<Ape> it = apes.iterator(); it.hasNext();) {
				Ape currentUfo = (Ape) it.next();
				
				// First test if not colliding with another ape
				rootNode.attachChild(newApe);
				if (newApe.getWorldBound().intersects(currentUfo.getWorldBound())) {
					rootNode.detachChild(newApe);
					return;
				}
			}
			newApe.setCullHint(CullHint.Never);
			addApe(newApe);
		}
	}
	
	private void spawnRandomUfos() {
		if ((random.nextInt(10) > 4) && getTimer().getTimeInSeconds() % 1f <= 0.01f) {

			Ufo newUfo = new Ufo(this);
			
			Vector3f position = ship.getLocalTranslation().clone().add(0f, 0f, 50f);			
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;

			newUfo.setLocalTranslation(position);			
			newUfo.setCullHint(CullHint.Always);
			
			// Check for free space
			for (Iterator<Ufo> it = ufos.iterator(); it.hasNext();) {
				Ufo currentUfo = (Ufo) it.next();
				
				// First test if not colliding with another ufo
				rootNode.attachChild(newUfo);
				if (newUfo.getWorldBound().intersects(currentUfo.getWorldBound())) {
					rootNode.detachChild(newUfo);
					return;
				}
			}
			newUfo.setCullHint(CullHint.Never);
			addUfo(newUfo);
		}
	}
	
	private void spawnRandomHeart() {
		if (random.nextInt(10) > 1 && (getTimer().getTimeInSeconds() % 1f <= 0.001f)) {
			Heart heart = new Heart(this);
			Vector3f position = ship.getLocalTranslation().clone().add(0f, 0f, 50f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			heart.setLocalTranslation(position);
			addHeart(heart);
		}
	}

	public Ship getShip() {
		return ship;
	}

	public void addProjectiles(Laser projectile) {
		lasers.add(projectile);
		rootNode.attachChild(projectile);
	}
	
	public ArrayList<Laser> getLasers() {
		return lasers;
	}
	
	public void addTorusCoin(TorusCoin torusCoin) {
		torusCoins.add(torusCoin);
		rootNode.attachChild(torusCoin);
	}
	
	public void addApe(Ape ape) {
		apes.add(ape);
		rootNode.attachChild(ape);
	}
	
	public void addUfo(Ufo ufo) {
		ufos.add(ufo);
		rootNode.attachChild(ufo);
	}
	
	public void addHeart(Heart heart) {
		hearts.add(heart);
		rootNode.attachChild(heart);
	}
	
	public void addExplosion(ShockWaveExplosion explosion) {
		shockWaveExplosions.add(explosion);
		rootNode.attachChild(explosion);
	}
	
	public void addFireExplosion(FireExplosion explosion) {
		fireExplosions.add(explosion);
		rootNode.attachChild(explosion);
	}
	
	public void addStar(Star star) {
		stars.add(star);
		rootNode.attachChild(star);
	}
	
	public void addPlanet(Planet planet) {
		planets.add(planet);
		rootNode.attachChild(planet);
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

	public GameLevel getLevel() {
		return level;
	}

	public void setLevel(GameLevel level) {
		this.level = level;
	}

	public ArrayList<Ufo> getAllUfos() {
		return ufos;
	}
	
	public Vector3f getCursorLocation() {
		Vector2f mousePositionScreen = inputManager.getCursorPosition();
		Vector3f mousePosition3d = cam.getWorldCoordinates(mousePositionScreen, 0).clone();
		mousePosition3d.y = 0f;
		
		return ship.getLocalTranslation();
	}

}
