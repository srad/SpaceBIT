package com.github.srad.spacebit.nodes.entities;

import com.github.srad.spacebit.game.Game;
import com.github.srad.spacebit.interfaces.*;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Super class for all entities.
 * <p/>
 * I know that jmonkey wiki advises not to use an own node super class
 * but I don't trust their implementation.
 *
 * @author srad
 */
public abstract class AbstractNode extends Node implements ICollidable {

  protected Game game;
  protected AssetManager assetManager;
  protected boolean active = true;
  protected float scrollSpeed = 0f;
  protected Random random;

  protected Spatial spatial = null;
  protected Material material = null;
  protected PointLight light = null;

  private static float updateTimer = 0f;

  public AbstractNode(Game game) {
    this.game = game;
    this.assetManager = game.getAssetManager();
    this.scrollSpeed = game.SCROLL_SPEED;
    init();
  }

  protected abstract void init();

  public void update(float tpf) {
    updateTimer += tpf;

    if (updateTimer > 0.1f) {
      updateTimer = 0f;
      // Out of sight, remove
      if (game.getCamera().getLocation().z > getLocalTranslation().z) {
        active = false;
      }
      checkCollisions();
    }
  }

  public void destroy() {
    active = false;

    if (light != null) {
      game.getRootNode().removeLight(light);
    }
    game.getRootNode().detachChild(this);
  }

  private void checkCollisions() {
    if (this instanceof ICollidable) {
      ICollidable collision = (ICollidable) this;
      ArrayList<AbstractNode> colliders = collision.collidesWith();

      if (colliders != null) {
        for (Iterator<AbstractNode> it = colliders.iterator(); it.hasNext(); ) {

          AbstractNode anode = (AbstractNode) it.next();

          // Check intersection and invoke collision callback.
          if (collision.getBounds().intersects(anode.getWorldBound()) && anode.isActive()) {
            collision.onCollision(anode);
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
}
