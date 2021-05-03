package me.jomi.androidapp.util;

import android.widget.EditText;

public class Checker {
    /**
     * Sprawdza czy test jest prawdziwy, jeśli nie wyrzuci IllegalArgumentException
     *
     * @param test warunek który powinien być spełniony
     * @param view View na który będzie rzucany focus i error
     * @param info wyświetlany error
     * @throws IllegalArgumentException wyrzucany gdy @code{test == false}
     */
    public static void checkEditText(boolean test, EditText view, String info) throws IllegalArgumentException {
        if (!test) {
            view.setError(info);
            view.requestFocus();
            throw new IllegalArgumentException(info);
        }
    }
}
