/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import java.util.Random;

/**
 *
 * @author svt
 */
public enum DiceNumbers {
    LEFT_NUM, RIGHT_NUM, BOTH_NUM;

    static DiceNumbers getRandomSide() {
	Random r = new Random();
	switch (r.nextInt(3)) {
	    case 0:
		return LEFT_NUM;
	    case 1:
		return RIGHT_NUM;
	    case 2:
		return BOTH_NUM;
	}
	return BOTH_NUM;
    }

}