/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame.TableHanding;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author svt
 */
public class AnimationEventCounter {
    
    private AtomicInteger countOfAnimationEventsInTable = new AtomicInteger(0);
    
    public void captureAnimationSlot(){
        countOfAnimationEventsInTable.incrementAndGet();
    }
    
    public void releaseAnimationSlot(){
        countOfAnimationEventsInTable.decrementAndGet();
    }
    
    public boolean isAnimationInTableExists(){
        return countOfAnimationEventsInTable.get() != 0;
    }
    
    public int getCountOfAnimationInTable(){
        return countOfAnimationEventsInTable.get();
    }
    
    private AnimationEventCounter() {
    }
    
    public synchronized static AnimationEventCounter getInstance() {
        return AnimationEventHandlerHolder.INSTANCE;
    }
    
    private static class AnimationEventHandlerHolder {

        private static final AnimationEventCounter INSTANCE = new AnimationEventCounter();
    }
}
