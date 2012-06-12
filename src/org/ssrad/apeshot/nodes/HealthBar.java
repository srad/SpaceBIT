package org.ssrad.apeshot.nodes;

import org.ssrad.apeshot.game.Game;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class HealthBar extends ANode {
	
	private final int HEALTHBAR_WIDTH = 275;
	private final int HEALTHBAR_HEIGHT = 30;
	
	private Geometry fg;
	private Geometry bg;

	public HealthBar(Game game) {
		super(game);
	}

	@Override
	protected void init() {
		// BACKGROUND
		Quad quad = new Quad(HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT);
		
		Material m_bg = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		m_bg.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		m_bg.setTexture("ColorMap", assetManager.loadTexture("health_bg.png"));
		
		bg = new Geometry("healthbar_bg", quad);
		bg.setMaterial(m_bg);
		
		attachChild(bg);
		
		// FOREGROUND
		Material m_fg = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		m_fg.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		m_fg.setTexture("ColorMap", assetManager.loadTexture("health_fg.png"));
		
		fg = new Geometry("healthbar_fg", quad.clone());
		fg.setMaterial(m_fg);
		fg.move(0, 0.1f, 0);
		
		attachChild(fg);
	}
	
	@Override
	public void update(float tpf) {
	}
	
	public void setPercentage (float percent) {
		if (percent > 1f) {
			percent = 1f;
		} else if (percent < 0f) {
			percent = 0f;
		}
		fg.setLocalScale(percent, 1f, 1f);
	}
	
	@Override
	public void destroy() {
		game.getRootNode().detachChild(this);
	}

}
