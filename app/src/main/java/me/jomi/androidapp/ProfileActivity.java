package me.jomi.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.model.Clothes;
import me.jomi.androidapp.model.User;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    // Info
    TextView moneyView;
    TextView energyView;

    // Hero
    ImageView heroView;

    // Clothes
    ImageView dressView;
    ImageView pantsView;

    // Buttons
    ImageView shopView;
    ImageView gamesView;
    ImageView realActivityView;


    static ProfileActivity instance;
    public static ProfileActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        setContentView(R.layout.activity_profile);

        moneyView  = findViewById(R.id.profile_money_TextView);
        energyView = findViewById(R.id.profile_energy_TextView);
        heroView   = findViewById(R.id.profile_hero_ImageView);
        dressView  = findViewById(R.id.profile_cloth_dress_ImageView);
        pantsView  = findViewById(R.id.profile_cloth_pants_ImageView);

        shopView         = findViewById(R.id.profile_shop_ImageView);
        gamesView        = findViewById(R.id.profile_games_ImageView);
        realActivityView = findViewById(R.id.profile_realActivity_ImageView);

        shopView.setOnClickListener(this);
        gamesView.setOnClickListener(this);
        realActivityView.setOnClickListener(this);

        refreshAll();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == shopView.getId()) {
            startActivity(new Intent(this, ClothesShopActivity.class));
        } else if (v.getId() == gamesView.getId()) {
            // TODO games
        } else if (v.getId() == realActivityView.getId()) {
            startActivity(new Intent(this, UserProfile.class));
        } else {
            System.err.println("Nieznane View w ProfileActivity " + v);
        }
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
    public void refreshMoney() {
        refresh(moneyView, "Monety", "money", int.class);
    }
    public void refreshMoney(int money) {
        refresh(moneyView, "Monety", money);
    }
    public void refreshEnergy() {
        refresh(energyView, "Energia", "energy", float.class);
    }
    public void refreshEnergy(float energy) {
        refresh(energyView, "Energia", energy);
    }
    private void refresh(final TextView view, final String prefix, String child, final Class<?> clazz) {
        Api.getUser().child(child).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                    refresh(view, prefix, task.getResult().getValue(clazz));
            }
        });
    }
    private void refresh(TextView view, String prefix, Object value) {
        view.setText(prefix + ": " + value);
    }


    /**
     * Ubiera bohatera w ubrania znelezione w bazie
     */
    public void dressUp() {
        Api.getUser().child("clothes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Clothes clothes = task.getResult().getValue(Clothes.class);
                    dressUp(clothes);
                }
            }
        });
    }

    /**
     * Ubiera bohatera w podane ubrania
     *
     * @param clothes ubrania w które ma się ubrać
     */
    public void dressUp(Clothes clothes) {
        dressUp(pantsView, "pants", clothes.getPants());
        dressUp(dressView, "dress", clothes.getDress());
    }
    /**
     * Ustawia odpowidni obrazek (z /res/drawable wybiera ten o nazwie prefix + version) </br>
     * jeśli {@code version == 0} ukrywa view
     *
     * @param view na którym będzie zakładane ubranie
     * @param prefix nazwy w /res/drawable
     * @param version numer dodany po nazwie (bez spacji) w /res/drawable
     */
    private void dressUp(ImageView view, String prefix, int version) {
        if (version == 0)
            view.setVisibility(ImageView.INVISIBLE);
        else {
            int id = -1;
            try {
                id = (int) R.drawable.class.getDeclaredField(prefix + version).get(null);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            view.setImageDrawable(ActivityCompat.getDrawable(this, id));
            view.setVisibility(ImageView.VISIBLE);
        }
    }
}
