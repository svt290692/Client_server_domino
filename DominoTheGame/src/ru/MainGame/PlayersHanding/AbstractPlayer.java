/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import ru.MainGame.HeapState;
import ru.MainGame.TableHanding.DiceAnimator;
import ru.MainGame.TableHanding.DiceSimpleAnimator;

/**
 *
 * @author svt
 */
public abstract class AbstractPlayer{


    private final PlayersPlaces Place;
    protected final Node myNode;
    private final Node rootNode;

    private final HeapState heap;
    protected final Queue<Spatial> queueAddToScreenDices;
    protected final Object Mutex = new Object();
    protected String name;

    public AbstractPlayer(PlayersPlaces Place,Node rootNode, HeapState heap,String name) {
        this.Place = Place;
        this.name = name;
        PlayersState.registerPlace(Place);
        this.heap = heap;
	this.rootNode = rootNode;
        myNode = new Node("Player, " + "loc:" + getPlace());
        myNode.setLocalTranslation(Place.getVector());
        rootNode.attachChild(myNode);
        this.queueAddToScreenDices = new ConcurrentLinkedQueue<>();
    }

    public final List<Spatial> getHand() {
	List<Spatial> list = new ArrayList<>(10);
	for(Spatial s : myNode.getChildren()){
	    list.add(s);
	}
        return list;
    }

    public HeapState getHeap() {
        return heap;
    }


    public final PlayersPlaces getPlace() {
        return Place;
    }

    public final Node getNode() {
        return myNode;
    }

    protected boolean TakeFromHeap(int left,int rught){
        Spatial d = heap.getDice(left, rught);
        if(null == d){
            d = heap.getDice(rught, left);
            if(null == d)
            return false;
        }
        synchronized(Mutex){
            queueAddToScreenDices.add(d);
        }
        return true;
    }

    public static void sortNodeDices(Node node, float dicesWidth) {
        float startPoint;
        final int size = node.getChildren().size();
        if ((size % 2) == 0) {
            startPoint = (-((size / 2) * dicesWidth));
        } else {
            startPoint = (-((((size + 1) / 2) * dicesWidth) - dicesWidth / 2));
        }
        for (Spatial s : node.getChildren()) {
            
            s.setLocalTranslation(startPoint, 0, 0);
            startPoint += dicesWidth;
        }
    }
    public abstract void sortDices();
    /**
     * this method will be call when application will update to do some
     * update mesh or render something in Application thread
     * becaus if update in another thread the game will crush
     */
    public abstract void UpdateFromApplication();

    public String getName() {
        return name;
    }
}
