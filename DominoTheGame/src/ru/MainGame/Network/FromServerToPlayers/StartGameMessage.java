/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.FromServerToPlayers;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import ru.MainGame.Network.NumsOfDice;

/**
 * This special message that tell players about game started
 * @author svt
 */
@Serializable
public class StartGameMessage extends AbstractMessage{


    private Map<String,List<NumsOfDice> > startGamePart;
    
    private List<String> queueToSteps;
    public StartGameMessage() {
        startGamePart = new HashMap< >();
        queueToSteps = new ArrayList<>();
    }

    public void addNewEntry(String namePlayer,List<NumsOfDice> dices){
        startGamePart.put(namePlayer, dices);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.startGamePart);
        hash = 29 * hash + Objects.hashCode(this.queueToSteps);
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
        final StartGameMessage other = (StartGameMessage) obj;
        if (!Objects.equals(this.startGamePart, other.startGamePart)) {
            return false;
        }
        if (!Objects.equals(this.queueToSteps, other.queueToSteps)) {
            return false;
        }
        return true;
    }

    public Map<String, List<NumsOfDice>> getStartGamePart() {
        return startGamePart;
    }

    public void setStartGamePart(Map<String, List<NumsOfDice>> startGamePart) {
        this.startGamePart = startGamePart;
    }

    public List<String> getQueueToSteps() {
        return queueToSteps;
    }

    public void setQueueToSteps(List<String> queueToSteps) {
        this.queueToSteps = queueToSteps;
    }

    @Override
    public String toString() {
        return "StartGameMessage{" + "startGamePart=" + startGamePart + ",\n queueToSteps=" + queueToSteps + '}';
    }

   public List<NumsOfDice> getPartOf(String name){
       return startGamePart.get(name);
   }
}
