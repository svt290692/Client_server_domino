/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.FromServerToPlayers;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.MainGame.Network.NumsOfDice;

/**
 *
 * @author svt
 */
@Serializable
public class StartGameMessage extends AbstractMessage{
    

    private Map<String,List<NumsOfDice> > startGamePart;
    public StartGameMessage() {
        startGamePart = new HashMap< >();
    }
    
    public void addNewEntry(String namePlayer,List<NumsOfDice> dices){
        startGamePart.put(namePlayer, dices);
    }

    public Map<String, List<NumsOfDice>> getStartGamePart() {
        return startGamePart;
    }
    
    public List<NumsOfDice> getPartOf(String name){
        return startGamePart.get(name);
    }
}
