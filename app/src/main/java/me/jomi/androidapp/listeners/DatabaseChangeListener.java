package me.jomi.androidapp.listeners;


import androidx.core.util.Consumer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import me.jomi.androidapp.ClothesShopActivity;
import me.jomi.androidapp.ProfileActivity;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.model.Clothes;
import me.jomi.androidapp.util.Accessibler;

import java.util.HashMap;
import java.util.Map;


public class DatabaseChangeListener implements ValueEventListener {
    public static class Changes<T> {
        private Map<Class<?>, Consumer<T>> map = new HashMap<>();

        final Class<?> clazz;
        Changes(Class<T> clazz) {
            this.clazz = clazz;
        }

        public void register(Class<?> clazz, Consumer<T> cons) {
            map.put(clazz, cons);
        }
        public boolean unregister(Class<?> clazz) {
            return map.remove(clazz) != null;
        }

        void invoke(T value) {
            for (Consumer<T> cons : map.values())
                try {
                    cons.accept(value);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
        }
    }

    public static final Changes CLOTHES = new Changes(Clothes.class);
    public static final Changes ENERGY = new Changes(float.class);
    public static final Changes MONEY = new Changes(int.class);


    @Override
    public void onDataChange(DataSnapshot snapshot) {
        Changes changes = Accessibler.getStatic(this.getClass(), snapshot.getKey().toUpperCase());

        changes.invoke(snapshot.getValue(changes.clazz));
    }

    @Override
    public void onCancelled(DatabaseError error) {

    }

    public void register() {
        Api.getUser().child("clothes").addValueEventListener(this);
        Api.getUser().child("energy").addValueEventListener(this);
        Api.getUser().child("money").addValueEventListener(this);
    }
}
