/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.io.IOException;
import ru.MainGame.DiceNumbers;

//    private DominoGameTable.DiceGameWrapper getWrapperAt(Dice dice,

//            DominoGameTable.DiceGameWrapper firstWrap,DominoGameTable.DiceGameWrapper previous){
public class DiceTableWrapper implements Savable{
    public DiceTableWrapper TopAdjacent = null;
    public DiceTableWrapper BottomAdjacent = null;
    public DiceTableWrapper LeftNum_LeftAdjacent = null;
    public DiceTableWrapper LeftNum_TopAdjacent = null;
    public DiceTableWrapper LeftNum_RightAdjacent = null;
    public DiceTableWrapper RightNum_TopAdjacent = null;
    public DiceTableWrapper RightNum_LeftAdjacent = null;
    public DiceTableWrapper RightNum_RightAdjacent = null;

    public DiceTableWrapper(DiceTableWrapper TopadjAcent, DiceTableWrapper BottomAdjacent,
            DiceTableWrapper LeftNum_LeftAdjacent, DiceTableWrapper LeftNum_TopAdjacent,
            DiceTableWrapper LeftNum_RightAdjacent, DiceTableWrapper RightNum_TopAdjacent,
            DiceTableWrapper RightNum_LeftAdjacent, DiceTableWrapper RightNum_RightAdjacent) {
        this.TopAdjacent = TopadjAcent;
        this.BottomAdjacent = BottomAdjacent;
        this.LeftNum_LeftAdjacent = LeftNum_LeftAdjacent;
        this.LeftNum_TopAdjacent = LeftNum_TopAdjacent;
        this.LeftNum_RightAdjacent = LeftNum_RightAdjacent;
        this.RightNum_TopAdjacent = RightNum_TopAdjacent;
        this.RightNum_LeftAdjacent = RightNum_LeftAdjacent;
        this.RightNum_RightAdjacent = RightNum_RightAdjacent;
    }

    public boolean isLeftFree(){
	return  LeftNum_LeftAdjacent  == null &&
		LeftNum_TopAdjacent   == null &&
		LeftNum_RightAdjacent == null;
    }

    public boolean isRightFree(){
	return  RightNum_TopAdjacent  == null &&
		RightNum_LeftAdjacent   == null &&
		RightNum_RightAdjacent == null;
    }
    /**
     *
     * @return this function eturn left or right free side of dice in table
     * or BOTH_NUM if both of num free, or null
     */
    public DiceNumbers getFreeSideNum(){
	if(isLeftFree() && isRightFree()) return DiceNumbers.BOTH_NUM;
	else if(isLeftFree()) return DiceNumbers.LEFT_NUM;
	else if(isRightFree()) return DiceNumbers.RIGHT_NUM;
	else return null;
    }
    public DiceTableWrapper() {
    }

    public void write(JmeExporter ex) throws IOException {
    }

    public void read(JmeImporter im) throws IOException {
    }

}
