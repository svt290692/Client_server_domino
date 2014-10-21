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

    private String message;
    
    private String WhoSend;

    private StatusPlayer mStatus;

    private Object restrictedObject;

    public ExtendedSpecificationMessage() {
    }

    public ExtendedSpecificationMessage(MessageSpecification specification,
            String WhoSend, StatusPlayer mStatus, Object restrictedObject) {
        this.specification = specification;
        this.WhoSend = WhoSend;
        this.mStatus = mStatus;
        this.restrictedObject = restrictedObject;
    }

    public ExtendedSpecificationMessage(MessageSpecification specification, String message, String WhoSend, StatusPlayer mStatus, Object restrictedObject) {
        this.specification = specification;
        this.message = message;
        this.WhoSend = WhoSend;
        this.mStatus = mStatus;
        this.restrictedObject = restrictedObject;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.specification);
        hash = 67 * hash + Objects.hashCode(this.WhoSend);
        hash = 67 * hash + Objects.hashCode(this.mStatus);
        hash = 67 * hash + Objects.hashCode(this.restrictedObject);
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
        if (this.mStatus != other.mStatus) {
            return false;
        }
        if (!Objects.equals(this.restrictedObject, other.restrictedObject)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ExtendedSpecificationMessage{" + "specification=" + specification + ", WhoSend=" + WhoSend + ", mStatus=" + mStatus + ", restrictedObject=" + restrictedObject + '}';
    }

}
