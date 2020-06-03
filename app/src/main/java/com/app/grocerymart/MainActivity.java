package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.grocerymart.Singelton.Categories;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "GroceryMart";
    String[] notificationChannel = {"Update", "Offer", "Wallet", "Coupon", "Payment", "Order"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        createNotificationChannels();
        Ion.with(getApplicationContext())
                .load("GET","http://ec2-18-218-92-210.us-east-2.compute.amazonaws.com:3030/getCategories")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        /** Server returns a json object, format is specified in the backend
                         documentation
                         */
                        Log.e("got", result+"");

                        if (result != null) {
                            Toast.makeText(getApplicationContext(), "User-Verified",
                                    Toast.LENGTH_LONG).show();

                            Categories cg = Categories.getInstance();
                            cg.setJsonArray(result);
                            startActivity(new Intent(MainActivity.this, Home.class));

                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createNotificationChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        for (String str : notificationChannel)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(str, str, NotificationManager.IMPORTANCE_DEFAULT);
                mChannel.setDescription("For " + str + " Related");
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setShowBadge(false);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(mChannel);
            }
    }
}
