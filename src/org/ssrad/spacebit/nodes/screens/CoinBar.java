package org.ssrad.spacebit.nodes.screens;

import org.ssrad.spacebit.game.Game;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

public class CoinBar extends AbstractScreen {

	public CoinBar(Game game) {
		super(game);
	}
	
	private final int COIN_WIDTH = 37;
	private final int COIN_HEIGHT = 35;
	
	private Geometry goldCoins;
	private Geometry greyCoins;
	private Node coinsNode;
	private BitmapText coins;

	@Override
	protected void init() {	
		coinsNode = new Node();

		// Gold
		Quad quad = new Quad(COIN_WIDTH, COIN_HEIGHT);
		goldCoins = new Geometry("coinbar", quad);
		
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		material.setTexture("ColorMap", assetManager.loadTexture("coin.png"));
		
		goldCoins.setMaterial(material);
		
		// Grey
		greyCoins = new Geometry("coinbar", quad.clone());
		
		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		material.setTexture("ColorMap", assetManager.loadTexture("coin_off.png"));
		
		greyCoins.setMaterial(material);
		
		// TEXT		
		coins = new BitmapText(game.getGuiFont(), false);          
		coins.setSize(game.getGuiFont().getCharSet().getRenderedSize() * 1.5f);      // font size
		coins.setColor(ColorRGBA.White);                             // font color
		coins.setLocalTranslation(0, -5, 0.1f); // position

		attachChild(coins);
	}
	
	@Override
	public void update(float tpf) {
		detachChild(coinsNode);
		coinsNode = new Node();
			
		// Gold coins
		int goldCoinCount = (int)((float)game.getShip().getCoins() / 10f);
		int counter = 0;
		// Make couple more
		for (; counter < goldCoinCount; counter += 1) {
			Geometry temp_gold_coin = goldCoins.clone();
			temp_gold_coin.move(counter * 20, 0, counter * -0.1f);
			coinsNode.attachChild(temp_gold_coin);
		}
		
		// Grey coins
		for (; counter < 10; counter += 1) {
			Geometry temp_grey_coin = greyCoins.clone();
			temp_grey_coin.move(counter * 20, 0, counter * -0.1f);
			coinsNode.attachChild(temp_grey_coin);
		}
		
		attachChild(coinsNode);
		coins.setText("Power Rings  " + game.getShip().getCoins());
	}

}
