package me.jomi.androidapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.util.Checker;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    public static MainActivity instance;
    private TextView register;
    private Button buttonLogin;
    private EditText editNick;
    private EditText editPassword;
    private TextView remind;


    //TODO: sprawdzic czy sensor kroków działa i poprawnie zapisuje
    //TODO: real time location tracking (google firebase) https://www.youtube.com/watch?v=17HqLBkuX-E

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);

        register = (TextView) findViewById(R.id.textViewRegister);
        register.setOnClickListener(this);

        remind = findViewById(R.id.textViewRemind);
        remind.setOnClickListener(this);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        editNick = (EditText) findViewById(R.id.editNick);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    //TODO: poprawić gdy krokow jest 0 to update nie dziala, a insert tak, a gdy jest powyzej 0 to insert nie dziala a update dziala xD
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textViewRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.textViewRemind:
                startActivity(new Intent(this, ForgotActivity.class));
                break;
            case R.id.buttonLogin:
                loginUser();
                break;
        }
    }
    private void loginUser(){
        String email = editNick.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        try {
            Checker.checkEditText(!email.isEmpty(),                                 editNick,    "email jest wymagany");
            Checker.checkEditText(Patterns.EMAIL_ADDRESS.matcher(email).matches(),  editNick,    "Wpisz poprawny email");
            Checker.checkEditText(!password.isEmpty(),                              editPassword,"Haslo jest wymagane");
        } catch (IllegalArgumentException e) {
            return;
        }


        Api.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = Api.auth.getCurrentUser();
                    if(user.isEmailVerified()) {
                        Toast.makeText(MainActivity.this, "Udalo sie zalogowac!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, UserProfile.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Twoje konto nie zostalo jeszcze zweryfikowane, wyslano adres e-mail!", Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(MainActivity.this, "Nie udalo sie zalogowac, sprawdz dane!", Toast.LENGTH_LONG).show();
            }
        });
    }
}