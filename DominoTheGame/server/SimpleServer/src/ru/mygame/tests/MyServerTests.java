/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.mygame.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import ru.MainGame.Network.FromServerToPlayers.StartGameMessage;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.Server.HostedPlayer;

/**
 *
 * @author svt
 */
public class MyServerTests {
    public static void main(String argsp[]){
	createStartGameMessage();
    }
    public static StartGameMessage createStartGameMessage(){
        List<NumsOfDice> dices = new ArrayList<>();
	for(int i=0,j=0,k=0;i <= 27 ;i++){
            dices.add(new NumsOfDice(j, k));
	    System.out.print("<" + j + "> <" +k+ ">_");
            if(j == 6){
                k++;
		j = k;
            }else j++;
        }

        Collections.sort(dices, new Comparator<Object>() {

                Random rand = new Random();
            @Override
            public int compare(Object o1, Object o2) {
                return (1 - rand.nextInt(3));
            }
        });
        StartGameMessage message = new StartGameMessage();

        int count = 0;
	List<List<NumsOfDice> > listOfLists = new ArrayList<>();
        for(int i = 0 ; i < 4;i++){
            List<NumsOfDice> list = new ArrayList<>();
	    for(int j = 0 ; j < 6; j++){
		list.add(dices.get(count++));
	    }
            listOfLists.add(list);
        }
        return message;
    }
}
