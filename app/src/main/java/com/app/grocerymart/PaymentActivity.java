package com.app.grocerymart;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.app.grocerymart.Database.CartData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class PaymentActivity extends Activity implements PaymentResultListener {
    String name = "", email = "", orderId = "";
    RazorpayClient razorpay = new RazorpayClient("rzp_test_gxXdHsXsGPtTvp", "8Jc8j68xWN9KtL5nlMuvZdAr");
    CartData mydb;
    ArrayList<String> itemIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sh.getString("email", "");
        name = sh.getString("name", "");

        if(email.equals("")){

        }else{
            mydb = new CartData(this);
            itemIds = mydb.getAllItems();
            final ProgressDialog dialog = ProgressDialog.show(PaymentActivity.this, "",
                    "Please wait...", true);
            dialog.show();
            try {

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                                orderId = "order" + Calendar.getInstance().getTimeInMillis();
                                JSONObject orderRequest = new JSONObject();
                                orderRequest.put("amount", getTotalPrice()*100); // amount in the smallest currency unit
                                orderRequest.put("currency", "INR");
                                orderRequest.put("receipt", email);
                                orderRequest.put("payment_capture", true);

                                Order order = razorpay.Orders.create(orderRequest);

                                orderId = order.get("id");
                                startPayment();
                                dialog.cancel();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();


            } catch (Exception e) {
                // Handle Exception
                dialog.cancel();
                Log.e("excep", e.toString());
                System.out.println(e.getMessage());
            }
        }

    }

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_gxXdHsXsGPtTvp");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_shopping_cart);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", getString(R.string.app_name));

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", email);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", orderId);
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", "500");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("paymentException", "Error in starting Razorpay Checkout" + e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("paymentsuc", "done " + s);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String nam = sh.getString("temp_order_name", "");
        String _id = sh.getString("_id", "");
        String address = sh.getString("temp_order_address", "");

        Log.e("paymentsuc1", "done " + s);
        JsonObject order = new JsonObject();
        //order.addProperty("_id", _id);
        order.addProperty("name", nam);
        order.addProperty("address", address);
        order.addProperty("paymentId", s);

        Log.e("paymentsuc2", "done " + s);
        JsonObject cart = new JsonObject();
        JsonArray items = new JsonArray();

        for(String str : itemIds){
            Cursor itemDetail = mydb.getItem(str);
            JsonObject element = new JsonObject();
            JsonObject item = new JsonObject();
            Log.e("paymentsuc6", "done " + itemDetail.getString(0) + ", "
                    + itemDetail.getString(1) + ", "
                    + itemDetail.getString(2) + ", "
                    + itemDetail.getString(3) + ", "
                    + itemDetail.getString(4) + ", "
                    + itemDetail.getString(5) + ", "
                    + itemDetail.getString(6) + ", ");

            item.addProperty("_id", itemDetail.getString(0));
            item.addProperty("imagePath", itemDetail.getString(1));
            item.addProperty("title", itemDetail.getString(2));
            item.addProperty("description", itemDetail.getString(3));
            item.addProperty("quantity", itemDetail.getString(4));
            item.addProperty("price", itemDetail.getString(5));

            element.add("item", item);
            element.addProperty("qty", itemDetail.getString(6));
            String tip = String.format("Rs. %.2f", Double.parseDouble(itemDetail.getString(5)) *
                    Double.parseDouble(itemDetail.getString(6)));

            element.addProperty("price", tip);
            Log.e("paymentsuc6", "done4 ");

            items.add(element);
        }

        cart.add("items", items);
        cart.addProperty("totalQty", itemIds.size());
        cart.addProperty("totalPrice", String.format("Rs. %.2f", getTotalPrice()));
        order.add("cart", cart);

//        JsonObject finobj = new JsonObject();
//        finobj.add("orderObject", order);

        Log.e("finobj", order.toString());
        Ion.with(getApplicationContext())
                .load("POST", "http://ec2-18-218-92-210.us-east-2.compute.amazonaws.com:3030/saveOrder")
                .setBodyParameter("orderObject", order.toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(result != null && result.get("_id").getAsInt() == 1){
                            Toast.makeText(PaymentActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                            sendNotification("Order Placed Successfully. It will be delivered to given address.");
                            mydb.deleteAllData();
                            finish();
                        }else{
                            Toast.makeText(PaymentActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            sendNotification("Payment failed due to some error. " +
                                    "If money has deducted from your account it will be refunded.");
                            finish();
                        }

                        if(e != null){
                            Log.e("pares1", e+"");
                            Toast.makeText(PaymentActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

        Log.e("paysucc", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("payerr", i + "  " + s);
    }

    public Double getTotalPrice() {
        double sum = 0;

        for(String ids : itemIds) {
            Cursor items = mydb.getItem(ids);
            sum += Double.parseDouble(items.getString(6)) * Double.parseDouble(items.getString(5));
        }

        return sum;
    }

    private void sendNotification(String msg) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Order")
                .setSmallIcon(R.drawable.ic_shopping_cart)
                .setContentTitle("Order")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(234, builder.build());
    }
}
