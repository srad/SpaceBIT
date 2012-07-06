package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.game.Game;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

public class ModelScreen extends AbstractScreen implements ActionListener {

	boolean active = true;
	
	Node apeNode, planetNode, shipNode, torusNode, heartNode, warpNode;
	Spatial ape, planet, ship, torus, heart, warp;

	public ModelScreen(Game game) {
		super(game);
	}
	
	@Override
	protected void init() {
		super.init();		
		
		initHeart();
		initTorus();
		initApe();
		addShip();
		addWarp();
		addPlanet();
	}

	public void update(float tpf) {
		game.getGuiNode().detachAllChildren();

		ape.rotate(0, tpf * FastMath.PI / 12, 0);
//		planet.rotate(0, tpf * FastMath.PI / 12, 0);
//		ship.rotate(0, tpf * FastMath.PI / 12, 0);
//		torus.rotate(0, tpf * FastMath.PI / 12, 0);
//		heart.rotate(0, tpf * FastMath.PI / 12, 0);
//		warp.rotate(0, tpf * FastMath.PI / 12, 0);
	}
	
	private void initHeart() {
		
	}
	private void initTorus() {
		
	}
	private void initApe() {
		game.getRootNode().detachAllChildren();
		
		apeNode = new Node();
		ape = assetManager.loadModel("affe.obj");

		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		Texture tex_ml = assetManager.loadTexture("affe.png");
		material.setTexture("ColorMap", tex_ml);
		ape.setMaterial(material);
		
		apeNode.attachChild(ape);
		//game.getGuiNode().attachChild(apeNode);
		game.getGuiNode().attachChild(ape);
		ape.setLocalTranslation(game.getCamera().getLocation().add(0, 0, 20f));
	}
	private void addShip() {

	}
	private void addWarp() {
		
	}
	private void addPlanet() {
		
	}
	
	@Override
	protected void bindKeys() {
		inputManager.addMapping("back", new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addListener(this, new String[] { "back" });
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {

		if (name.equals("back") && !keyPressed) {
			hide();
			game.getTitleScreen().show();
		}
	
	}
	
}
