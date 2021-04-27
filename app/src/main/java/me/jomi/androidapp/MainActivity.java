package me.jomi.androidapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.listeners.AuthStateListener;


public class MainActivity extends AppCompatActivity  {

    public static MainActivity instance;
    //TODO: sprawdzic czy sensor kroków działa i poprawnie zapisuje
    //TODO: real time location tracking (google firebase) https://www.youtube.com/watch?v=17HqLBkuX-E

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

    }
}