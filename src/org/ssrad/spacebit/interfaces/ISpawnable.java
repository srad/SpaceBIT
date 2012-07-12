package org.ssrad.spacebit.interfaces;

import java.util.ArrayList;

import org.ssrad.spacebit.nodes.AbstractNode;

public interface ISpawnable {
	
	public boolean isReadyToSpawn();

	public ArrayList<AbstractNode> getCollisionAvoiders();

}
