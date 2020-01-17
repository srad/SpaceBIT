package com.github.srad.spacebit.nodes.entity;

import com.github.srad.spacebit.interfaces.IAudible;
import com.github.srad.spacebit.interfaces.IDamageMaker;
import com.github.srad.spacebit.interfaces.IDestroyable;
import com.github.srad.spacebit.interfaces.IScoreGiver;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.github.srad.spacebit.audio.GameAudio;
import com.github.srad.spacebit.audio.enums.SoundType;
import com.github.srad.spacebit.game.Game;

import java.util.ArrayList;

public class Heart extends AbstractNode implements IDamageMaker, IDestroyable, IScoreGiver, IAudible {

  public Heart(Game game) {
    super(game);
  }

  @Override
  protected void init() {
    material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

    material.setColor("Color", ColorRGBA.Red);
    material.setColor("GlowColor", ColorRGBA.Red);

    spatial = assetManager.loadModel("meshes/heart.obj");
    spatial.setMaterial(material);
    attachChild(spatial);
    addLight();

    setShadowMode(ShadowMode.Cast);
  }

  @Override
  public void update(float tpf) {
    super.update(tpf);
    spatial.rotate(0, FastMath.PI * tpf * 2.5f, 0);
    light.setPosition(getLocalTranslation());
  }

  private void addLight() {
    light = new PointLight();
    light.setColor(ColorRGBA.Red);
    light.setRadius(20f);

    game.getRootNode().addLight(light);
  }

  @SuppressWarnings("serial")
  @Override
  public ArrayList<AbstractNode> collidesWith() {
    return new ArrayList<AbstractNode>() {{
      add(game.getShip());
    }};
  }

  @Override
  public int getDamage() {
    return 7;
  }

  @Override
  public boolean destroyOnCollision() {
    return true;
  }

  @Override
  public int getScore() {
    return 5;
  }

  @Override
  public boolean isScoreCounted() {
    return active == false;
  }

  @Override
  public boolean playSoundOnDestroy() {
    return true;
  }

  @Override
  public void playAudio() {
    GameAudio audio = new GameAudio(game, this, SoundType.HEART);
    //audio.setVolume(1f);
    audio.play();
  }

}
