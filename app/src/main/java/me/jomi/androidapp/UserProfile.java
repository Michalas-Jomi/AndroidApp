package me.jomi.androidapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import me.jomi.androidapp.model.UserActivity;

import static me.jomi.androidapp.MainActivity.locListener;
import static me.jomi.androidapp.MainActivity.stepsListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {





    private static final int PERMISSION_LOCATION = 1001;
    private static final int PERMISSION_STEPS = 1000;
    public static UserActivity userActivity = UserActivity.NONE;
    private Switch cyclingSwitch;
    private Switch runningSwitch;
    private Switch footballSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        ActivityCompat.requestPermissions(this, new String[]{"com.google.android.gms.permission.ACTIVITY_RECOGNITION", Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_STEPS);
        //ActivityCompat.requestPermissions(this, new String[]{}, PERMISSION_STEPS);


        cyclingSwitch = findViewById(R.id.user_cycling_switch);
        cyclingSwitch.setOnCheckedChangeListener(this);
        runningSwitch = findViewById(R.id.user_running_switch);
        runningSwitch.setOnCheckedChangeListener(this);
        footballSwitch = findViewById(R.id.user_football_switch);
        footballSwitch.setOnCheckedChangeListener(this);
        cyclingSwitch.setChecked(false);
        runningSwitch.setChecked(false);
        footballSwitch.setChecked(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == PERMISSION_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Wykryto permisje do location listenera", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Brak permisji do location listenera!", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == PERMISSION_STEPS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Wykryto permisje do Steps listener", Toast.LENGTH_LONG).show();
                stepsListener.registerStepSensor();
            }
            else Toast.makeText(this, "Brak permisji do step listenera!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!canActivity()) return;
        // ustawia tak jakby aktualny stan wszystkich byl bez uwzglednienia aktualnej zmiany, zeby bramka mogla przejsc poprawnie.
         if(buttonView.isChecked()) buttonView.setChecked(false);
         else buttonView.setChecked(true);

        if(cyclingSwitch.isChecked() || runningSwitch.isChecked() || footballSwitch.isChecked()){

            if(buttonView.isChecked())
                userActivity = UserActivity.NONE;

            buttonView.setChecked(false);
            return;
        }

            buttonView.setChecked(true);
            switch (buttonView.getId()) {
                case R.id.user_cycling_switch:
                    userActivity = UserActivity.CYCLING;
                    break;
                case R.id.user_football_switch:
                    userActivity = UserActivity.FOOTBALL;
                    break;
                case R.id.user_running_switch:
                    userActivity = UserActivity.RUNNING;
                    break;


        }
    }

   private boolean canActivity(){
        boolean canCheckOn = false;

       if (ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
           ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);

       } else {
           locListener.registerListener();
           canCheckOn = true;

       }

       return canCheckOn;
   }
}
