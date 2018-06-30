package com.example.g4all.findme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Switch;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity implements LocationListener {

    Button btnGetLocation;
    TextView tvAddress;

    private FusedLocationProviderClient client;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        client = LocationServices.getFusedLocationProviderClient(this);

        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String resultLatitude = " " + latitude;
                            String resultLongitude = " " + longitude;

                            TextView txtLatitude = findViewById(R.id.txtLatitude);
                            txtLatitude.setText(resultLatitude);

                            TextView txtLongitude = findViewById(R.id.txtLongitude);
                            txtLongitude.setText(resultLongitude);

                            Switch switchAddress = findViewById(R.id.switchAddress);


                            /*
                            - checks if switchAddress is switched on or off
                            - if so, it will show the address (street, zip, city, state, etc) when btnGetLocation is pressed
                            - if not, it will clear tvAddress when btnGetLocation is pressed
                             */
                            if (switchAddress.isChecked()) {  // checks if switchAddress is switched on or off
                                try {
                                    //getAddressFromLocation(latitude,longitude);
                                    TextView tvAddress = findViewById(R.id.tvAddress);
                                    tvAddress.setText(getAddressFromLocation(latitude,longitude).toString());  // shows address (street, zip, city, state, etc)
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                TextView tvAddress = findViewById(R.id.tvAddress);
                                tvAddress.setText("");  // clear tvAddress
                            }

                        }

                    }
                });

            }
        });
    }

    /*
     - gets the address using latitude & longitude
     - address can be separated into individual parts if needed ( commented out because not needed )
     */
    public String getAddressFromLocation(final double latitude, final double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        //String city = addresses.get(0).getLocality();
        //String state = addresses.get(0).getAdminArea();
        //String country = addresses.get(0).getCountryName();
        //String postalCode = addresses.get(0).getPostalCode();
        //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        return address;
    }


    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[] {ACCESS_FINE_LOCATION}, 1);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}




