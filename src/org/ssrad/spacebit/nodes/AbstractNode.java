package org.ssrad.spacebit.nodes;

import java.util.Iterator;
import java.util.Random;

import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IAudible;
import org.ssrad.spacebit.interfaces.ICoinGiver;
import org.ssrad.spacebit.interfaces.ICoinTaker;
import org.ssrad.spacebit.interfaces.ICollidable;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;
import org.ssrad.spacebit.interfaces.IScoreTaker;
import org.ssrad.spacebit.interfaces.ISpawnable;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 * Super class for all entities.
 * 
 * I know that jmonkey wiki advises not to use an own node super class
 * but I don't trust their implementation.
 * 
 * @author Saman Sedighi Rad
 *
 */
public abstract class AbstractNode extends Node implements ICollidable {

	protected Game game;
	protected Node rootNode;
	protected AssetManager assetManager;
	protected boolean active = true;
	protected float scrollSpeed = 0f;

	protected Spatial spatial = null;
	protected Material material = null;
	protected PointLight light = null;

	private static float updateTimer = 0f;

	private final float SPAWN_ZDISTANCE_FROM_CAM = 60f;

	public AbstractNode(Game game) {
		this.game = game;
		this.rootNode = game.getRootNode();
		this.assetManager = game.getAssetManager();
		
		this.scrollSpeed = game.SCROLL_SPEED;
		setShadowMode(com.jme3.renderer.queue.RenderQueue.ShadowMode.Off);
		
		init();
	}
	
	protected abstract void init();

	public void update(float tpf) {
		updateTimer += tpf;
		
		// Limit required updates
		if (updateTimer > 0.1f) {
			updateTimer = 0f;
			// Out of sight, remove
			if (game.getCamera().getLocation().z > (getLocalTranslation().z + 1f)) {
				active = false;
			} else {
				checkCollisions();
			}
		}
	}
	
	public void destroy() {
		active = false;
		
		if (light != null) {
			rootNode.removeLight(light);
		}
		rootNode.detachChild(this);
	}

	protected void checkCollisions() {
		if (this instanceof ICollidable) {
			ICollidable collision = (ICollidable) this;		
			ArrayList<AbstractNode> colliders = collision.collidesWith();
			
			if (colliders != null) {
				for (Iterator<AbstractNode> it = colliders.iterator(); it.hasNext();) {
					
					AbstractNode node = (AbstractNode) it.next();
				
					// Check intersection and invoke collision callback.
					if (collision.getBounds().intersects(node.getWorldBound()) && node.isActive()) {
						collision.onCollision(node);
					}		
				}
			} 
		}
	}

	@Override
	public void onCollision(AbstractNode collidedWith) {
		// MAKE DAMAGE
		if (collidedWith instanceof IDamageTaker && this instanceof IDamageMaker) {
			// Notice that heats make "positive" damage
			((IDamageTaker) collidedWith).onDamage(((IDamageMaker) this).getDamage());
		}
		// TAKE DAMAGE
		if (collidedWith instanceof IDamageMaker && this instanceof IDamageTaker) {
			((IDamageTaker) this).onDamage(((IDamageMaker) collidedWith).getDamage());
		}

		// SELF DESTRUCTION
		if (this instanceof IDestroyable) {
			IDestroyable destroyable = (IDestroyable) this;
			if (destroyable.destroyOnCollision()) {
				destroyable.destroy();
				// AUDIOdd
				if (destroyable instanceof IAudible) {
					IAudible audible = (IAudible) destroyable;
					if (audible.playSoundOnDestroy()) {
						audible.playAudio();
					}
				}
			}
		}
		// COLLIDER DESTRUCTION
		if (collidedWith instanceof IDestroyable) {
			IDestroyable destroyable = (IDestroyable) collidedWith;
			if (destroyable.destroyOnCollision()) {
				destroyable.destroy();
			}
		}

		// SEND SCORE
		if (this instanceof IScoreGiver && collidedWith instanceof IScoreTaker) {
			IScoreTaker scoreTaker = (IScoreTaker) collidedWith;
			IScoreGiver scoreGiver = (IScoreGiver) this;
			
			if (scoreGiver.isScoreCounted()) {
				scoreTaker.onScore(scoreGiver.getScore());
			}		
			//((IScoreTaker) collidedWith).onScore(((IScoreGiver) this).getScore());			
		}
		// GET SCORE
		if (this instanceof IScoreTaker && collidedWith instanceof IScoreGiver) {
			IScoreTaker scoreTaker = (IScoreTaker) this;
			IScoreGiver scoreGiver = (IScoreGiver) collidedWith;

			if (scoreGiver.isScoreCounted()) {
				scoreTaker.onScore(scoreGiver.getScore());
			}
		}

		// GIVE COINS
		if (this instanceof ICoinGiver && collidedWith instanceof ICoinTaker) {
			((ICoinTaker) collidedWith).onCoins(((ICoinGiver) this).getCoins());
		}
		// TAKE COINS
		if (this instanceof ICoinTaker && collidedWith instanceof ICoinGiver) {
			((ICoinTaker) this).onCoins(((ICoinGiver) collidedWith).getCoins());
		}
	}

	@Override
	public BoundingVolume getBounds() {
		return spatial.getWorldBound();
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void trySpawn() {
		if (this instanceof ISpawnable) {
			ISpawnable spawner = (ISpawnable) this;
			if (spawner.isReadyToSpawn()) {
				rootNode.attachChild(this);
				setLocalTranslation(getSpawnCoordinates());
				
				
				// PREVENT COLLISIONS
				ArrayList<AbstractNode> n = spawner.getNodesPreventCollisionsWhenSpawn();
				if (n != null) {
					// Hide if collides with something else
					setCullHint(CullHint.Always);
					for (Iterator<AbstractNode> it = n.iterator(); it.hasNext();) {
						AbstractNode an = (AbstractNode) it.next();
						if (this.getBounds().intersects(an.getBounds())) {
							rootNode.detachChild(this);
							return;
						}
					}
					// SHOW AGAIN
					setCullHint(CullHint.Never);
				}
				game.getUpdateables().add(this, false);
			}
		}
	}
	
	public Vector3f getSpawnCoordinates() {
		Random random = new Random();
		
		Vector3f position = game.getCamera().getLocation().add(0f, 0f, SPAWN_ZDISTANCE_FROM_CAM);
		position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 40f;
		position.y = 0;
		
		return position;
	}
		
}
