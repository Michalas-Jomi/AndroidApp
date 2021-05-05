package me.jomi.androidapp.util;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import me.jomi.androidapp.api.Api;

public class Energy {

    public static void addEnergy(final float additionalEnergy){
        Api.getUser().child("energy").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                float energy = task.getResult().getValue(Float.class);
                if(energy + additionalEnergy > 100) {
                    Api.getUser().child("energy").setValue(100);
                    return;
                }
                if(energy + additionalEnergy < 100){
                    Api.getUser().child("energy").setValue(0);
                    return;
                }
             //   System.out.println("dodana energia + " + energy + additionalEnergy);

                Api.getUser().child("energy").setValue(energy + additionalEnergy);
            }
        });
    }
}
