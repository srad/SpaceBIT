
package org.ssrad.spacebit.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.ssrad.spacebit.enums.GameLevel;
import org.ssrad.spacebit.nodes.AbstractNode;
import org.ssrad.spacebit.nodes.Ape;
import org.ssrad.spacebit.nodes.BlackHole;
import org.ssrad.spacebit.nodes.Heart;
import org.ssrad.spacebit.nodes.Planet;
import org.ssrad.spacebit.nodes.Star;
import org.ssrad.spacebit.nodes.TorusCoin;
import org.ssrad.spacebit.nodes.Ufo;
import org.ssrad.spacebit.nodes.WarpCore;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * This class contains most entities that needs to be updated
 * within the main game loop.
 * 
 * @author Saman Sedighi Rad
 *
 */
public class Updateables {

	private Game game;
	private Node rootNode;
	private Random random;
	private float stopWatch = 0f;
	private final float SPAWN_ZDISTANCE_FROM_CAM = 60f;

	private List<AbstractNode> updates = new CopyOnWriteArrayList<AbstractNode>();

	public Updateables(Game game) {
		this.game = game;
		this.rootNode = game.getRootNode();
		this.random = new Random();
	}

	public void update(float tpf) {

		if (updates.size() > 0) {
			for (Iterator<AbstractNode> it = updates.iterator(); it.hasNext();) {
				AbstractNode node = (AbstractNode) it.next();
				if (node.isActive()) {
					node.update(tpf);
				} else {					
					updates.remove(node);
					node.destroy();
				}
			}  
		}
		
		if (stopWatch > 0.3f) {
			stopWatch = 0f;
			
			new TorusCoin(game).trySpawn();
			new Heart(game).trySpawn();
			new Star(game).trySpawn();
			new WarpCore(game).trySpawn();
			new BlackHole(game).trySpawn();
			new Planet(game).trySpawn();
			
			if (game.getLevel() == GameLevel.LEVEL_ONE) {
				new Ape(game).trySpawn();
			} else if (game.getLevel() == GameLevel.LEVEL_TWO) {
				new Ufo(game).trySpawn();
			}
		}
		stopWatch += tpf;
	}

	/**
	 * Returns nodes of a specific type
	 * @param type
	 * @return The nodes of the given type
	 */
	public <T> ArrayList<AbstractNode> get(Class<T> type) {
		ArrayList<AbstractNode> nodes = new ArrayList<AbstractNode>();
		
		for (Iterator<AbstractNode> it = updates.iterator(); it.hasNext();) {
			AbstractNode node = (AbstractNode) it.next();

			if (node.getClass().equals(type)) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public Vector3f getSpawnCoordinates(AbstractNode n) {
		Vector3f position = game.getCamera().getLocation().add(0f, 0f, SPAWN_ZDISTANCE_FROM_CAM);
		position.x = (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 40f;
		position.y = 0;
		
		return position;
	}

	public ArrayList<AbstractNode> getAllObstracles() {
		ArrayList<AbstractNode> obstracles = new ArrayList<AbstractNode>();
		
		for (Iterator<AbstractNode> it = updates.iterator(); it.hasNext();) {
			AbstractNode node = (AbstractNode) it.next();
			
			if ((node instanceof Ape) || (node instanceof Ufo) || (node instanceof BlackHole) || (node instanceof Planet)) {
				obstracles.add(node);
			}			
		}
		return obstracles;
	}

	public void destroyObstacles() {
		if (updates.size() > 0) {
			for (Iterator<AbstractNode> it = updates.iterator(); it.hasNext();) {
				AbstractNode node = (AbstractNode) it.next();
				
				if ((node instanceof Ape) || (node instanceof Ufo) || (node instanceof BlackHole) || (node instanceof Planet)) {
					updates.remove(node);
					node.destroy();
				}			
			}
		}
	}
	
	public void add(AbstractNode abstractNode) {
		add(abstractNode, true);
	}
	
	/**
	 * They right way to do this would be polymorpism,
	 * but this is more compact.
	 */
	public void add(AbstractNode abstractNode, boolean attachToRoot) {
		updates.add(abstractNode);
		if (attachToRoot) {
			rootNode.attachChild(abstractNode);
		}
	}

}
