
package org.ssrad.spacebit.game;

import java.util.ArrayList;
import java.util.Collection;
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
import com.jme3.scene.Spatial.CullHint;

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
	
	// Enemies
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
		if (Game.DEBUG) {
			System.err.println(ufos.size() + ", " + fireExplosions.size() + ", " + shockWaveExplosions.size() + ", " + torusCoins.size() + ", " + hearts.size() + ", " + stars.size() + ", " + lasers.size());
		}
		
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
//		if (blackHoles.size() > 0) {
//			for (Iterator<BlackHole> it = blackHoles.iterator(); it.hasNext();) {
//				BlackHole blackHole = (BlackHole) it.next();
//				if (!blackHole.isActive()) {
//					it.remove();
//					blackHole.destroy();
//				} else {
//					blackHole.update(tpf);
//				}
//			}
//		}
		
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
			
			spawnRandomTorusCoins();
			spawnRandomHeart();
			spawnRandomStars();
			spawnRandomWarpCore();
			//spawnRandomBlackHoles();
			//spawnRandomAsteroid();
			spawnRandomPlanet();
			
			if (game.getLevel() == GameLevel.LEVEL_ONE) {
				spawnRandomApes();
			} else if (game.getLevel() == GameLevel.LEVEL_TWO) {
				spawnRandomUfos();
			}
		}
		
		stopWatch += tpf;
	}
	
	private void spawnRandomAsteroid() {
		if ((random.nextInt(20) > 18)) {

			Asteroid a = new Asteroid(game);
			
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);			
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;

			a.setLocalTranslation(position);			
			a.setCullHint(CullHint.Always);
			
			// Check for free space
			for (Iterator<Asteroid> it = asteroids.iterator(); it.hasNext();) {
				Asteroid a_i = (Asteroid) it.next();
				
				// First test if not colliding with another ape
				rootNode.attachChild(a);
				if (a.getWorldBound().intersects(a_i.getWorldBound())) {
					rootNode.detachChild(a);
					return;
				}
			}
			a.setCullHint(CullHint.Never);
			addAsteroid(a);
		}
	}

	private void spawnRandomTorusCoins() {
		if (random.nextInt(20) > 12) {
			TorusCoin t = new TorusCoin(game);;
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			t.setLocalTranslation(position);
			addTorusCoin(t);
		}
	}

	private void spawnRandomPlanet() {
		if (random.nextInt(20) > 18) {
			Planet planet = new Planet(game);;
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			planet.setLocalTranslation(position);
			addPlanet(planet);
		}
	}	
	
	private void spawnRandomWarpCore() {
		if (random.nextInt(20) > 18) {
			WarpCore core = new WarpCore(game);;
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			core.setLocalTranslation(position);
			addWarpCore(core);
		}
	}	
	
	private void spawnRandomStars() {
		Star star = new Star(game);;
		Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, random.nextFloat() * 70f + 40f);
		// Random x position + random sign
		// Minus: left side of the ship, Positive: right side
		position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 40f;
		star.setLocalTranslation(position);
		addStar(star);
	}
	
	private void spawnRandomApes() {
		if ((random.nextInt(20) > 12)) {

			Ape newApe = new Ape(game);;
			
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);			
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;

			newApe.setLocalTranslation(position);			
			newApe.setCullHint(CullHint.Always);
			
			// Check for free space
			for (Iterator<Ape> it = apes.iterator(); it.hasNext();) {
				Ape ape_i = (Ape) it.next();
				
				// First test if not colliding with another ape
				rootNode.attachChild(newApe);
				if (newApe.getWorldBound().intersects(ape_i.getWorldBound())) {
					rootNode.detachChild(newApe);
					return;
				}
			}
			newApe.setCullHint(CullHint.Never);
			addApe(newApe);
		}
	}
	
	private void spawnRandomBlackHoles() {
		if ((random.nextInt(20) > 17)) {
			
			BlackHole blackHole = new BlackHole(game);;
			
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);			
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			
			blackHole.setLocalTranslation(position);			
			blackHole.setCullHint(CullHint.Always);
			
			// Check for free space
			for (Iterator<BlackHole> it = blackHoles.iterator(); it.hasNext();) {
				BlackHole currentBlackHole = (BlackHole) it.next();
				
				// First test if not colliding with another ape
				rootNode.attachChild(blackHole);
				if (blackHole.getWorldBound().intersects(currentBlackHole.getWorldBound())) {
					rootNode.detachChild(blackHole);
					return;
				}
			}
			blackHole.setCullHint(CullHint.Never);
			addBlackHole(blackHole);
		}
	}
	
	private void spawnRandomUfos() {
		if (random.nextInt(20) > 15) {

			Ufo newUfo = new Ufo(game);;
			
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);			
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
		if (random.nextInt(20) > 16) {
			Heart heart = new Heart(game);;
			Vector3f position = game.getShip().getLocalTranslation().clone().add(0f, 0f, 50f);
			// Random x position + random sign
			// Minus: left side of the ship, Positive: right side
			position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 32f;
			heart.setLocalTranslation(position);
			addHeart(heart);
		}
	}

	public void addLaser(Laser projectile) {
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
	
	public void addShockWaveExplosion(ShockWaveExplosion explosion) {
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
	
	public void addWarpCore(WarpCore warpCore) {
		warpCores.add(warpCore);
		rootNode.attachChild(warpCore);
	}
	
	public void addPlanet(Planet planet) {
		planets.add(planet);
		rootNode.attachChild(planet);
	}	

	public ArrayList<Ufo> getUfos() {
		return ufos;
	}
	
	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}
	
	public void addBlackHole(BlackHole blackHole) {
		blackHoles.add(blackHole);
		rootNode.attachChild(blackHole);
	}
	
	public void addAsteroid(Asteroid asteroid) {
		asteroids.add(asteroid);
		rootNode.attachChild(asteroid);
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

	public void destroyObstacles() {
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

	public ArrayList<Planet> getPlanets() {
		return planets;
	}

}
