/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author svt
 */
public class SimpleDicesLoader implements ModelsLoader{
    private final AssetManager assetManager;

    public SimpleDicesLoader(AssetManager assetManager) {
	this.assetManager = assetManager;
    }
    public List<Spatial> load()throws IOException{

	ArrayList<Spatial> list = new ArrayList<Spatial>();

	int countFigurInLine = 7;
        int numDice = 0;

        while(countFigurInLine >= 0){
	    for(int i = 0; i < countFigurInLine;i++){
		Spatial DiceModel = assetManager.loadModel(
			"Models/domino/"+numDice+"/dom.j3o");

		if(DiceModel == null){
		    throw new IOException("error Dice Models can not be load from Models/domino/ error in " + numDice + "dice");
		}

		Node n = (Node)DiceModel;
		DiceModel = n.getChild(0);

		list.add(DiceModel);

		int leftNum =  7 - countFigurInLine + i;
		int rightNum = 7 - countFigurInLine;
		DiceModel.setUserData("Name", "Dice L<"+leftNum+">"+" R<"+rightNum+">");

		DiceModel.addControl(new Dice(leftNum,rightNum));
		numDice++;
	    }
	    countFigurInLine--;
	}
	return list;
    }

}
