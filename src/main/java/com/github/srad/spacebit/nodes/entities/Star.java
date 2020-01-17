package com.github.srad.spacebit.nodes.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.github.srad.spacebit.game.Game;

import java.util.ArrayList;
import java.util.Random;

public class Star extends AbstractNode {

  private Random r;

  public Star(Game game) {
    super(game);
  }

  @Override
  protected void init() {
    r = new Random();

    Sphere sphere = new Sphere(30, 30, r.nextFloat() + .2f);
    spatial = (Spatial) new Geometry("star", sphere);

    material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setColor("Color", ColorRGBA.Yellow);
    material.setColor("GlowColor", getRandomColor());

    spatial.setMaterial(material);
    // Move far away from scenery
    move(0, r.nextFloat() * 20 + 20f, 0);
    scale(0.1f);
    attachChild(spatial);
  }

  private ColorRGBA getRandomColor() {
    switch (r.nextInt(20)) {
    case 0:
      return ColorRGBA.Yellow;
    case 1:
      return ColorRGBA.Orange;
    default:
      return ColorRGBA.White;
    }
  }

  @Override
  public void update(float tpf) {
    super.update(tpf);

    // TODO: move in black hole direction, optional
//		ArrayList<BlackHole> blackHoles = game.getUpdateables().getBlackHoles();
//		
//		for (Iterator<BlackHole> iterator = blackHoles.iterator(); iterator.hasNext();) {
//			BlackHole blackHole = (BlackHole) iterator.next();
//			
//			if (getLocalTranslation().clone().subtract(blackHole.getLocalTranslation().clone()).length() < 20f) {
//				// Move towards black holes
//				Vector3f v = new Vector3f(blackHole.getWorldTranslation().multLocal(Vector3f.UNIT_Z));
//		        Vector3f blackHoleVec = v.mult(tpf/10f);
//				move(blackHoleVec);
//			}
//		}
  }

  @Override
  public void onCollision(AbstractNode collidedWith) {
  }

  @Override
  public ArrayList<AbstractNode> collidesWith() {
    return null;
  }

}
