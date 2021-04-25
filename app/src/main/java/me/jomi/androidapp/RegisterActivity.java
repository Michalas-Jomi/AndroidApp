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

        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty()){
            editTextName.setError("Musisz wypełnić wszystkie kolumny!");
            editTextName.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Zly format adresu e-mail!");
            editTextEmail.requestFocus();
            return;
        }
        Api.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(name, age, email);


                    Api.database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Zarejestrowano pomyslnie, aby konto było aktywne potwierdz adres e-mail!", Toast.LENGTH_LONG).show();
                                FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
                                firebaseUser.sendEmailVerification();

                            }
                            else Toast.makeText(RegisterActivity.this, "Nie zarejestrowano, wystapil problem", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

    }

    public void test(){

    }
}
