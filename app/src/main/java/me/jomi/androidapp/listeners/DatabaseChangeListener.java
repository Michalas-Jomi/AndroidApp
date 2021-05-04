package me.jomi.androidapp.listeners;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class DatabaseChangeListener implements ValueEventListener {


    @Override
    public void onDataChange(DataSnapshot snapshot) {

        for(DataSnapshot data: snapshot.getChildren()){

        }
    }

    @Override
    public void onCancelled( DatabaseError error) {

    }
}
