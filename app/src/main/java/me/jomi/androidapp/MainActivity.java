package me.jomi.androidapp;

import android.Manifest;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import me.jomi.androidapp.listeners.StepsListener;

public class MainActivity extends AppCompatActivity  {

    private DatabaseManager databaseManager;
    public static MainActivity instance;
    StepsListener stepsListener;

    //TODO: sprawdzic czy sensor kroków działa i poprawnie zapisuje
    //TODO: real time location tracking (google firebase) https://www.youtube.com/watch?v=17HqLBkuX-E

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        stepsListener = new StepsListener();
        stepsListener.registerStepSensor();
        databaseManager = new DatabaseManager(this);
    }

    //TODO: poprawić gdy krokow jest 0 to update nie dziala, a insert tak, a gdy jest powyzej 0 to insert nie dziala a update dziala xD
    @Override
    protected void onStop() {
        super.onStop();
        databaseManager.updateSteps(stepsListener.getCurrentSteps());

    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }



}