package me.jomi.androidapp.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import me.jomi.androidapp.MainActivity;

public class StepsListener implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor countSensor;
    private int steps = 0;
    private boolean registered = false;

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println("krok 0: " + event.values[0]);
        if(event.sensor == countSensor) {
            System.out.println("krok 1: " + event.values[0]);
            steps = (int) event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerStepSensor(){
        if(registered) return;

       MainActivity mainActivity = MainActivity.instance;
        sensorManager = (SensorManager)  mainActivity.getSystemService(Context.SENSOR_SERVICE);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            registered = true;
            Toast.makeText(mainActivity, "Sensor kroków wykryty", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(mainActivity, "brak sensora", Toast.LENGTH_LONG).show();
        }

    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public int getSessionSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void unregisterListener(){
        sensorManager.unregisterListener(this);
        registered = false;
    }
}
