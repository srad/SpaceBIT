package com.github.srad.spacebit.nodes.screens;

import com.github.srad.spacebit.game.Game;
import com.github.srad.spacebit.nodes.entity.AbstractNode;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import java.util.ArrayList;

public class HeartBar extends AbstractNode {

  private final int COIN_WIDTH = 40;
  private final int COIN_HEIGHT = 35;

  private Geometry heartGeometry;
  private Node heartsNode;
  private BitmapText hearts;

  public HeartBar(Game game) {
    super(game);
  }

  @Override
  protected void init() {
    heartsNode = new Node();

    Quad quad = new Quad(COIN_WIDTH, COIN_HEIGHT);
    heartGeometry = new Geometry("coinbar", quad);

    material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    material.setTexture("ColorMap", assetManager.loadTexture("interface/heart.png"));

    heartGeometry.setMaterial(material);

    // TEXT
    hearts = new BitmapText(game.getGuiFont(), false);
    hearts.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
    hearts.setColor(ColorRGBA.White);                             // font color
    hearts.setLocalTranslation(0, -5, 0.1f); // position

    attachChild(hearts);
  }

  @Override
  public void update(float tpf) {
    detachChild(heartsNode);
    heartsNode = new Node();

    for (int i = 0; i < game.getShip().getLives(); i += 1) {
      Geometry temp_heart = heartGeometry.clone();
      temp_heart.move(i * 20, 0, i * -0.1f);
      heartsNode.attachChild(temp_heart);
    }

    attachChild(heartsNode);
    hearts.setText("Lives  " + game.getShip().getLives());
  }

  @Override
  public void onCollision(AbstractNode collidedWith) {
  }

  @Override
  public ArrayList<AbstractNode> collidesWith() {
    return null;
  }

}
