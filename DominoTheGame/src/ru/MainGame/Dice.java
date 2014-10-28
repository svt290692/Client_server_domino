package ru.MainGame;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * this class provide dice entity for dice Models
 * @author svt
 */
public class Dice extends AbstractControl implements Cloneable{
    private int leftNum;
    private int rightNum;

    //    private Spatial Model;
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.leftNum;
        hash = 17 * hash + this.rightNum;
        return hash;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
	return new Dice(leftNum, rightNum);
    }

    @Override
    public String toString() {
	return "Dice with nums: R<" + rightNum + "> L<" + leftNum + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dice other = (Dice) obj;
        if (this.leftNum != other.leftNum) {
            return false;
        }
        if (this.rightNum != other.rightNum) {
            return false;
        }
        return true;
    }
    
    Dice(int LeftNum,int RightNum){
        this.leftNum = LeftNum;
        this.rightNum = RightNum;
//        this.Model = Model;
    }
    
    Spatial getModel(){
        return spatial;
    }
    
    public int getLeftNum(){
        return leftNum;
    }
    
    public int getRightNum(){
        return rightNum;
    }
    
    /**
     * @return return num if left and right num equals else return -1
     */
    public int getBothNum(){
	if(leftNum == rightNum)
	    return leftNum;
	else return -1;
    }
    
    public int getNum(DiceNumbers num){
	if(num == DiceNumbers.LEFT_NUM) return leftNum;
	else if(num == DiceNumbers.RIGHT_NUM) return rightNum;
	else if(num == DiceNumbers.BOTH_NUM && leftNum == rightNum) return leftNum;
	else return -1;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}