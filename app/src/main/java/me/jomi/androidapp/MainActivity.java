package me.jomi.androidapp;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.listeners.AuthStateListener;
import me.jomi.androidapp.listeners.LocListener;
import me.jomi.androidapp.listeners.StepsListener;


public class MainActivity extends AppCompatActivity  {

    public static MainActivity instance;
    public static LocListener locListener;
    public static StepsListener stepsListener;
    public static LocationManager locationManager;

    //TODO: sprawdzic czy sensor kroków działa i poprawnie zapisuje
    //TODO: real time location tracking (google firebase) https://www.youtube.com/watch?v=17HqLBkuX-E
    //TODO: wybiera sie forme jaka sie robi i liczy, czyli np jezdzenie rowerem. I od tego momentu database listener bedzie nasluchiwac na
    // zmiany w lokacji i robic konkretne obliczenia ile sie przejechalo od momentu wlaczenia.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        if(Api.auth.getCurrentUser() != null) startActivity(new Intent(this, UserProfile.class));
        else startActivity(new Intent(this, LoginActivity.class));
 */
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
        Api.auth.addAuthStateListener(new AuthStateListener());
        locListener = new LocListener();
        stepsListener = new StepsListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


/*
        Api.database.getReference().child("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for(DataSnapshot data : task.getResult().getChildren()) {
                        System.out.println(data.getKey());
                    }

                }
            }
        });

 */

    }
}