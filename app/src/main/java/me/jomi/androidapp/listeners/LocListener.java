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
import me.jomi.androidapp.MainActivity;

import static me.jomi.androidapp.MainActivity.locListener;
import static me.jomi.androidapp.MainActivity.locationManager;

public class LocListener implements LocationListener {

    private LocationManager locationManager;
    private boolean isEnabled;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println("Latitude" + location.getLatitude() + " Longitude" + location.getLongitude());
   }

   @SuppressLint("MissingPermission")
   public void registerListener(){
        if(isEnabled) return;
       locationManager = (LocationManager) MainActivity.instance.getSystemService(Context.LOCATION_SERVICE);
       if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
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
