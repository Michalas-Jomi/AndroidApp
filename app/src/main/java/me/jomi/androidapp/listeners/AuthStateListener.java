package me.jomi.androidapp.listeners;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import me.jomi.androidapp.LoginActivity;
import me.jomi.androidapp.MainActivity;
import me.jomi.androidapp.ProfileActivity;
import me.jomi.androidapp.UserProfile;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.model.Clothes;
import me.jomi.androidapp.model.Location;
import me.jomi.androidapp.model.User;

public class AuthStateListener implements FirebaseAuth.AuthStateListener {

    public static DatabaseChangeListener databaseChangeListener;

    @Override
    public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            Toast.makeText(MainActivity.instance, "Potwierdź adres e-mail aby kontynuować!", Toast.LENGTH_LONG).show();
            MainActivity.instance.startActivity(new Intent(MainActivity.instance, LoginActivity.class));
            return;
        }

        if (user != null && user.isEmailVerified()) {


            Api.getUser().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if(task.isSuccessful()){

                        if(!task.getResult().exists())
                            Api.getUser().setValue(new User(0, 100, new Location((double) 0, (double) 0), new Clothes(0, 0)))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(Task<Void> task) {
                                            startMain();
                                        }
                                    });
                        else {
                            startMain();
                        }
                    }
                }
            });
        }
        else
            MainActivity.instance.startActivity(new Intent(MainActivity.instance, LoginActivity.class));
    }

    private void greetings(){
        Api.getUser().child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.instance, "Hej " + task.getResult().getValue(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startMain(){
        MainActivity.instance.startActivity(new Intent(MainActivity.instance, ProfileActivity.class));
        MainActivity.instance.finish(); // niszczy instancje, nie mozna juz do niej wrocic.
        if(databaseChangeListener == null){
            databaseChangeListener = new DatabaseChangeListener();
            databaseChangeListener.register();
        }
    }
}
