/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network;

/**
 *
 * @author svt
 */
public class StepToSend {
    int handLeft,handRight;
    
    int tableLeft,tableRight;

    public StepToSend() {
    }

    public int getHandLeft() {
        return handLeft;
    }

    public int getHandRight() {
        return handRight;
    }

    public int getTableLeft() {
        return tableLeft;
    }

    public int getTableRight() {
        return tableRight;
    }

    public void setHandLeft(int handLeft) {
        this.handLeft = handLeft;
    }

    public void setHandRight(int handRight) {
        this.handRight = handRight;
    }

    public void setTableLeft(int tableLeft) {
        this.tableLeft = tableLeft;
    }

    public void setTableRight(int tableRight) {
        this.tableRight = tableRight;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.handLeft;
        hash = 53 * hash + this.handRight;
        hash = 53 * hash + this.tableLeft;
        hash = 53 * hash + this.tableRight;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StepToSend other = (StepToSend) obj;
        if (this.handLeft != other.handLeft) {
            return false;
        }
        if (this.handRight != other.handRight) {
            return false;
        }
        if (this.tableLeft != other.tableLeft) {
            return false;
        }
        if (this.tableRight != other.tableRight) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StepToSend{" + "handLeft=" + handLeft + ", handRight=" + handRight +
                ", tableLeft=" + tableLeft + ", tableRight=" + tableRight + '}';
    }
    
    
}
