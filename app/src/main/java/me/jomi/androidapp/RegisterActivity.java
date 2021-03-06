package me.jomi.androidapp;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.model.User;
import me.jomi.androidapp.util.Checker;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName = (EditText) findViewById(R.id.editTextFullName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister2);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bannerOtherKey:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.buttonRegister2:
                registerUser();
                break;
        }
    }


    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String age = editTextAge.getText().toString().trim();

        try {
            Checker.checkEditText(!(email.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty()),  editTextName,       "Musisz wype??ni?? wszystkie kolumny!");
            Checker.checkEditText(Patterns.EMAIL_ADDRESS.matcher(email).matches(),                              editTextEmail,      "Niepoprawny adres email");
            Checker.checkEditText(password.length() >= 6,                                                   editTextPassword,   "Has??o musi zawiera?? conajmniej 6 znak??w");
            Checker.checkEditText(Pattern.compile("\\d+").matcher(age).matches(),                               editTextAge,        "Wiek musi by?? poprawn?? liczb??");
        } catch (IllegalArgumentException e) {
            return;
        }

        Api.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Zarejestrowano pomyslnie, aby konto by??o aktywne potwierdz adres e-mail!", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                } else
                    Toast.makeText(RegisterActivity.this, "Wyst??pi?? problem podczas rejesteracji", Toast.LENGTH_LONG).show();
            }
        });
    }

}
