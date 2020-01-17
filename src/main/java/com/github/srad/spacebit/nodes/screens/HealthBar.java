package com.github.srad.spacebit.nodes.screens;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.github.srad.spacebit.game.Game;
import com.github.srad.spacebit.nodes.entity.AbstractNode;

import java.util.ArrayList;

public class HealthBar extends AbstractNode {

  private final int HEALTHBAR_WIDTH = 275;
  private final int HEALTHBAR_HEIGHT = 30;

  private Geometry fg;
  private Geometry bg;
  private BitmapText health;

  public HealthBar(Game game) {
    super(game);
  }

  @Override
  protected void init() {
    // BACKGROUND
    Quad quad = new Quad(HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT);

    Material m_bg = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    m_bg.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    m_bg.setTexture("ColorMap", assetManager.loadTexture("interface/health_bg.png"));

    bg = new Geometry("healthbar_bg", quad);
    bg.setMaterial(m_bg);

    attachChild(bg);

    // FOREGROUND
    Material m_fg = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    m_fg.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    m_fg.setTexture("ColorMap", assetManager.loadTexture("interface/health_fg.png"));

    fg = new Geometry("healthbar_fg", quad.clone());
    fg.setMaterial(m_fg);
    fg.move(0, 0.1f, 0);

    attachChild(fg);

    // TEXT
    health = new BitmapText(game.getGuiFont(), false);
    health.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
    health.setColor(ColorRGBA.White);                             // font color
    health.setLocalTranslation(0, -5, 0.1f); // position

    attachChild(health);
  }

  @Override
  public void update(float tpf) {
    health.setText("Health  " + game.getShip().getHealth());
    setPercentage((float) game.getShip().getHealth() / 100);
  }

  public void setPercentage(float percent) {
    if (percent > 1f) {
      percent = 1f;
    } else if (percent < 0f) {
      percent = 0f;
    }
    fg.setLocalScale(percent, 1f, 1f);
  }

  @Override
  public void onCollision(AbstractNode collidedWith) {
  }

  @Override
  public ArrayList<AbstractNode> collidesWith() {
    return null;
  }

}
