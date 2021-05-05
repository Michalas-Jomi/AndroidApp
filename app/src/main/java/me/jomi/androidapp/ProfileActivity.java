package me.jomi.androidapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.games.Game;
import me.jomi.androidapp.games.GameRow;
import me.jomi.androidapp.listeners.DatabaseChangeListener;
import me.jomi.androidapp.model.Clothes;
import me.jomi.androidapp.model.User;
import me.jomi.androidapp.util.ViewUtils;
import me.jomi.androidapp.util.ViewUtils.Connector;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    // Info
    ConstraintLayout mainLayout;
    TextView moneyView;
    TextView energyView;
    ProgressBar energyBar;

    // Hero
    ImageView heroView;
    ConstraintLayout heroLayout;

    // Buttons
    ImageView shopView;
    ImageView gamesView;
    ImageView realActivityView;

    // Games
    ConstraintLayout gamesMapLayout;
    ImageView gamesMapView;
    List<GameRow> gameRowList = new ArrayList<>();

    // Settings
    ImageView settingsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        Settings.load();

        moneyView  = findViewById(R.id.profile_money_TextView);
        energyView = findViewById(R.id.profile_energy_TextView);
        energyBar  = findViewById(R.id.profile_energy_ProgressBar);
        heroView   = findViewById(R.id.profile_hero_ImageView);
        heroLayout = findViewById(R.id.profile_hero_layout);
        mainLayout = findViewById(R.id.profile_mainLayout);

        shopView         = findViewById(R.id.profile_shop_ImageView);
        gamesView        = findViewById(R.id.profile_games_ImageView);
        realActivityView = findViewById(R.id.profile_realActivity_ImageView);

        gamesMapLayout = findViewById(R.id.profile_games_mapLayout);
        gamesMapView   = findViewById(R.id.profile_games_map_ImageView);

        settingsView = findViewById(R.id.profile_settings_ImageView);


        shopView.setOnClickListener(this);
        gamesView.setOnClickListener(this);
        settingsView.setOnClickListener(this);
        realActivityView.setOnClickListener(this);

        DatabaseChangeListener.CLOTHES.register(this.getClass(), new Consumer<Clothes>() { public void accept(Clothes clothes) { dressUp(clothes);      }});
        DatabaseChangeListener.ENERGY .register(this.getClass(), new Consumer<Float>()   { public void accept(Float energy)    { refreshEnergy(energy); }});
        DatabaseChangeListener.MONEY  .register(this.getClass(), new Consumer<Integer>() { public void accept(Integer money)   { refreshMoney(money);   }});



        gameRowList.clear();
        gamesMapLayout.removeAllViews();
        gamesMapLayout.addView(gamesMapView);
        Connector.create(gamesMapLayout)
                .connectWithParent(gamesMapView, Connector.TOP)
                .connectWithParent(gamesMapView, Connector.BOTTOM)
                .connectWithParent(gamesMapView, Connector.LEFT)
                .connectWithParent(gamesMapView, Connector.RIGHT)
                .finish();
        for (Game game : Game.values())
            gameRowList.add(new GameRow(this, game, gamesMapLayout));


        refreshAll();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == shopView.getId()) {
            startActivity(new Intent(this, ClothesShopActivity.class));
        } else if (v.getId() == gamesView.getId()) {
            final boolean visible = gamesMapLayout.getVisibility() == ScrollView.VISIBLE;
            gamesMapLayout.animate()
                .alpha(visible ? 0 : 1)
                .setDuration(750)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (!visible)
                            gamesMapLayout.setVisibility(ScrollView.VISIBLE);
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (visible)
                            gamesMapLayout.setVisibility(ScrollView.INVISIBLE);
                    }
                });
        } else if (v.getId() == realActivityView.getId()) {
            startActivity(new Intent(this, UserProfile.class));
        } else if (v.getId() == settingsView.getId()) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else {
            System.err.println("Nieznane View w ProfileActivity " + v);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Settings.needSave)
            Settings.saveNow();
    }

    public void refreshAll() {
        Api.getUser().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    System.out.println(task.getResult().getValue());
                    System.out.println(task.getResult().getValue().getClass());
                    refreshAll(task.getResult().getValue(User.class));
                }
            }
        });
    }
    private void refreshAll(User user) {
        refreshMoney(user.getMoney());
        refreshEnergy(user.getEnergy());
        dressUp(user.getClothes());
    }
    public void refreshMoney(int money) {
        moneyView.setText("Monety: " + money);
    }
    public void refreshEnergy(float energy) {
        energyBar.setProgress((int) energy);
        energyView.setText(String.valueOf((int) energy));

        for (GameRow gameRow : gameRowList)
            gameRow.setUnlocked(energy >= gameRow.game.energy);

    }


    public void dressUp(Clothes clothes) {
        ViewUtils.dressUpHero(this, heroLayout, heroView, clothes);
    }

}
