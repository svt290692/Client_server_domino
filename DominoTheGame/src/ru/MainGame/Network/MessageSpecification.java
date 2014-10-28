/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network;

/**
 * this enum contain all types of message that will send from server and to server in Messages
 * @author svt
 */
public enum MessageSpecification {
    INITIALIZATION,
    REQUEST,
    DISCONNECT,
    STEP,
    GET_DICE_FROM_HEAP,
    FISH,
    EMPTY_HAND,
    SCORE,
    NEW_STATUS,
    KICK;
}
