package me.jomi.androidapp.model;

import android.view.View;
import android.widget.TextView;
import me.jomi.androidapp.MainActivity;
import me.jomi.androidapp.R;

import static me.jomi.androidapp.MainActivity.instance;

public class CoinButton {

    private View view;
    private int updateValue;

    public CoinButton(View view, final int updateValue) {
        this.view = view;
        this.updateValue = updateValue;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.getDatabaseManager().updateCoins(updateValue);
                ((TextView) instance.findViewById(R.id.textView)).setText(instance.getDatabaseManager().getCoin() + "$");
            }
        });

    }

    public View getView() {
        return view;
    }

    public int getUpdateValue() {
        return updateValue;
    }
}
