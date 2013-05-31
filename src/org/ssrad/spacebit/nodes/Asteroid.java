package org.ssrad.spacebit.nodes;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import org.ssrad.spacebit.game.Game;
import org.ssrad.spacebit.interfaces.IDamageMaker;
import org.ssrad.spacebit.interfaces.IDamageTaker;
import org.ssrad.spacebit.interfaces.IDestroyable;
import org.ssrad.spacebit.interfaces.IScoreGiver;

import java.util.ArrayList;
import java.util.Random;

public class Asteroid extends AbstractNode implements IDamageMaker, IDamageTaker, IScoreGiver, IDestroyable {

    private Random random;
    private int health;
    private float scale;

    public Asteroid(Game game) {
        super(game);
    }

    @SuppressWarnings("serial")
    @Override
    public ArrayList<AbstractNode> collidesWith() {
        return new ArrayList<AbstractNode>() {{
            add(game.getShip());
        }};
    }

    @Override
    protected void init() {
        random = new Random();

        spatial = this.assetManager.loadModel("asteroid/asteroid.obj");
        material = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");

        material.setTexture("DiffuseMap", this.assetManager.loadTexture("asteroid/asteriod.png"));
        material.setTexture("NormalMap", this.assetManager.loadTexture("asteroid/asteriod_normals.png"));

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Specular", ColorRGBA.Brown);
        material.setColor("Diffuse", ColorRGBA.Brown);
        material.setFloat("Shininess", 128f); // [1,128]

        spatial.setMaterial(material);

        this.scale = random.nextFloat() * 3f + 1f;
        this.health = 5 * ((int) Math.round(this.scale));

        scale(scale);

        attachChild(spatial);
        addLight();
    }

    private void addLight() {
        light = new PointLight();
        light.setColor(ColorRGBA.White);
        light.setRadius(200f);

        game.getRootNode().addLight(light);
        light.setPosition(spatial.getLocalTranslation().clone().addLocal(0, 5, 0));
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        // Random rotation
        spatial.rotate(FastMath.PI / (random.nextInt(5) + 5) * tpf, FastMath.PI / (random.nextInt(5) + 5) * tpf, FastMath.PI / (random.nextInt(5) + 5) * tpf);
    }

    /**
     * The health is proportional to the asteriod's size.
     *
     * @return Asteroid damage.
     */
    @Override
    public int getDamage() {
        return -5 * ((int) Math.round(this.scale));
    }

    @Override
    public boolean destroyOnCollision() {
        return health <= 0;
    }

    @Override
    public void destroy() {
        super.destroy();
        game.getUpdateables().addShockWaveExplosion(new ShockWaveExplosion(game, getLocalTranslation()));
    }

    @Override
    public void onDamage(int damage) {
        health += damage;
    }

    @Override
    public int getScore() {
        return 20;
    }

    @Override
    public boolean isScoreCounted() {
        return true;
    }

}
