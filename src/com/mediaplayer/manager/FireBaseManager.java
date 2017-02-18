package com.mediaplayer.manager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mediaplayer.models.Radio;

import java.util.List;

/**
 * Created by shrikanth on 2/18/17.
 */

public class FireBaseManager implements ChildEventListener, ValueEventListener{

    OnFireBaseEvents events;

    public FireBaseManager(OnFireBaseEvents events) {
        this.events = events;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Radio r = dataSnapshot.getValue(Radio.class);
        events.onNewRadioStatio(r);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Radio r = dataSnapshot.getValue(Radio.class);
        events.onRadioStationChanged(r);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Radio r = dataSnapshot.getValue(Radio.class);
        events.onRadioStationRemoved(r);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Radio r = dataSnapshot.getValue(Radio.class);
        events.onNewRadioStatio(r);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        events.onRadioCreateFailure(databaseError.toException().getMessage());
    }

    public interface OnFireBaseEvents{
        void onNewRadioStatio(Radio child);
        void onRadioStationRemoved(Radio child);
        void allRadioStations(List<Radio> children);
        void onRadioStationChanged(Radio child);
        void onRadioCreateFailure(String error);
    }
}
