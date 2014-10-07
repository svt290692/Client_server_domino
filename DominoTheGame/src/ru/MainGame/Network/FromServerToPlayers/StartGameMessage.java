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
import java.util.Objects;
import ru.MainGame.Network.NumsOfDice;
import ru.MainGame.Network.Server.HostedPlayer;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.startGamePart);
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
        return true;
    }

    @Override
    public String toString() {
        return "StartGameMessage{" + "startGamePart=" + startGamePart + '}';
    }

    public void addNewEntry(HostedPlayer hostedPlayer, List<NumsOfDice> list) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
