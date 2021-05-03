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

public class UserProfile extends AppCompatActivity implements View.OnClickListener {


    private Button buttonLogout;
    private Button checkLocation;
    private Switch locSwitch;
    public static TextView locCoords;
    private static final int PERMISSION_LOCATION = 1001;
    private static final int PERMISSION_STEPS = 1000;
    public static UserActivity userActivity = UserActivity.BIKING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ActivityCompat.requestPermissions(this, new String[]{"com.google.android.gms.permission.ACTIVITY_RECOGNITION", Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_STEPS);
        //ActivityCompat.requestPermissions(this, new String[]{}, PERMISSION_STEPS);
        buttonLogout = findViewById(R.id.buttonUserProfileLogout);
        buttonLogout.setOnClickListener(this);
        checkLocation = findViewById(R.id.checkLocation);
        checkLocation.setOnClickListener(this);
        locSwitch = findViewById(R.id.simpleSwitch);
        locCoords = findViewById(R.id.locCoordText);
        locSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(locSwitch.isChecked()){
                    if (ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                        locSwitch.setChecked(false);
                        ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                    }
                    else {
                        locListener.registerListener();
                    }
                }
                else {
                    locListener.unregisterListener();
                }
            }
        });
        if(locListener.isEnabled()){
            locSwitch.setChecked(true);
        }
    }
//uzywa sie, gdy po prostu mamy permisje i tyle
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
            case R.id.buttonUserProfileLogout:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserProfile.this, "Pomyślnie zostałeś wylogowany", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.checkLocation:
                requestLocationPermissions();
                break;

        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
    }




}
