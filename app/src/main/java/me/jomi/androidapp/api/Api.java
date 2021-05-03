package me.jomi.androidapp.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Api {

    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static FirebaseDatabase database = FirebaseDatabase.getInstance("https://healthyapp-2a503-default-rtdb.europe-west1.firebasedatabase.app/");


    public static DatabaseReference getUser() {
        return Api.database.getReference().child("users").child(Api.auth.getCurrentUser().getUid());
    }
}
