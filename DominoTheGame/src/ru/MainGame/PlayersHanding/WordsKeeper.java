/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.PlayersHanding;

/**
 *
 * @author svt
 */
public enum WordsKeeper {
    USER_DATA_DICE_CAN_STEP("dice can step"),
    USER_DATA_DICE_IS_BACKLIGHT("dice is backlight");
    

    private WordsKeeper(String map) {
        this.map = map;
    }
    String map;

    @Override
    public String toString() {
        return map;
    }
    
}
