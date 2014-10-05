/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network;

/**
 *
 * @author svt
 */
public class NumsOfDice {
    private final int left;
    private final int right;

    public NumsOfDice(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "NumsOfDice{" + "left=" + left + ", right=" + right + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.left;
        hash = 19 * hash + this.right;
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
        final NumsOfDice other = (NumsOfDice) obj;
        if (this.left != other.left) {
            return false;
        }
        if (this.right != other.right) {
            return false;
        }
        return true;
    }
    
}
