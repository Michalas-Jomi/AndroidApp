package me.jomi.androidapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import me.jomi.androidapp.MainActivity;
import me.jomi.androidapp.R;
import me.jomi.androidapp.model.Clothes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    @SuppressLint("NewApi")
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < 17) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

    /**
     * Wyświetla niewielką wiadomość na ekranie
     *
     * @param context gdzie ma się wyświetlić
     * @param text treść wiadomości
     */
    public static void toast(Context context, String text) {
        Toast.makeText(context, text, text.length() >= 50 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    /**
     * Zamienia dp na px
     *
     * @param dp ustawiane dp
     * @return piksele
     */
    public static int dp(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }


    public static ImageView createImage(Context context, int drawable) {
        ImageView view = new ImageView(context);
        view.setImageDrawable(ActivityCompat.getDrawable(context, drawable));
        return view;
    }
    public static TextView createTextView(Context context, @Nullable String text, @Nullable Float fontSize) {
        TextView view = new TextView(context);
        if (text != null) view.setText(text);
        if (fontSize != null) view.setTextSize(fontSize);
        return view;
    }
    public static TextView createTextView(Context context, String text) {
        return createTextView(context, text, null);
    }
    public static TextView createTextView(Context context, float fontSize) {
        return createTextView(context, null, fontSize);
    }


    /**
     * Przygotowywuje view do ustalenia jego pozycji przez ConstraintSet
     *
     * @param view które jest przygotowywane
     * @param parent view zostanie dodane jako child
     * @param x szerokość view w dp
     * @param y wysokość view w dp
     * @param <T>
     * @return view
     */
    public static <T extends View> T prepareToConstraintSet(T view, @Nullable ViewGroup parent, int x, int y) {
        return prepareToConstraintSet(view, parent, new ConstraintLayout.LayoutParams(dp(x), dp(y)));
    }
    public static <T extends View> T prepareToConstraintSet(T view, @Nullable ViewGroup parent, ViewGroup.LayoutParams params) {
        view.setLayoutParams(params);
        view.setId(ViewUtils.generateViewId());
        if (parent != null) parent.addView(view);
        return view;
    }

    /**
     * Pozwala połączyć boki Views w ConstraintLayout za pośrednictwem ConstraintSet
     */
    public static class Connector {
        private static class ParentView extends View {
            private ParentView() {
                super(MainActivity.instance);
            }
            @Override
            public int getId() {
                return ConstraintSet.PARENT_ID;
            }
        }

        public static final int TOP    = ConstraintSet.TOP;
        public static final int BOTTOM = ConstraintSet.BOTTOM;
        public static final int RIGHT  = ConstraintSet.RIGHT;
        public static final int LEFT   = ConstraintSet.LEFT;

        public static final View PARENT = new ParentView();


        public final ConstraintSet constraintSet = new ConstraintSet();
        public final ConstraintLayout layout;

        public static Connector create(ConstraintLayout layout) {
            return new Connector(layout);
        }
        private Connector(ConstraintLayout layout) {
            constraintSet.clone(layout);
            this.layout = layout;
        }


        /**
         * Łączy View start od strony sideStart z View end od strony sideEnd
         *
         * @param start
         * @param sideStart
         * @param end
         * @param sideEnd
         * @return
         */
        public Connector connect(View start, int sideStart, View end, int sideEnd) {
            constraintSet.connect(start.getId(), sideStart, end.getId(), sideEnd);
            return this;
        }
        /**
         * Łączy View z jego parentem od strony side
         * @param view
         * @param side
         * @return
         */
        public Connector connectWithParent(View view, int side) {
            return connect(view, side, PARENT, side);
        }
        /**
         * Łączy View start od strony sideStart z przeciwnym sidem View end
         * @param start
         * @param sideStart
         * @param end
         * @return this
         */
        public Connector connect(View start, int sideStart, View end) {
            int sideEnd;
            switch (sideStart) {
                case TOP: sideEnd = BOTTOM; break;
                case BOTTOM: sideEnd = TOP; break;
                case LEFT: sideEnd = RIGHT; break;
                case RIGHT: sideEnd = LEFT; break;
                default: throw new IllegalArgumentException("Niepoprawna strona View: " + sideStart);
            }
            return connect(start, sideStart, end, sideEnd);
        }

        public void finish() {
            constraintSet.applyTo(layout);
        }
    }


    private static Map<Integer, List<ImageView>> oldClothes = new HashMap<>();
    public static void dressUpHero(Context context, ConstraintLayout heroLayout, ImageView heroView, Clothes clothes) {
        List<ImageView> oldClothesList = oldClothes.get(heroLayout.getId());
        if (oldClothesList == null)  {
            oldClothesList = new ArrayList<>();
            oldClothes.put(heroLayout.getId(), oldClothesList);
        }
        for (ImageView old : oldClothesList)
            heroLayout.removeView(old);
        oldClothesList.clear();

        dressUpHero(context, heroLayout, heroView, oldClothesList, "cap",   clothes.getCap());
        dressUpHero(context, heroLayout, heroView, oldClothesList, "pants", clothes.getPants());
        dressUpHero(context, heroLayout, heroView, oldClothesList, "shirt", clothes.getShirt());
        dressUpHero(context, heroLayout, heroView, oldClothesList, "dress", clothes.getDress());
    }
    private static void dressUpHero(Context context, ConstraintLayout heroLayout, ImageView heroView, List<ImageView> oldClothesList, String prefix, int version) {
        if (version != 0) {
            int id = Accessibler.getStatic(R.drawable.class, prefix + version);
            ImageView cloth = ViewUtils.prepareToConstraintSet(ViewUtils.createImage(context, id), heroLayout, heroView.getLayoutParams());
            Connector.create(heroLayout)
                    .connectWithParent(cloth, Connector.TOP)
                    .connectWithParent(cloth, Connector.LEFT)
                    .finish();
            oldClothesList.add(cloth);
        }
    }
}
