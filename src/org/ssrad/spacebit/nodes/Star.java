package org.ssrad.spacebit.nodes;

import java.util.Random;

import org.ssrad.spacebit.game.Game;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class Star extends ANode {
	
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

}
