package com.example.mylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
LocationManager locationManager;
LocationListener locationListener;
TextView textView;
String address="";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                textView.setText("GPS disabled, please enable GPS");
             else{
                 textView.setText("please wait!");
                }

            if(grantResults.length>0 && permissions[0]==Manifest.permission.ACCESS_FINE_LOCATION){

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 1, locationListener);
                }
            }


        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            textView.setText("GPS disabled, please enable GPS");
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                address="Latitude :"+location.getLatitude()+"\nLongitide :"+location.getLongitude()+"\nAccuracy: "+location.getAccuracy()+"\nAltitude :"+location.getAltitude();

                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addr=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);


                    address+="\n\nAddress:";
                    if(addr.get(0).getThoroughfare()!=null){
                        address+="\n "+addr.get(0).getThoroughfare();
                    }
                    if(addr.get(0).getLocality()!=null){
                        address+="\n "+addr.get(0).getLocality();
                    }
                    if(addr.get(0).getAdminArea()!=null){
                        address+="\n "+addr.get(0).getAdminArea();
                    }
                    if(addr.get(0).getPostalCode()!=null){
                        address+="\n "+addr.get(0).getPostalCode();
                    }
                    if(addr.get(0).getCountryName()!=null){
                        address+="\n "+addr.get(0).getCountryName();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                textView.setText(address);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                textView.setText("please wait!");
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 1, locationListener);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(MainActivity.this, "Gps disabled!", Toast.LENGTH_SHORT).show();
            }
        };
 if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 1, locationListener);
            }
        }

    }
}
