package me.jomi.androidapp.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import me.jomi.androidapp.MainActivity;
import me.jomi.androidapp.util.Energy;

import java.util.List;

public class StepsListener implements SensorEventListener {

    private SensorManager sensorManager;
    public static int steps = 0;
    private boolean registered = false;

    private double MagnitudePrevious = 0;
    private int sensorType;

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (sensorType) {
            case Sensor.TYPE_STEP_COUNTER:
                steps = (int) event.values[0];
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
                        if(steps%100 == 0 && steps != 0) Energy.addEnergy(steps * 0.001f);
                    }
              //      System.out.println(steps);
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

        if (registerStepSensor(Sensor.TYPE_STEP_COUNTER,  SensorManager.SENSOR_DELAY_UI,     "Sensor krok??w wykryty")  != null) return; // Zwyk??y
        if (registerStepSensor(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_NORMAL, "Akcelerometr wykryty")   != null) return; // Akcelerometr
        Toast.makeText(mainActivity, "brak sensora", Toast.LENGTH_LONG).show(); // Brak sensora
    }
    private Sensor registerStepSensor(int type, int samplingPeriodUs, String msg) {
        Sensor sensor = sensorManager.getDefaultSensor(type);
        if (sensor != null) {
            registered = true;
            sensorType = type;
            sensorManager.registerListener(this, sensor, samplingPeriodUs);
            Toast.makeText(MainActivity.instance, msg, Toast.LENGTH_LONG).show();
        }
        return sensor;
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
