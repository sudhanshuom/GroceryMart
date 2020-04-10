package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.app.grocerymart.Adapters.CartAdapter;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    static TextView finPrice;
    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        finPrice = findViewById(R.id.totprice);
        RecyclerView rv = findViewById(R.id.cartitems);

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> amount = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<Integer> qty = new ArrayList<>();

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        int a = sharedPreferences.getInt("Coffee", 0);
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
        }

        CartAdapter cd = new CartAdapter(Cart.this, title, amount, price, qty);
        rv.setLayoutManager(new LinearLayoutManager(Cart.this));
        rv.setAdapter(cd);
        updatePrice();
    }

   static public void updatePrice(){

        int a = sharedPreferences.getInt("Coffee", 0);
        int b = sharedPreferences.getInt("Tea", 0);
        int c = sharedPreferences.getInt("Orange Juice", 0);
        int d = sharedPreferences.getInt("Strawberry Juice", 0);

        double sum = a*25 + b*20 + c*50 + d*50;

        finPrice.setText(String.format("%.2f", sum));
    }
}
