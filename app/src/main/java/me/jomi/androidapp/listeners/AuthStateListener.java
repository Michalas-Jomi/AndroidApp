package me.jomi.androidapp.listeners;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import me.jomi.androidapp.LoginActivity;
import me.jomi.androidapp.MainActivity;
import me.jomi.androidapp.UserProfile;
import me.jomi.androidapp.api.Api;

import java.util.ArrayList;
import java.util.List;

public class AuthStateListener implements FirebaseAuth.AuthStateListener {
    @Override
    public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            Toast.makeText(MainActivity.instance, "Potwierdź adres e-mail aby kontynuować!", Toast.LENGTH_LONG).show();
            MainActivity.instance.startActivity(new Intent(MainActivity.instance, LoginActivity.class));
            return;
        }

        if (user != null && user.isEmailVerified()) {
            MainActivity.instance.startActivity(new Intent(MainActivity.instance, UserProfile.class));
            MainActivity.instance.finish(); // niszczy instancje, nie mozna juz do niej wrocic.

            Api.database.getReference().child("Users").child(Api.auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()) System.out.println("istnieje w bazie - nie tworzymy uzytkownika");
                        else System.out.println("nie istnieje w bazie - tworzymy dane usera");
                    }
                }
            });
        }
        else
            MainActivity.instance.startActivity(new Intent(MainActivity.instance, LoginActivity.class));


    }

    private void greetings(){
        Api.database.getReference().child("Users").child(Api.auth.getCurrentUser().getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.instance, "Hej " + task.getResult().getValue(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
