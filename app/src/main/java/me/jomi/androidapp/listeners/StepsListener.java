package me.jomi.androidapp.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import me.jomi.androidapp.MainActivity;

import java.util.List;

public class StepsListener implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor countSensor;
    private int steps = 0;
    private boolean registered = false;

    private double MagnitudePrevious = 0;
    private int sensorType;

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (sensorType) {
            case Sensor.TYPE_STEP_COUNTER:
                if(event.sensor == countSensor) {
                    steps = (int) event.values[0];
                }
                break;
            case Sensor.TYPE_ACCELEROMETER:
                if (event != null){
                    float x_acceleration = event.values[0];
                    float y_acceleration = event.values[1];
                    float z_acceleration = event.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if (MagnitudeDelta > 5){
                        steps++;
                    }
                    System.out.println(steps);
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    public void registerStepSensor(){
        if(registered) return;

        MainActivity mainActivity = MainActivity.instance;
        sensorManager = (SensorManager)  mainActivity.getSystemService(Context.SENSOR_SERVICE);



        // Zwykły
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null) {
            registered = true;
            sensorType = Sensor.TYPE_STEP_COUNTER;
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(mainActivity, "Sensor kroków wykryty", Toast.LENGTH_LONG).show();
            return;
        }

        // Akcelerometr
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            registered = true;
            sensorType = Sensor.TYPE_ACCELEROMETER;
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(mainActivity, "Akcelerometr wykryty", Toast.LENGTH_LONG).show();
            return;
        }

        // Brak sensora
        Toast.makeText(mainActivity, "brak sensora", Toast.LENGTH_LONG).show();
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
