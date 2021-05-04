package me.jomi.androidapp;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.model.Clothes;
import me.jomi.androidapp.model.User;
import me.jomi.androidapp.util.Accessibler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClothesShopActivity extends AppCompatActivity {
    public static class Prices {
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

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clothes_shop);

        linearLayout = findViewById(R.id.clothesShop_scrol);

        Api.getUser().child("clothes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)//TODO pozbyć się RequiresApi
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    createIcons(task.getResult().getValue(Clothes.class));
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)//TODO pozbyć się RequiresApi
    private void createIcons(Clothes clothes) {
        for (String category : new String[] {"pants", "dress"}) {
            LinearLayout linearLayout = createScroll();

            List<Integer> bought = Accessibler.get(clothes, "bought_" + category);
            if (bought == null) bought = new ArrayList<>();
            int[] prices = Accessibler.getStatic(Prices.class, category);


            ImageView noneView = prepare(createImage(R.drawable.none), linearLayout, 150, 150);;
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

                ImageView imageView = prepare(createImage(id), layout, 150, 150);

                ImageView coinView = prepare(createImage(R.drawable.moneta), layout, 20, 20);

                TextView textView = prepare(new TextView(this), layout, 130, 20);
                textView.setText(bought.contains(i) ? "Zakupiono" : String.valueOf(prices[i - 1]));

                imageView.setOnClickListener(new ClickListener(category, i, textView));

                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                set.connect(coinView.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.BOTTOM);
                set.connect(textView.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.BOTTOM);
                set.connect(textView.getId(), ConstraintSet.LEFT, coinView.getId(), ConstraintSet.RIGHT);
                set.applyTo(layout);
            }
        }
    }

    /**
     * Zamienia dp na px
     *
     * @param dp ustawiane dp
     * @return piksele
     */
    static int dp(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)//TODO pozbyć się RequiresApi
    private <T extends View> T prepare(T view, ViewGroup parent, int x, int y) {
        view.setLayoutParams(new ConstraintLayout.LayoutParams(dp(x), dp(y)));
        view.setId(View.generateViewId());
        parent.addView(view);
        return view;
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
    /**
     * Tworzy ImageView
     *
     * @param drawable obrazek
     * @return utworzone ImageView
     */
    private ImageView createImage(int drawable) {
        ImageView view = new ImageView(this);
        view.setImageDrawable(ActivityCompat.getDrawable(this, drawable));
        return view;
    }
}
