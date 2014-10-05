/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.Network.FromBothSides;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.Objects;
import ru.MainGame.Network.MessageSpecification;
import ru.MainGame.Network.StatusPlayer;

/**
 * message that send when sone diffferend things is happend
 * descripted by MessageSpecification
 * @author svt
 */
@Serializable
public class ExtendedSpecificationMessage extends AbstractMessage{
    private MessageSpecification specification;

    private String WhoSend;

    private StatusPlayer mStatus;

    private Object restrictedObject;

    public ExtendedSpecificationMessage() {
    }

    public MessageSpecification getSpecification() {
        return specification;
    }

    public String getWhoSend() {
        return WhoSend;
    }

    public StatusPlayer getStatusPlayer() {
        return mStatus;
    }

    public void setSpecification(MessageSpecification specification) {
        this.specification = specification;
    }

    public void setWhoSend(String WhoSend) {
        this.WhoSend = WhoSend;
    }

    public void setStatusPlayer(StatusPlayer data) {
        this.mStatus = data;
    }

    public Object getRestrictedObject() {
        return restrictedObject;
    }

    public void setRestrictedObject(Object restrictedObject) {
        this.restrictedObject = restrictedObject;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.specification);
        hash = 37 * hash + Objects.hashCode(this.WhoSend);
        hash = 37 * hash + Objects.hashCode(this.mStatus);
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
        final ExtendedSpecificationMessage other = (ExtendedSpecificationMessage) obj;
        if (this.specification != other.specification) {
            return false;
        }
        if (!Objects.equals(this.WhoSend, other.WhoSend)) {
            return false;
        }
        if (!Objects.equals(this.mStatus, other.mStatus)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ExtendedSpecificationMessage{" + "specification=" + specification + ", WhoSend=" + WhoSend + ", data=" + mStatus + '}';
    }
}