package me.jomi.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import me.jomi.androidapp.api.Api;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView register;
    private Button buttonLogin;
    private EditText editNick;
    private EditText editPassword;
    private TextView remind;
    private SignInButton google_signIn;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar progressBar;
    private final static int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.textViewRegister);
        register.setOnClickListener(this);

        remind = findViewById(R.id.textViewRemind);
        remind.setOnClickListener(this);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        editNick = (EditText) findViewById(R.id.editNick);
        editPassword = (EditText) findViewById(R.id.editPassword);

        google_signIn = findViewById(R.id.google_signIn);
        google_signIn.setOnClickListener(this);
        createGoogleRequest();






    }

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
            case R.id.google_signIn:
                googleSignIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //   Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Wystąpił problem podczas logowania", Toast.LENGTH_LONG).show();
                // Google Sign In failed, update UI appropriately
                //    Log.w(TAG, "Google sign in failed", e);
            }
        }

    }

    public void createGoogleRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Api.auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Zalogowano!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wystąpił problem podczas logowania", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void loginUser(){
        String email = editNick.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(email.isEmpty()){
            editNick.setError("Email jest wymagany!");
            editNick.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editNick.setError("Wpisz poprawny email!");
            editNick.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("Hasło jest wymagane!");
            editPassword.requestFocus();
            return;
        }

        Api.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Zalogowano!", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(LoginActivity.this, "Nie udalo sie zalogowac, sprawdz dane!", Toast.LENGTH_LONG).show();

            }
        });
    }

}
