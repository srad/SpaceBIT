package com.github.srad.spacebit.nodes.entity;

import com.github.srad.spacebit.interfaces.IDamageMaker;
import com.github.srad.spacebit.interfaces.IDestroyable;
import com.github.srad.spacebit.interfaces.IScoreTaker;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import com.github.srad.spacebit.audio.GameAudio;
import com.github.srad.spacebit.audio.enums.SoundType;
import com.github.srad.spacebit.game.Game;

import java.util.ArrayList;

public class Laser extends AbstractNode implements IDamageMaker, IDestroyable, IScoreTaker {

  float moveDistance = 0f;
  float timer = 0.5f;

  GameAudio audio;

  public Laser(Game game) {
    super(game);
  }

  @Override
  protected void init() {
    Cylinder c = new Cylinder(8, 10, .15f, 4f);
    spatial = new Geometry("c", c);

    material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setColor("Color", ColorRGBA.Red);
    material.setColor("GlowColor", ColorRGBA.Pink);

    spatial.setMaterial(material);

    attachChild(spatial);
    setLocalTranslation(game.getShip().getLocalTranslation());
    move(0, 0, 6f);

    audio = new GameAudio(game, this, SoundType.LASER);
    //audio.setVolume(2f);
    audio.play();
  }

  @Override
  public void update(float tpf) {
    super.update(tpf);
    timer += tpf;

    float deltaMove = FastMath.exp(timer) / 2.5f;

    moveDistance += deltaMove;

    move(0, 0, deltaMove);

    if (moveDistance > 80f) {
      active = false;
    }
  }

  @Override
  public ArrayList<AbstractNode> collidesWith() {
    ArrayList<AbstractNode> nodes = new ArrayList<AbstractNode>();

    nodes.addAll(game.getUpdateables().getApes());
    nodes.addAll(game.getUpdateables().getUfos());
    nodes.addAll(game.getUpdateables().getPlanets());

    return nodes;
  }

  @Override
  public int getDamage() {
    return -5;
  }

  @Override
  public boolean destroyOnCollision() {
    return true;
  }

  @Override
  public void onScore(int score) {
    // The laser represent the ship
    // so it sends its score to the ship
    game.getShip().onScore(score);
  }

}
