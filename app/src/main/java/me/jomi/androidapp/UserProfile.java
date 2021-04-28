package me.jomi.androidapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.listeners.StepsListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {


    private Button buttonLogout;
    private StepsListener stepsListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        }
        stepsListener.registerStepSensor();
        buttonLogout = findViewById(R.id.buttonUserProfileLogout);
        buttonLogout.setOnClickListener(this);



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
        }
    }
}
