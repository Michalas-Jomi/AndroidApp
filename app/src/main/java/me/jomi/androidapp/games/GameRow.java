package me.jomi.androidapp.games;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import me.jomi.androidapp.R;
import me.jomi.androidapp.util.ViewUtils;
import me.jomi.androidapp.util.ViewUtils.Connector;

import java.util.Random;

public class GameRow implements View.OnClickListener, View.OnLongClickListener {
    public final ConstraintLayout layout;

    public final Game game;

    //public final TextView textView;
    public final ImageView imageView;

    private boolean unlocked;

    private final Context context;


    public GameRow(Context context, Game game, ConstraintLayout parent) {
        this.context = context;
        this.game = game;


        layout = ViewUtils.prepareToConstraintSet(new ConstraintLayout(context), parent, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));


        imageView = ViewUtils.prepareToConstraintSet(ViewUtils.createImage(context, R.drawable.maker), layout, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

        layout.setOnClickListener(this);
        layout.setOnLongClickListener(this);

        Connector.create(layout)
                .connectWithParent(imageView, Connector.TOP)
                .connectWithParent(imageView, Connector.LEFT)
                .connectWithParent(imageView, Connector.RIGHT)
                .finish();

        Connector.create(parent)
                .connectWithParent(layout, Connector.LEFT)
                .connectWithParent(layout, Connector.RIGHT)
                .connectWithParent(layout, Connector.TOP)
                .connectWithParent(layout, Connector.BOTTOM)
                .biasX(layout, game.x)
                .biasY(layout, game.y)
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

    @Override
    public boolean onLongClick(View v) {
        ViewUtils.toast(context, game.name().toLowerCase().replace("_", " ") + "\n" + ((int) game.energy) + " energi");
        return true;
    }
}
