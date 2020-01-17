package com.github.srad.spacebit.game;

import com.github.srad.spacebit.enums.GameLevel;
import com.github.srad.spacebit.helpers.LogHelper;
import com.github.srad.spacebit.nodes.entities.*;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Extremely simplified entity manager.
 *
 * @author srad
 */
public class Entities extends ConcurrentLinkedQueue<AbstractNode> {

  private Game game;
  private Node rootNode;
  private Random random;
  private float stopWatch = 0f;
  private final float SPAWN_ZDISTANCE_FROM_CAM = 60f;

  public Entities(Game game) {
    this.game = game;
    this.rootNode = game.getRootNode();
    this.random = new Random();
  }

  // Callbacks for better flexibility in case something more is done before spawn.
  private Supplier<AbstractNode> torus = () -> new TorusCoin(game);
  private Supplier<AbstractNode> hear = () -> new Heart(game);
  private Supplier<AbstractNode> star = () -> new Star(game);
  private Supplier<AbstractNode> warp = () -> new WarpCore(game);
  private Supplier<AbstractNode> planet = () -> new Planet(game);
  private Supplier<AbstractNode> ape = () -> new Ape(game);
  private Supplier<AbstractNode> ufo = () -> new Ufo(game);

  public void update(float tpf) {
    if (size() > 0) {
      for (Iterator<AbstractNode> it = iterator(); it.hasNext(); ) {
        var entity = it.next();
        if (!entity.isActive()) {
          entity.destroy();
          it.remove();
        } else {
          entity.update(tpf);
        }
      }
    }

    if (stopWatch > 0.3f) {
      stopWatch = 0f;

      spawn(torus, 17);
      spawn(hear, 16);
      spawn(star, 1);
      spawn(warp, 18);
      spawn(planet, 17);

      if (game.getLevel() == GameLevel.LEVEL_ONE) {
        spawn(ape, 11, true);
      } else if (game.getLevel() == GameLevel.LEVEL_TWO) {
        spawn(ufo, 15, true);
      }
    }

    stopWatch += tpf;
  }

  private void spawn(Supplier<AbstractNode> supplier, int prob) {
    spawn(supplier, prob, false);
  }

  private void spawn(Supplier<AbstractNode> supplier) {
    spawn(supplier, 1, false);
  }

  private void spawn(Supplier<AbstractNode> supplier, int prob, boolean ignoreCollision) {
    if ((random.nextInt(20) > prob)) {
      Vector3f position = getSpawnCoordinates();
      var e = supplier.get();
      e.setLocalTranslation(position);
      e.setCullHint(CullHint.Always);

      // Check for free space
      for (Iterator<AbstractNode> it = iterator(); it.hasNext(); ) {
        final var e2 = it.next();

        // Test for intersection, and remove if so. Only insert at an empty space.
        rootNode.attachChild(e);
        if (ignoreCollision == false && e.getWorldBound().intersects(e2.getWorldBound())) {
          rootNode.detachChild(e);
          return;
        }
      }
      e.setCullHint(CullHint.Never);
      super.add(e);
      LogHelper.getLogger().info("Adding entity");
    }
  }

  public Vector3f getSpawnCoordinates() {
    Vector3f position = game.getCamera().getLocation().add(0f, 0f, SPAWN_ZDISTANCE_FROM_CAM);
    position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 40f;
    position.y = 0;

    return position;
  }

  @Override
  public boolean add(AbstractNode node) {
    final var b = super.add(node);
    rootNode.attachChild(node);
    return b;
  }

  public ArrayList<AbstractNode> getType(Class<? extends AbstractNode> type) {
    return stream()
        .filter(e -> type.isInstance(e))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public void destroyObstacles() {
    if (size() > 0) {
      for (Iterator<AbstractNode> it = iterator(); it.hasNext(); ) {
        final var e = it.next();
        it.remove();
        e.destroy();
      }
    }
  }
}
