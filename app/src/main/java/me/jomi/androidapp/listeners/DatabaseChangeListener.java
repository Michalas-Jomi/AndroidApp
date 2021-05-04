package me.jomi.androidapp.listeners;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import me.jomi.androidapp.ProfileActivity;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.model.Clothes;


public class DatabaseChangeListener implements ValueEventListener {


    @Override
    public void onDataChange(DataSnapshot snapshot) {
        switch (snapshot.getKey()) {
            case "clothes":
                ProfileActivity.getInstance().dressUp(snapshot.getValue(Clothes.class));
                break;
            case "energy":
                ProfileActivity.getInstance().refreshEnergy(snapshot.getValue(float.class));
                break;
            case "money":
                ProfileActivity.getInstance().refreshMoney(snapshot.getValue(int.class));
                break;
        }
    }

    @Override
    public void onCancelled(DatabaseError error) {

    }

    public void register() {
        Api.getUser().child("clothes").addValueEventListener(this);
        Api.getUser().child("energy").addValueEventListener(this);
        Api.getUser().child("money").addValueEventListener(this);
    }
}
