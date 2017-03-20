package com.mediaplayer.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shrikanth on 3/19/17.
 */

public class ModeManager {

    private static ModeManager INSTANCE;
    public enum  Mode{
        STREAMING, REMOTE_PLAY, LOCAL_PLAY
    }

    Mode currentMode;
    List<ModeChangeListener> modeChangeListeners;

    private ModeManager(){
        currentMode = Mode.LOCAL_PLAY;
        modeChangeListeners = new ArrayList<>();
    }

    public static ModeManager getInstance(){
        if(INSTANCE == null) INSTANCE = new ModeManager();
        return INSTANCE;
    }

    public interface ModeChangeListener {
        void onModeChange(Mode currentMode);
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
        notifyModeChange();
    }

    public void addModeChangeListener(ModeChangeListener listener){
        modeChangeListeners.add(listener);
    }

    public void removeModeChangeListener(ModeChangeListener listener){
        modeChangeListeners.remove(listener);
    }

    private void notifyModeChange(){
        Iterator<ModeChangeListener> listenerIterator = modeChangeListeners.iterator();
        while (listenerIterator.hasNext()){
            ModeChangeListener listener = listenerIterator.next();
            listener.onModeChange(currentMode);
        }
    }
}
