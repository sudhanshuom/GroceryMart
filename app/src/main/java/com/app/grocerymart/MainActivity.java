package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "GroceryMart";
    String[] notificationChannel = {"Update", "Offer", "Wallet", "Coupon", "Payment", "Order"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannels();

        startActivity(new Intent(MainActivity.this, Home.class));
        finish();
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
