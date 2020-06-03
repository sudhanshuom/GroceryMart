package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.grocerymart.Adapters.CartAdapter;
import com.app.grocerymart.Database.CartData;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    static TextView finPrice;
    static SharedPreferences sharedPreferences;
    static private CartData mydb;
    static private RecyclerView rv;
    static private ArrayList<String> itemIds;
    static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        finPrice = findViewById(R.id.totprice);
        rv = findViewById(R.id.cartitems);
        Button checkout = findViewById(R.id.checkout);
        ctx = Cart.this;

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> amount = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<Integer> qty = new ArrayList<>();

        mydb = new CartData(this);
        itemIds = mydb.getAllItems();

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

       /* int a = sharedPreferences.getInt("Coffee", 0);
        int b = sharedPreferences.getInt("Tea", 0);
        int c = sharedPreferences.getInt("Orange Juice", 0);
        int d = sharedPreferences.getInt("Strawberry Juice", 0);

        if(a != 0){
            title.add("Coffee");
            amount.add("50 gm");
            price.add("25");
            qty.add(a);
        }
        if(b != 0){
            title.add("Tea");
            amount.add("50 gm");
            price.add("20");
            qty.add(b);
        }
        if(c != 0){
            title.add("Orange Juice");
            amount.add("250 ml");
            price.add("50");
            qty.add(c);
        }
        if(d != 0){
            title.add("Strawberry Juice");
            amount.add("250ml");
            price.add("50");
            qty.add(d);
        }*/


        Log.e("cartss", itemIds + "");
        if (itemIds != null && itemIds.size() > 0) {
            CartAdapter cd = new CartAdapter(Cart.this, itemIds);
            rv.setLayoutManager(new LinearLayoutManager(Cart.this));
            rv.setAdapter(cd);
        }
        updatePrice();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveData();
                //sendNotification();
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String emai = sh.getString("email", "");

                if (emai.equals("")) {
                    startActivity(new Intent(Cart.this, SignIn.class));
                    Toast.makeText(Cart.this, "Please Login First", Toast.LENGTH_LONG).show();
                } else if(itemIds != null && itemIds.size() > 0){
                    startActivity(new Intent(Cart.this, Checkout.class));
                    finish();
                }else{
                    Toast.makeText(Cart.this, "Add at least one item to cart", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Order")
                .setSmallIcon(R.drawable.ic_shopping_cart)
                .setContentTitle("Order")
                .setContentText("Order Placed Successfully")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(234, builder.build());
    }

    static public Double updatePrice() {
        double sum = 0;

        for(String ids : itemIds) {
            Cursor items = mydb.getItem(ids);
            sum += Double.parseDouble(items.getString(6)) * Double.parseDouble(items.getString(5));
        }

        finPrice.setText(String.format("Rs. %.2f", sum));
        return sum;
    }

    public static void updateView(){
        itemIds = mydb.getAllItems();
        rv.setAdapter(null);
        if (itemIds != null && itemIds.size() > 0) {
            CartAdapter cd = new CartAdapter(ctx, itemIds);
            rv.setLayoutManager(new LinearLayoutManager(ctx));
            rv.setAdapter(cd);
        }
        updatePrice();
    }
}
