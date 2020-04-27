package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;

public class PaymentActivity extends Activity implements PaymentResultListener {
    String name = "", email = "", orderId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sh.getString("email", "");
        name = sh.getString("name", "");

        if(email.equals("")){

        }else{
            //startPayment();
            try {
                orderId = "order" + Calendar.getInstance().getTimeInMillis();
                JsonObject orderRequest = new JsonObject();
                orderRequest.addProperty("amount", 500); // amount in the smallest currency unit
                orderRequest.addProperty("currency", "INR");
                orderRequest.addProperty("receipt", orderId);
                orderRequest.addProperty("payment_capture", false);

                Ion.with(getApplicationContext())
                        .load("POST","https://api.razorpay.com/v1/orders")
                        .setJsonObjectBody(orderRequest)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                /** Server returns a json object, format is specified in the backend
                                 documentation
                                 */
                                if(result!= null)
                                Log.e("got", result.toString());

                                if(e != null){
                                    Log.e("got", e.toString());
                                }
                            }
                        });

            } catch (Exception e) {
                // Handle Exception
                System.out.println(e.getMessage());
            }
        }

    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_gxXdHsXsGPtTvp");
        /**
         * Instantiate Checkout
         */

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
        Log.e("paysucc", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("payerr", i + "  " + s);
    }
}
