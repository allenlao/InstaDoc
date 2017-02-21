package com.example.allenlao.instadoc;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity {

    // Buttons on Home Page
    private Button buttonHome1;
    private Button buttonHome2;
    private Button buttonHome3;
    private Button buttonHome4;
    private Button buttonHome5;
    public double latitude;
    public double longitude;

    // Stuff on the Hospital Finder
    private TextView locationTextView;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private ImageView nearbyHospitalsImage;

    // Stuff for Profile Page
    private TextView profileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning Buttons
        buttonHome1 = (Button) findViewById(R.id.choking_button);
        buttonHome2 = (Button) findViewById(R.id.intoxication_button);
        buttonHome3 = (Button) findViewById(R.id.cpr_button);
        buttonHome4 = (Button) findViewById(R.id.seizure_button);
        buttonHome5 = (Button) findViewById(R.id.send_location_button);


        //Nearby Hospitals Image
        nearbyHospitalsImage = (ImageView) findViewById(R.id.nearbyHospitalsImage);

        // Assigning Profile Page Stuff
        profileText = (TextView) findViewById(R.id.profileText);

        //Assigning Stuff for Hospital Finder
        locationTextView = (TextView) findViewById(R.id.text_location);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                locationTextView.append("\n" + location.getLatitude() + " " + location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        // Checking for Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;
            } else{
                configureButton();
            }
        }




        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                buttonHome1.setVisibility(VISIBLE);
                                buttonHome2.setVisibility(VISIBLE);
                                buttonHome3.setVisibility(VISIBLE);
                                buttonHome4.setVisibility(VISIBLE);
                                buttonHome5.setVisibility(VISIBLE);
                                locationTextView.setVisibility(VISIBLE);
                                nearbyHospitalsImage.setVisibility(GONE);
                                profileText.setVisibility(GONE);
                                break;
                            case R.id.action_hospitals:
                                buttonHome1.setVisibility(GONE);
                                buttonHome2.setVisibility(GONE);
                                buttonHome3.setVisibility(GONE);
                                buttonHome4.setVisibility(GONE);
                                buttonHome5.setVisibility(GONE);
                                locationTextView.setVisibility(GONE);
                                nearbyHospitalsImage.setVisibility(VISIBLE);
                                profileText.setVisibility(GONE);
                                break;
                            case R.id.action_profile:
                                buttonHome1.setVisibility(GONE);
                                buttonHome2.setVisibility(GONE);
                                buttonHome3.setVisibility(GONE);
                                buttonHome4.setVisibility(GONE);
                                buttonHome5.setVisibility(GONE);                                locationTextView.setVisibility(VISIBLE);
                                locationTextView.setVisibility(GONE);
                                nearbyHospitalsImage.setVisibility(GONE);
                                profileText.setVisibility(VISIBLE);
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        buttonHome5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+12055684011", null, "I need help at this location: Latitude: " + latitude + " Longitude: " + longitude, null, null);
                Toast.makeText(getApplicationContext(), "Your Location Has Been Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showInfo(View view){
        String button_text;
        button_text = ((Button) view).getText().toString();
        if (button_text.equals("Choking")) {
            Intent intent = new Intent(this, ChokingActivity.class);
            startActivity(intent);
        }else if (button_text.equals("Intoxication")){
            Intent intent = new Intent(this, IntoxicationActivity.class);
            startActivity(intent);
        }
       else if (button_text.equals("CPR")){
            Intent intent = new Intent(this, CPRActivity.class);
            startActivity(intent);
        }else if (button_text.equals("Seizure")){
            Intent intent = new Intent(this, SeizureActivity.class);
            startActivity(intent);
        }
    }
}