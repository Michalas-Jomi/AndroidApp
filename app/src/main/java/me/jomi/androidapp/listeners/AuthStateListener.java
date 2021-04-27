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
import com.google.firebase.database.ValueEventListener;
import me.jomi.androidapp.LoginActivity;
import me.jomi.androidapp.MainActivity;
import me.jomi.androidapp.UserProfile;
import me.jomi.androidapp.api.Api;

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
        }
        else
            MainActivity.instance.startActivity(new Intent(MainActivity.instance, LoginActivity.class));


    }
}
