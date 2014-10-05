/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author svt
 */
public abstract class AbstractPlayer{

    private final PlayersPlaces Place;
    private final Node myNode;
    private final Node rootNode;

    public AbstractPlayer(PlayersPlaces Place,Node rootNode) {
        this.Place = Place;
	this.rootNode = rootNode;
        myNode = new Node("Player" + "loc:" + getPlace());
        myNode.setLocalTranslation(Place.getVector());
        rootNode.attachChild(myNode);
    }

    public final List<Spatial> getHand() {
	List<Spatial> list = new ArrayList<Spatial>(10);
	for(Spatial s : myNode.getChildren()){
	    list.add(s);
	}
        return list;
    }

    public final PlayersPlaces getPlace() {
        return Place;
    }

    public final Node getNode() {
        return myNode;
    }

}
