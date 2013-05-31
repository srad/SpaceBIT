package org.ssrad.spacebit.nodes;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.ICoinGiver;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;

import java.util.ArrayList;

public class TorusCoin extends AbstractNode implements ICoinGiver, IDestroyable, IScoreGiver {

    public TorusCoin(Game game) {
        super(game);
    }

    @Override
    protected void init() {
        spatial = assetManager.loadModel("toruscoin/ring.obj");

        // Simple glow material looks nicer than texture
        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        material.setColor("Color", ColorRGBA.Yellow);
        material.setColor("GlowColor", ColorRGBA.Green);

        Texture tex_ml = assetManager.loadTexture("toruscoin/ring.png");
        material.setTexture("ColorMap", tex_ml);

        spatial.setMaterial(material);
        scale(1.4f);

        setShadowMode(ShadowMode.Cast);

        addLight();

        attachChild(spatial);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        spatial.rotate(0, FastMath.PI * tpf, 0);
        light.setPosition(getLocalTranslation());
    }

    private void addLight() {
        light = new PointLight();
        light.setColor(ColorRGBA.Yellow);
        light.setRadius(20f);

        game.getRootNode().addLight(light);
    }

    public Spatial getSpatial() {
        return spatial;
    }

    @SuppressWarnings("serial")
    @Override
    public ArrayList<AbstractNode> collidesWith() {
        return new ArrayList<AbstractNode>() {{
            add(game.getShip());
        }};
    }

    @Override
    public int getCoins() {
        return 1;
    }

    @Override
    public boolean destroyOnCollision() {
        return true;
    }

    @Override
    public int getScore() {
        return 1;
    }

    @Override
    public boolean isScoreCounted() {
        return active == false;
    }

}
