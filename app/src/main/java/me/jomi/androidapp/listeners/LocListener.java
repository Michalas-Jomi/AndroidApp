package me.jomi.androidapp.listeners;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import me.jomi.androidapp.MainActivity;
import me.jomi.androidapp.UserProfile;
import me.jomi.androidapp.api.Api;
import me.jomi.androidapp.model.UserActivity;

import static me.jomi.androidapp.MainActivity.locListener;
import static me.jomi.androidapp.MainActivity.locationManager;

public class LocListener implements LocationListener {

    private LocationManager locationManager;
    private boolean isEnabled;
//TODO: zrobic jakis wzor, dodajacy energie za jakas konkretna zmianą lokalizacji.
    @Override
    public void onLocationChanged(final Location location) {
        final double currentLatitude = location.getLatitude();
        final double currentLongitude = location.getLongitude();
        UserProfile.locCoords.setText(currentLatitude + "\n" + currentLatitude);
        final double[] lastLatitude = new double[1];
        final double[] lastLongitude = new double[1];

        switch (UserProfile.userActivity){
            case RUNNING:

            case BIKING:
                Api.database.getReference().child("users").child(Api.auth.getCurrentUser().getUid()).child("location").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DataSnapshot data : task.getResult().getChildren())
                                if (data.getKey().equals("latitude")) lastLatitude[0] = data.getValue(Double.class);
                                else lastLongitude[0] = data.getValue(Double.class);

                            Api.database.getReference().child("users").child(Api.auth.getCurrentUser().getUid()).child("location")
                                    .setValue(new me.jomi.androidapp.model.Location(currentLatitude, currentLongitude));

                            if(lastLatitude[0] == 0 && lastLongitude[0] == 0) return;

                            float changedDistance = distFrom((float) currentLatitude, (float) currentLongitude, (float)lastLatitude[0], (float) lastLongitude[0]);
                            UserProfile.locCoords.setText(currentLatitude + "\n" + currentLongitude + "\n" + changedDistance);
                            System.out.println(changedDistance);;

                        }
                    }
                });

            case NONE:
        }
      //  System.out.println("Latitude" + location.getLatitude() + " Longitude" + location.getLongitude());
   }

    private float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

   @SuppressLint("MissingPermission")
   public void registerListener(){
        if(isEnabled) return;
       locationManager = (LocationManager) MainActivity.instance.getSystemService(Context.LOCATION_SERVICE);
       if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
           isEnabled = true;
       }
       else{
           Toast.makeText(MainActivity.instance, "Wlacz GPS!", Toast.LENGTH_SHORT).show();
           MainActivity.instance.startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
       }
   }

   public void unregisterListener(){
        locationManager.removeUpdates(locListener);
        isEnabled = false;
   }

    public boolean isEnabled() {
        return isEnabled;
    }


}
