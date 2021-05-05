package me.jomi.androidapp.games;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import me.jomi.androidapp.R;
import me.jomi.androidapp.util.ViewUtils;

import java.util.Random;

public class GameRow implements View.OnClickListener {
    public final ConstraintLayout layout;

    public final Game game;

    public final TextView textView;
    public final ImageView imageView;

    private boolean unlocked;

    public GameRow(Context context, Game game) {
        this.game = game;

        layout = new ConstraintLayout(context);

        imageView = ViewUtils.prepareToConstraintSet(ViewUtils.createImage(context, R.drawable.shop_icon), layout, 200, 80);

        String text = game.name().toLowerCase().replace("_", " ") + " " + ((int) game.energy) + " energi";
        textView = ViewUtils.prepareToConstraintSet(ViewUtils.createTextView(context, text), layout, 200, 20);

        layout.setOnClickListener(this);

        ViewUtils.Connector.create(layout)
                .connect(textView, ViewUtils.Connector.TOP, imageView)
                .connectWithParent(textView, ViewUtils.Connector.LEFT)
                .connectWithParent(imageView, ViewUtils.Connector.TOP)
                .connectWithParent(imageView, ViewUtils.Connector.LEFT)
                .finish();

        setUnlocked(false);
    }


    public boolean isUnlocked() {
        return unlocked;
    }
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        if (unlocked)
            imageView.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
        else
            imageView.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onClick(View v) {
        if (unlocked)
            game.start();
    }
}
