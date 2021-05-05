package me.jomi.androidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    SeekBar sfxBar;
    SeekBar musicBar;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        sfxBar       = findViewById(R.id.settings_sfx_SeekBar);
        musicBar     = findViewById(R.id.settings_music_SeekBar);
        logoutButton = findViewById(R.id.settings_logout_Button);

        logoutButton.setOnClickListener(this);
        sfxBar.setOnSeekBarChangeListener(this);
        musicBar.setOnSeekBarChangeListener(this);

        sfxBar.setProgress(Settings.getSfx());
        musicBar.setProgress(Settings.getMusic());
    }

    @Override
    public void onClick(View v) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(SettingsActivity.this, "Pomyślnie zostałeś wylogowany", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == sfxBar.getId())
            Settings.setSfx(progress);
        else if (seekBar.getId() == musicBar.getId())
            Settings.setMusic(progress);
        else
            System.err.println("Nieznany Seekbar w SettingsActivity: " + seekBar);
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
