package me.jomi.androidapp;

import android.os.Bundle;
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
import me.jomi.androidapp.listeners.DatabaseChangeListener;
import me.jomi.androidapp.model.Clothes;
import me.jomi.androidapp.model.User;
import me.jomi.androidapp.util.Accessibler;
import me.jomi.androidapp.util.ViewUtils;
import me.jomi.androidapp.util.ViewUtils.Connector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static me.jomi.androidapp.util.ViewUtils.dp;

public class ClothesShopActivity extends AppCompatActivity {
    public static class Prices {
        public static int[] cap = {20, 40, 75};
        public static int[] shirt = {100};
        public static int[] pants = {80, 250};
        public static int[] dress = {1000};
    }
    public class ClickListener implements View.OnClickListener {
        private final String category;
        private final TextView textView;
        private final int id;
        private final boolean bypass;

        public ClickListener(String category, int id, TextView textView) {
            this.category = category;
            this.textView = textView;
            this.bypass = false;
            this.id = id;
        }
        public ClickListener(String category, int id) {
            // None
            this.category = category;
            this.textView = null;
            this.bypass = true;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            if (bypass) {
                Api.getUser().child("clothes").child(category).setValue(id);
                Toast.makeText(ClothesShopActivity.this, "Zdjęto", Toast.LENGTH_SHORT).show();
                return;
            }
            Api.getUser().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    User user = task.getResult().getValue(User.class);
                    try {
                        Field boughtField = user.getClothes().getClass().getDeclaredField("bought_" + category);
                        boughtField.setAccessible(true);
                        List<Integer> bought = (List<Integer>) boughtField.get(user.getClothes());
                        if (bought == null) bought = new ArrayList<>();
                        if (bought.contains(id)) {
                            Api.getUser().child("clothes").child(category).setValue(id);
                            Toast.makeText(ClothesShopActivity.this, "Ubrano", Toast.LENGTH_SHORT).show();
                        } else {
                            int price = ((int[]) Prices.class.getDeclaredField(category).get(null))[id - 1];
                            if (user.getMoney() < price)
                                Toast.makeText(ClothesShopActivity.this, "Nie stać cię na to", Toast.LENGTH_SHORT).show();
                            else {
                                bought.add(id);
                                Api.getUser().child("money").setValue(user.getMoney() - price);
                                Api.getUser().child("clothes").child(category).setValue(id);
                                Api.getUser().child("clothes").child("bought_" + category).setValue(bought);
                                Toast.makeText(ClothesShopActivity.this, "Zakupiono nowy przedmiot", Toast.LENGTH_SHORT).show();
                                textView.setText("Zakupiono");
                            }
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    // Shop
    ConstraintLayout mainLayout;
    LinearLayout linearLayout;
    ScrollView scrollView;
    TextView moneyView;

    // Hero
    ConstraintLayout heroLayout;
    ImageView heroView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clothes_shop);

        mainLayout   = findViewById(R.id.clothesShop_mainLayout);
        scrollView   = findViewById(R.id.clothesShop_ScrollView);
        linearLayout = findViewById(R.id.clothesShop_scroll);
        moneyView    = findViewById(R.id.clothesShop_money);

        heroLayout = findViewById(R.id.clothesShop_heroLayout);
        heroView   = findViewById(R.id.clothesShop_hero_ImageView);

        Api.getUser().child("clothes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Clothes clothes = task.getResult().getValue(Clothes.class);
                    createIcons(clothes);
                    dressUpHero(clothes);
                } else
                    ViewUtils.toast(ClothesShopActivity.this, "Nie udało się otworzyć sklepu");
            }
        });
        Api.getUser().child("money").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                    refreshMoney(task.getResult().getValue(int.class));
            }
        });
        DatabaseChangeListener.MONEY  .register(this.getClass(), new Consumer<Integer>() { public void accept(Integer money)   { refreshMoney(money);  }});
        DatabaseChangeListener.CLOTHES.register(this.getClass(), new Consumer<Clothes>() { public void accept(Clothes clothes) { dressUpHero(clothes); }});
    }

    public void refreshMoney(int money) {
        moneyView.setText(String.valueOf(money));
    }

    private void createIcons(Clothes clothes) {
        for (Field field : Prices.class.getDeclaredFields()) {
            String category = field.getName();

            LinearLayout linearLayout = createScroll();

            List<Integer> bought = Accessibler.get(clothes, "bought_" + category);
            if (bought == null) bought = new ArrayList<>();
            int[] prices = Accessibler.getStatic(Prices.class, category);


            ImageView noneView = ViewUtils.prepareToConstraintSet(ViewUtils.createImage(this, R.drawable.none), linearLayout, 150, 150);;
            noneView.setOnClickListener(new ClickListener(category, 0));

            for (int i = 1;; i++) {
                int id;
                try {
                    id = Accessibler.getStatic(R.drawable.class, category + i);
                } catch (Throwable e) {
                    break;
                }

                ConstraintLayout layout = new ConstraintLayout(this);
                layout.setMaxWidth(dp(150)); layout.setMaxHeight(dp(170));
                layout.setMinWidth(dp(150)); layout.setMinHeight(dp(170));
                linearLayout.addView(layout);

                ImageView imageView = ViewUtils.prepareToConstraintSet(ViewUtils.createImage(this, id), layout, 150, 150);

                ImageView coinView = ViewUtils.prepareToConstraintSet(ViewUtils.createImage(this, R.drawable.moneta), layout, 20, 20);

                TextView textView = ViewUtils.prepareToConstraintSet(new TextView(this), layout, 130, 20);
                textView.setText(bought.contains(i) ? "Zakupiono" : String.valueOf(prices[i - 1]));

                imageView.setOnClickListener(new ClickListener(category, i, textView));

                Connector.create(layout)
                        .connect(coinView, Connector.TOP, imageView)
                        .connect(textView, Connector.TOP, imageView)
                        .connect(textView, Connector.LEFT, coinView)
                        .finish();
            }
        }
    }


    public void dressUpHero(Clothes clothes) {
        ViewUtils.dressUpHero(this, heroLayout, heroView, clothes);
    }


    /**
     * Tworzy HorizontalScrollView, dodaje go do głównego ScrollView i zwraca swój LinearLayout
     * @return LinearLayout wewnątrz nowoutworzonego HorizontalScrollView
     */
    private LinearLayout createScroll() {
        HorizontalScrollView scroll = new HorizontalScrollView(this);
        this.linearLayout.addView(scroll);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(linearLayout);

        return linearLayout;
    }
}
