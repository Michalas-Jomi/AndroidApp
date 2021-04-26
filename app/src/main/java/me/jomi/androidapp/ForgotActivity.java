package me.jomi.androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import me.jomi.androidapp.api.Api;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        button = findViewById(R.id.buttonForgot);
        button.setOnClickListener(this);

        email = findViewById(R.id.editTextEmail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonForgot:
                if(email.getText().toString().trim().isEmpty()) return;

                Api.auth.sendPasswordResetEmail(email.getText().toString().trim());

                break;
        }
    }

}
