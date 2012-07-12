
package org.ssrad.spacebit.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.nodes.AbstractNode;
import org.ssrad.spacebit.nodes.Ape;
import org.ssrad.spacebit.nodes.Asteroid;
import org.ssrad.spacebit.nodes.BlackHole;
import org.ssrad.spacebit.nodes.FireExplosion;
import org.ssrad.spacebit.nodes.Heart;
import org.ssrad.spacebit.nodes.Laser;
import org.ssrad.spacebit.nodes.Planet;
import org.ssrad.spacebit.nodes.ShockWaveExplosion;
import org.ssrad.spacebit.nodes.Star;
import org.ssrad.spacebit.nodes.TorusCoin;
import org.ssrad.spacebit.nodes.Ufo;
import org.ssrad.spacebit.nodes.WarpCore;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * This class contains most entities that needs to be updated
 * within the main game loop.
 * 
 * @author Saman Sedighi Rad
 *
 */
public class Updateables {

	Game game;
	Node rootNode;
	Random random;
	float stopWatch = 0f;
	private final float SPAWN_ZDISTANCE_FROM_CAM = 60f;

	private ArrayList<Ape> apes = new ArrayList<Ape>();
	private ArrayList<Ufo> ufos = new ArrayList<Ufo>();
	
	private ArrayList<Laser> lasers = new ArrayList<Laser>();
	private ArrayList<TorusCoin> torusCoins = new ArrayList<TorusCoin>();
	private ArrayList<Heart> hearts = new ArrayList<Heart>();
	
	private ArrayList<BlackHole> blackHoles = new ArrayList<BlackHole>();
	private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	
	private ArrayList<ShockWaveExplosion> shockWaveExplosions = new ArrayList<ShockWaveExplosion>();
	private ArrayList<FireExplosion> fireExplosions = new ArrayList<FireExplosion>();
	
	private ArrayList<Star> stars = new ArrayList<Star>();
	private ArrayList<Planet> planets = new ArrayList<Planet>();
	
	private ArrayList<WarpCore> warpCores = new ArrayList<WarpCore>();
	
	
	public Updateables(Game game) {
		this.game = game;
		this.rootNode = game.getRootNode();
		this.random = new Random();
	}
	
	public void update(float tpf) {
		
		System.err.printf("%s:%s:%s:%s:%s:%s:%s:%s:%s:%s:%s\n", apes.size(), ufos.size(), planets.size(), shockWaveExplosions.size(), fireExplosions.size(), warpCores.size(), lasers.size(), stars.size(), blackHoles.size(), torusCoins.size(), hearts.size());
	
		// Update lasers
		if (lasers.size() > 0) {
			for (Iterator<Laser> it = lasers.iterator(); it.hasNext();) {
				Laser laser = (Laser) it.next();
				if (!laser.isActive()) {
					it.remove();
					laser.destroy();
				} else {
					laser.update(tpf);
				}
			}
		}

		// Update rings
		if (torusCoins.size() > 0) {
			for (Iterator<TorusCoin> it = torusCoins.iterator(); it.hasNext();) {
				TorusCoin t = (TorusCoin) it.next();
				if (!t.isActive()) {
					it.remove();
					t.destroy();
				} else {
					t.update(tpf);
				}
			}
		}
		
		// Update ape enemies
		if (apes.size() > 0) {
			for (Iterator<Ape> it = apes.iterator(); it.hasNext();) {
				Ape ape = (Ape) it.next();
				if (!ape.isActive()) {
					it.remove();
					ape.destroy();
				} else {
					ape.update(tpf);
				}
			}
		}
		
		// Update ufo enemies
		if (ufos.size() > 0) {
			for (Iterator<Ufo> it = ufos.iterator(); it.hasNext();) {
				Ufo ufo = (Ufo) it.next();
				if (!ufo.isActive()) {
					it.remove();
					ufo.destroy();
				} else {
					ufo.update(tpf);
				}
			}
		}
		
		// Update hearts
		if (hearts.size() > 0) {
			for (Iterator<Heart> it = hearts.iterator(); it.hasNext();) {
				Heart heart = (Heart) it.next();
				if (!heart.isActive()) {
					it.remove();
					heart.destroy();
				} else {
					heart.update(tpf);
				}
			}
		}
		
		// Update stars
		if (stars.size() > 0) {
			for (Iterator<Star> it = stars.iterator(); it.hasNext();) {
				Star star = (Star) it.next();
				if (!star.isActive()) {
					it.remove();
					star.destroy();
				} else {
					star.update(tpf);
				}
			}
		}

		// Update warp cores
		if (warpCores.size() > 0) {
			for (Iterator<WarpCore> it = warpCores.iterator(); it.hasNext();) {
				WarpCore wc = (WarpCore) it.next();
				if (!wc.isActive()) {
					it.remove();
					wc.destroy();
				} else {
					wc.update(tpf);
				}
			}
		}
		
		// Update planets
		if (planets.size() > 0) {
		for (Iterator<Planet> it = planets.iterator(); it.hasNext();) {
			Planet planet = (Planet) it.next();
			if (!planet.isActive()) {
				it.remove();
				planet.destroy();
			} else {
				planet.update(tpf);
			}
		}
		}

		// Update explosions
		if (shockWaveExplosions.size() > 0) {
			for (Iterator<ShockWaveExplosion> it = shockWaveExplosions.iterator(); it.hasNext();) {
				ShockWaveExplosion explosion = (ShockWaveExplosion) it.next();
				if (!explosion.isActive()) {
					it.remove();
					explosion.destroy();
				} else {
					explosion.update(tpf);
				}
			}
		}
		
		// Update explosions of type 2
		if (fireExplosions.size() > 0) {
			for (Iterator<FireExplosion> it = fireExplosions.iterator(); it.hasNext();) {
				FireExplosion explosion = (FireExplosion) it.next();
				if (!explosion.isActive()) {
					it.remove();
					explosion.destroy();
				} else {
					explosion.update(tpf);
				}
			}
		}
		
		// Update black holes
		if (blackHoles.size() > 0) {
			for (Iterator<BlackHole> it = blackHoles.iterator(); it.hasNext();) {
				BlackHole blackHole = (BlackHole) it.next();
				if (!blackHole.isActive()) {
					it.remove();
					blackHole.destroy();
				} else {
					blackHole.update(tpf);
				}
			}
		}
		
		// Update asteroids
//		if (asteroids.size() > 0) {
//			for (Iterator<Asteroid> it = asteroids.iterator(); it.hasNext();) {
//				Asteroid asteroid = (Asteroid) it.next();
//				if (!asteroid.isActive()) {
//					it.remove();
//					asteroid.destroy();
//				} else {
//					asteroid.update(tpf);
//				}
//			}
//		}
		
		if (stopWatch > 0.3f) {
			stopWatch = 0f;
			
			new TorusCoin(game).trySpawn();
			new Heart(game).trySpawn();
			spawnRandomStars();
			new WarpCore(game).trySpawn();
//			new BlackHole(game).trySpawn();
//			new Planet(game).trySpawn();
			
			if (game.getLevel() == GameLevel.LEVEL_ONE) {
				new Ape(game).trySpawn();
			} else if (game.getLevel() == GameLevel.LEVEL_TWO) {
				new Ufo(game).trySpawn();
			}
		}		
		stopWatch += tpf;
	}
	
	// TODO: too many vertices, rework
//	private void spawnRandomAsteroid() {
//		if ((random.nextInt(20) > 18)) {
//
//			Asteroid a = new Asteroid(game);
//			
//			Vector3f position = getSpawnCoordinates(a);		
//
//			a.setLocalTranslation(position);			
//			a.setCullHint(CullHint.Always);
//			
//			// Check for free space
//			for (Iterator<Asteroid> it = asteroids.iterator(); it.hasNext();) {
//				Asteroid a_i = (Asteroid) it.next();
//				
//				// First test if not colliding with another ape
//				rootNode.attachChild(a);
//				if (a.getWorldBound().intersects(a_i.getWorldBound())) {
//					rootNode.detachChild(a);
//					return;
//				}
//			}
//			a.setCullHint(CullHint.Never);
//			addAsteroid(a);
//		}
//	}

	private void spawnRandomStars() {
		Star star = new Star(game);
		
		Vector3f position = getSpawnCoordinates(star);
		star.setLocalTranslation(position);
		add(star);
	}
	
	public Vector3f getSpawnCoordinates(AbstractNode n) {
		Vector3f position = game.getCamera().getLocation().add(0f, 0f, SPAWN_ZDISTANCE_FROM_CAM);
		position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 40f;
		position.y = 0;
		
		return position;
	}
	
	@SuppressWarnings("serial")
	public ArrayList<AbstractNode> getAllObstracles() {
		return new ArrayList<AbstractNode>() {{
			addAll(apes);
			addAll(ufos);
			addAll(blackHoles);
			addAll(planets);
		}};
	}

	public void destroyObstacles() {
		if (blackHoles.size() > 0) {
			for (Iterator<BlackHole> it = blackHoles.iterator(); it.hasNext();) {
				BlackHole b = (BlackHole) it.next();
				it.remove();
				b.destroy();
			}
		}
		
		if (ufos.size() > 0) {
			for (Iterator<Ufo> it = ufos.iterator(); it.hasNext();) {
				Ufo ufo = (Ufo) it.next();
				it.remove();
				ufo.destroy();
			}
		}

		if (apes.size() > 0) {
			for (Iterator<Ape> it = apes.iterator(); it.hasNext();) {
				Ape ape = (Ape) it.next();
				it.remove();
				ape.destroy();
			}
		}
		
		if (planets.size() > 0) {
			for (Iterator<Planet> it = planets.iterator(); it.hasNext();) {
				Planet p = (Planet) it.next();
				it.remove();
				p.destroy();
			}
		}
	}
	
	/**
	 * They right way to do this would be polymorpism,
	 * but this is more compact.
	 */
	public void add(AbstractNode abstractNode) {
		if (abstractNode instanceof Ape) {
			apes.add((Ape) abstractNode);
		} else if (abstractNode instanceof BlackHole) {
			blackHoles.add((BlackHole) abstractNode);
		} else if (abstractNode instanceof Ufo) {
			ufos.add((Ufo) abstractNode);
		} else if (abstractNode instanceof Planet) {
			planets.add((Planet) abstractNode);
		} else if (abstractNode instanceof TorusCoin) {
			torusCoins.add((TorusCoin) abstractNode);
		} else if (abstractNode instanceof Heart) {
			hearts.add((Heart) abstractNode);
		} else if (abstractNode instanceof WarpCore) {
			warpCores.add((WarpCore) abstractNode);
		} else if (abstractNode instanceof ShockWaveExplosion) {
			shockWaveExplosions.add((ShockWaveExplosion) abstractNode);
		} else if (abstractNode instanceof FireExplosion) {
			fireExplosions.add((FireExplosion) abstractNode);
		} else if (abstractNode instanceof Laser) {
			lasers.add((Laser) abstractNode);
		} else if (abstractNode instanceof Star) {
			stars.add((Star) abstractNode);
		} else {
			return;
		}
		rootNode.attachChild(abstractNode);
	}	
	
	public ArrayList<Laser> getLasers() {
		return lasers;
	}

	public ArrayList<Ufo> getUfos() {
		return ufos;
	}
	
	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}
	
	public ArrayList<Ape> getApes() {
		return apes;
	}

	public ArrayList<Star> getStars() {
		return stars;
	}
	
	public ArrayList<BlackHole> getBlackHoles() {
		return blackHoles;
	}
	
	public ArrayList<Planet> getPlanets() {
		return planets;
	}

}
