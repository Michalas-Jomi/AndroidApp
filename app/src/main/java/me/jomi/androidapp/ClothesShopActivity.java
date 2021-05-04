package me.jomi.androidapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ClothesShopActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clothes_shop);

        linearLayout = findViewById(R.id.clothesShop_scrol);

        ImageView view = new ImageView(this.getApplicationContext());
        view.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.dress1));
        view.setLayoutParams(new ViewGroup.LayoutParams(263, 263));
        linearLayout.addView(view);

        ImageView testView = findViewById(R.id.test1);
        System.out.println("testView: " + testView.getLayoutParams().height);

    }
}
