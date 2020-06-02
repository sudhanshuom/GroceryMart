package com.app.grocerymart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Checkout extends AppCompatActivity {

    private TextInputLayout name, phone1, phone2, address1, address2, postal, local, district;
    private Animation shakeAnimation;
    RadioGroup radioGroup;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    LocationManager manager;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mLocationCallback;
    ProgressDialog dialog;
    CheckBox defaultAddressCb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);


        name = findViewById(R.id.name_chout);
        phone1 = findViewById(R.id.phone1_chout);
        phone2 = findViewById(R.id.phone2_chout);
        address1 = findViewById(R.id.address1_chout);
        address2 = findViewById(R.id.address2_chout);
        postal = findViewById(R.id.area_pin_chout);
        local = findViewById(R.id.local_area_chout);
        district = findViewById(R.id.district_chout);
        radioGroup = findViewById(R.id.address_rg);
        defaultAddressCb = findViewById(R.id.default_address_cb);
        final RadioButton defAddressrb = findViewById(R.id.default_address);
        final RadioButton newAddressrb = findViewById(R.id.new_address);
        Button continu = findViewById(R.id.continue_chout);
        TextView location = findViewById(R.id.location);

        Bundle extras = getIntent().getExtras();
        boolean fromProfile = false;
        if (extras != null) {
            fromProfile = extras.getBoolean("fromProfile");
            if(fromProfile){
                continu.setText("Done");
                defaultAddressCb.setVisibility(View.GONE);
                newAddressrb.setText("Add new address");
                defAddressrb.setText("Show default address");
            }
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                startLocating();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.new_address){
                    name.getEditText().setText("");
                    phone1.getEditText().setText("");
                    phone2.getEditText().setText("");
                    address1.getEditText().setText("");
                    address2.getEditText().setText("");
                    postal.getEditText().setText("");
                    local.getEditText().setText("");
                    district.getEditText().setText("");
                }else if(checkedId == R.id.default_address){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    if(sharedPreferences.getString("def_order_address", null) == null){
                        Toast.makeText(Checkout.this, "No default address found\nAdd new address", Toast.LENGTH_LONG).show();
                        RadioButton rb = findViewById(R.id.new_address);
                        RadioButton rb2 = findViewById(R.id.default_address);
                        rb.setChecked(true);
                        rb2.setChecked(false);
                    }else {
                        name.getEditText().setText(sharedPreferences.getString("def_order_name", ""));
                        phone1.getEditText().setText(sharedPreferences.getString("def_order_phone1", ""));
                        phone2.getEditText().setText(sharedPreferences.getString("def_order_phone2", ""));
                        address1.getEditText().setText(sharedPreferences.getString("def_order_address1", ""));
                        address2.getEditText().setText(sharedPreferences.getString("def_order_address2", ""));
                        postal.getEditText().setText(sharedPreferences.getString("def_order_areapin", ""));
                        local.getEditText().setText(sharedPreferences.getString("def_order_locareaname", ""));
                        district.getEditText().setText(sharedPreferences.getString("def_order_district", ""));

                    }
                }
            }
        });

        final boolean finalFromProfile = fromProfile;
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalFromProfile){
                    if(newAddressrb.isChecked() && isValid()){
                        String address = getAddressString();
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putString("def_order_address", address);
                        myEdit.putString("def_order_name", name.getEditText().getText().toString().trim());
                        myEdit.putString("def_order_phone1", phone1.getEditText().getText().toString().trim());
                        myEdit.putString("def_order_phone2", phone2.getEditText().getText().toString().trim());
                        myEdit.putString("def_order_address1", address1.getEditText().getText().toString().trim());
                        myEdit.putString("def_order_address2", address2.getEditText().getText().toString().trim());
                        myEdit.putString("def_order_areapin", postal.getEditText().getText().toString().trim());
                        myEdit.putString("def_order_locareaname", local.getEditText().getText().toString().trim());
                        myEdit.putString("def_order_district", district.getEditText().getText().toString().trim());

                        finish();
                    }else if(defAddressrb.isChecked()){
                        finish();
                    }
                    return;
                }else {
                    if (isValid()) {
                        String address = getAddressString();
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        if (defaultAddressCb.isChecked()) {
                            myEdit.putString("def_order_address", address);
                            myEdit.putString("def_order_name", name.getEditText().getText().toString().trim());
                            myEdit.putString("def_order_phone1", phone1.getEditText().getText().toString().trim());
                            myEdit.putString("def_order_phone2", phone2.getEditText().getText().toString().trim());
                            myEdit.putString("def_order_address1", address1.getEditText().getText().toString().trim());
                            myEdit.putString("def_order_address2", address2.getEditText().getText().toString().trim());
                            myEdit.putString("def_order_areapin", postal.getEditText().getText().toString().trim());
                            myEdit.putString("def_order_locareaname", local.getEditText().getText().toString().trim());
                            myEdit.putString("def_order_district", district.getEditText().getText().toString().trim());
                        }

                        myEdit.putString("temp_order_address", address);
                        myEdit.putString("temp_order_name", name.getEditText().getText().toString().trim());
                        myEdit.putString("temp_order_phone1", phone1.getEditText().getText().toString().trim());
                        myEdit.putString("temp_order_phone2", phone2.getEditText().getText().toString().trim());

                        myEdit.apply();
                        Intent in = new Intent(Checkout.this, PaymentActivity.class);
                        startActivity(in);
                        finish();
                    }
                }

            }
        });

    }

    private String getAddressString(){
        StringBuilder st = new StringBuilder();
        st.append(address1.getEditText().getText().toString().trim());
        st.append(", " + address2.getEditText().getText().toString().trim());
        st.append(", " + postal.getEditText().getText().toString().trim());
        st.append(", " + local.getEditText().getText().toString().trim());
        st.append(", " + district.getEditText().getText().toString().trim());
        return st.toString();
    }

    private boolean isValid(){

        if(name.getEditText().getText().toString().trim().length() == 0){
            name.startAnimation(shakeAnimation);
            return false;
        }if(phone1.getEditText().getText().toString().trim().length() != 10){
            phone1.startAnimation(shakeAnimation);
            return false;
        }if(phone2.getEditText().getText().toString().trim().length() != 10){
            phone2.startAnimation(shakeAnimation);
            return false;
        }if(address1.getEditText().getText().toString().trim().length() == 0){
            address1.startAnimation(shakeAnimation);
            return false;
        }if(address2.getEditText().getText().toString().trim().length() == 0){
            address2.startAnimation(shakeAnimation);
            return false;
        }if(postal.getEditText().getText().toString().trim().length() == 0){
            postal.startAnimation(shakeAnimation);
            return false;
        }if(local.getEditText().getText().toString().trim().length() == 0){
            local.startAnimation(shakeAnimation);
            return false;
        }if(district.getEditText().getText().toString().trim().length() == 0){
            district.startAnimation(shakeAnimation);
            return false;
        }

        return true;
    }

    private void startLocating(){
        if (!hasPermissions(this, PERMISSIONS)) {
            new AlertDialog
                    .Builder(this)
                    .setCancelable(true)
                    .setTitle("Permissions not provided")
                    .setMessage("This app uses location permission. Click okay to grant the permission.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ActivityCompat.checkSelfPermission(Checkout.this,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(Checkout.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayLocationSettingsRequest();
        }

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasPermissions(this, PERMISSIONS)) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location mLastLocation = locationResult.getLastLocation();
                    Log.e("lastlocf", mLastLocation.getLatitude() + "");
                    Log.e("lastlocf", mLastLocation.getLongitude() + "");

                }
            };

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    private void getLastLocation() {
        dialog = ProgressDialog.show(Checkout.this, "", "Fetching address", true);
        dialog.setCancelable(false);
        dialog.show();
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.e("lastlocf", location.getLatitude() + "");
                            Log.e("lastlocf", location.getLongitude() + "");

                            getAddress(location.getLatitude(), location.getLongitude());
                        }
                    }
                }
        );
    }

    private void getAddress(double latitude, double longitude) {

        Geocoder geoCoder = new Geocoder(Checkout.this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            if (addresses == null)
                return;

            Log.e("Address", addresses + "");

            address2.getEditText().setText(addresses.get(0).getFeatureName());
            postal.getEditText().setText(addresses.get(0).getPostalCode());
            local.getEditText().setText(addresses.get(0).getLocality());
            district.getEditText().setText(addresses.get(0).getSubAdminArea());
            dialog.cancel();

        } catch (IOException e) {
            Toast.makeText(Checkout.this, "Some error occurred while fetching your location", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            e.printStackTrace();
        }
    }

    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        mLocationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location mLastLocation = locationResult.getLastLocation();
                                Log.e("lastlocf", mLastLocation.getLatitude() + "");
                                Log.e("lastlocf", mLastLocation.getLongitude() + "");

                            }

                        };
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                        getLastLocation();
                    }else{
                        displayLocationSettingsRequest();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new AlertDialog
                            .Builder(this)
                            .setCancelable(true)
                            .setTitle("Permissions not provided")
                            .setMessage("This app uses location permission. Click okay to grant the permission.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (ActivityCompat.checkSelfPermission(Checkout.this,
                                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(Checkout.this,
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(Checkout.this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("dsplocst1", "All location settings are satisfied.");
                        getLastLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("dsplocst2", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Checkout.this, 199);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("dsplocst3", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("dsplocst4", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}
