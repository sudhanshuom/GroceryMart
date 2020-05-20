package com.app.grocerymart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import java.util.Calendar;

public class PaymentActivity extends Activity implements PaymentResultListener {
    String name = "", email = "", orderId = "";
    RazorpayClient razorpay = new RazorpayClient("rzp_test_gxXdHsXsGPtTvp", "8Jc8j68xWN9KtL5nlMuvZdAr");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sh.getString("email", "");
        name = sh.getString("name", "");

        if(email.equals("")){

        }else{
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
                                orderRequest.put("amount", 500); // amount in the smallest currency unit
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
        JsonObject user = new JsonObject();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emai = sh.getString("email", "");
        String nam = sh.getString("name", "");
        user.addProperty("name", nam);
        user.addProperty("email", emai);
        Log.e("paysucc", s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("payerr", i + "  " + s);
    }
}
