package me.jomi.androidapp;

import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import me.jomi.androidapp.model.CoinButton;

public class MainActivity extends AppCompatActivity {
//zle z baza

    private DatabaseManager databaseManager;
    public static MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = new DatabaseManager(this);
        instance = this;

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.ingame);
                ((TextView) findViewById(R.id.textView)).setText(databaseManager.getCoin() + "$");

                new CoinButton(findViewById(R.id.plus), 1);
                new CoinButton(findViewById(R.id.plustwo), 2);
                new CoinButton(findViewById(R.id.plusthree), 3);



            }
        });


    }



    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}