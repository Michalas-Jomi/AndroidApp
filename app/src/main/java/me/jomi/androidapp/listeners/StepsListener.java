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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == countSensor) {
            steps = (int) event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerStepSensor(){
       MainActivity mainActivity = MainActivity.instance;
        sensorManager = (SensorManager)  mainActivity.getSystemService(Context.SENSOR_SERVICE);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(new StepsListener(), countSensor, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(mainActivity, "Sensor kroków wykryty", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(mainActivity, "brak sensora", Toast.LENGTH_LONG).show();
        }
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public int getCurrentSteps() {
        return steps;
    }
}
