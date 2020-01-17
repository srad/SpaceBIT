package com.github.srad.spacebit.game;

import com.github.srad.spacebit.audio.GameMusic;
import com.github.srad.spacebit.enums.GameLevel;
import com.github.srad.spacebit.helpers.LogHelper;
import com.github.srad.spacebit.helpers.SettingsHelper;
import com.github.srad.spacebit.nodes.entities.Ship;
import com.github.srad.spacebit.nodes.screens.*;
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
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class Game extends SimpleApplication {

  public static int GAME_TIME_SECONDS = 300;
  public static int MUST_SCORE = 2000;

  public final static boolean DEBUG = false;
  public final float SCROLL_SPEED = 6f;

  private Ship ship = null;

  FilterPostProcessor fpp;

  /**
   * Game screens.
   */
  private AbstractScreen hudScreen, titleScreen, gameOverScreen, winScreen, loadScreen, helpScreen, copyrightScreen;

  /**
   * Currently visible screen.
   */
  AbstractScreen screen;

  private GameLevel level;

  /**
   * Game paused/unpaused
   */
  boolean running = false;

  /**
   * Game initilized
   */
  private boolean launched = false;

  // SHADOW
  private DirectionalLightShadowRenderer shadowRenderer = null;
  private boolean shadow = false;

  // BLOOM
  private BloomFilter bloomFilter;
  private boolean bloom = true;

  // For ship explosions
  private LightScatteringFilter lsFilter;
  private boolean useLSFilter = true;

  private GameMusic gameMusic = null;

  Entities entities;

  private float timer;

  private boolean win = false;

  private SettingsHelper settingsHelper;
  private GameSettings gameSettings;

  public Game() {
  }

  public Game(SettingsHelper settingsHelper) {
    this.setSettingsHelper(settingsHelper);
  }

  @Override
  public void simpleInitApp() {
    running = false;

    titleScreen = new TitleScreen(this);
    loadScreen = new LoadScreen(this);
    winScreen = new WinScreen(this);
    helpScreen = new HelpScreen(this);
    copyrightScreen = new CopyrightScreen(this);

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

    // CAMERA
    cam.setLocation(ship.clone().getLocalTranslation().add(0, 60f, -25f));
    cam.lookAt(ship.getLocalTranslation(), Vector3f.UNIT_Y);
    flyCam.setEnabled(false);

    addEffects();

    initLight();
    entities = new Entities(this);
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

    if (shadow) {
      initShadow();
      viewPort.addProcessor(shadowRenderer);
    }

    rootNode.setShadowMode(ShadowMode.Off);
  }

  private void initShadow() {
    if (shadowRenderer == null) {
      shadowRenderer = new DirectionalLightShadowRenderer(assetManager, 1024, 3);
      // TODO shadowRenderer.set(new Vector3f(0, 0, 20f)); // light direction
    }
  }

  /**
   * Scene wide light.
   */
  public void initLight() {
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
      west = assetManager.loadTexture("textures/skybox/skybox_left2.png");
      east = assetManager.loadTexture("textures/skybox/skybox_right1.png");
      north = assetManager.loadTexture("textures/skybox/skybox_front5.png");
      south = assetManager.loadTexture("textures/skybox/skybox_back6.png");
      up = assetManager.loadTexture("textures/skybox/skybox_top3.png");
      down = assetManager.loadTexture("textures/skybox/skybox_bottom4.png");
    } else {
      west = assetManager.loadTexture("textures/skybox/stars_lila/skybox_lila_left2.png");
      east = assetManager.loadTexture("textures/skybox/stars_lila/skybox_lila_right1.png");
      north = assetManager.loadTexture("textures/skybox/stars_lila/skybox_lila_front5.png");
      south = assetManager.loadTexture("textures/skybox/stars_lila/skybox_lila_back6.png");
      up = assetManager.loadTexture("textures/skybox/stars_lila/skybox_lila_top3.png");
      down = assetManager.loadTexture("textures/skybox/stars_lila/skybox_lila_bottom4.png");
    }
    rootNode.attachChild(SkyFactory.createSky(assetManager, west, east, north, south, up, down));
  }

  public void setScreen(AbstractScreen screen) {
    this.screen = screen;
  }

  @Override
  public void simpleUpdate(float tpf) {
    super.simpleUpdate(tpf);

    screen.update(tpf);

    if (isRunning()) {
      // SHIP BEGIN
      if (ship.isActive()) {
        ship.update(tpf);

        // FAIL
        if ((int) getTimer().getTimeInSeconds() > GAME_TIME_SECONDS && ship.getScore() < MUST_SCORE) {
          ship.destroy();
          getTimer().reset();
        }
        // WIN
        else if ((ship.getScore() >= MUST_SCORE) && ((int) getTimer().getTimeInSeconds() <= GAME_TIME_SECONDS)) {
          // LEVEL 2
          if (level == GameLevel.LEVEL_ONE) {
            ship.setScore(0);
            getTimer().reset();
            entities.destroyObstacles();
            getGameMusic().stop();
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
        } else if (timer > 2f) {
          if (ship.isDead()) {
            gameOverScreen.show();
            running = false;
          } else {
            entities.destroyObstacles();
            toggleLSEffect();
            ship.reInit();
            timer = 0f;
          }
        }
      }
      // SHIP END

      // Update all entities
      entities.update(tpf);

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
    gameMusic.play();
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

  public AbstractScreen getTitleScreen() {
    return titleScreen;
  }

  public BitmapFont getGuiFont() {
    return guiFont;
  }

  public Entities getEntities() {
    return entities;
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

  public void setGameMusic(GameMusic gameMusic) {
    this.gameMusic = gameMusic;
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
    // Add to processor at all if not yet there
    initShadow();

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

  public AbstractScreen getHudScreen() {
    return hudScreen;
  }

  public AbstractScreen getGameOverScreen() {
    return gameOverScreen;
  }

  public AbstractScreen getHelpScreen() {
    return helpScreen;
  }

  public AbstractScreen getLoadScreen() {
    return loadScreen;
  }

  public AbstractScreen getCopyrightScreen() {
    return copyrightScreen;
  }

  public SettingsHelper getSettingsHelper() {
    return settingsHelper;
  }

  public void setSettingsHelper(SettingsHelper settingsHelper) {
    this.settingsHelper = settingsHelper;
    try {
      this.gameSettings = settingsHelper.getGameSettings();
    } catch (Exception e) {
      LogHelper.getLogger().error("No game setting file.");
    }
  }

  @Override
  public void setSettings(AppSettings settings) {
    super.setSettings(settings);

    gameSettings.setBitsPerPixel(settings.getBitsPerPixel());
    gameSettings.setEnableVSync(settings.isVSync());
    gameSettings.setFullScreen(settings.isFullscreen());
    gameSettings.setvResolution(settings.getHeight());
    gameSettings.sethResolution(settings.getWidth());
    gameSettings.setSamples(settings.getSamples());
  }

  public void saveSettings() {
    if (settingsHelper != null) {
      settingsHelper.setGameSettings(gameSettings);
    }
  }

}
