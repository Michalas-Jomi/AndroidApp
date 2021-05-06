package me.jomi.androidapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.games.GameRow;
import me.jomi.androidapp.listeners.DatabaseChangeListener;
import me.jomi.androidapp.listeners.StepsListener;
import me.jomi.androidapp.model.User;
import me.jomi.androidapp.model.UserActivity;
import org.jetbrains.annotations.NotNull;

import static me.jomi.androidapp.MainActivity.locListener;
import static me.jomi.androidapp.MainActivity.stepsListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {







    private static final int PERMISSION_LOCATION = 1001;
    private static final int PERMISSION_STEPS = 1000;
    public static UserActivity userActivity = UserActivity.NONE;
    private Switch cyclingSwitch;
    private Switch runningSwitch;
    private Switch footballSwitch;
    private TextView stepsCount;
    private TextView energyTextView;
    private CircularProgressBar steps_circularProgressBar;
    private ProgressBar progressBar;
    private User user;


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

        if(userActivity == UserActivity.CYCLING) cyclingSwitch.setChecked(true);
        if(userActivity == UserActivity.RUNNING) runningSwitch.setChecked(true);
        if(userActivity == UserActivity.FOOTBALL) footballSwitch.setChecked(true);


        steps_circularProgressBar = findViewById(R.id.user_steps_circularProgressBar);
        steps_circularProgressBar.setProgressMax(5000);
        steps_circularProgressBar.setProgress(StepsListener.steps > 5000 ? 5000 : StepsListener.steps);
        stepsCount = findViewById(R.id.user_steps_count);
        stepsCount.setText(String.valueOf(StepsListener.steps));
        energyTextView = findViewById(R.id.user_energy_textview);
        progressBar = findViewById(R.id.user_horizontalbar_progressbar);
        DatabaseChangeListener.ENERGY .register(this.getClass(), new Consumer<Float>()   { public void accept(Float energy)    { refreshEnergy(energy); }});

        refresh();
    }

    public void refresh(){
        Api.getUser().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    user = (task.getResult().getValue(User.class));
                    energyTextView.setText("Posiadana energia: " +((int) user.getEnergy()) + "%");
                    progressBar.setProgress((int) user.getEnergy());


                }
            }
        });
    }

    public void refreshEnergy(float energy) {
        progressBar.setProgress((int) energy);
        energyTextView.setText("Posiadana energia: " +((int) energy) + "%");
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
