package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SignIn extends AppCompatActivity {

    EditText email, pass;
    private Animation shakeAnimation;
    private String server_url_signIn = "http://e-grocery-mart.herokuapp.com/doSigninRes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);

        email = findViewById(R.id.signinemail);
        pass = findViewById(R.id.signinpassword);

        Button btn = findViewById(R.id.signinsigninbtn);
        TextView su = findViewById(R.id.signinsignuptv);

        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    final String em = email.getText().toString();
                    String pas = pass.getText().toString();
                    final ProgressDialog dialog = ProgressDialog.show(SignIn.this, "",
                            "Please wait...", true);
                    dialog.show();
                    Ion.with(getApplicationContext())
                            .load("POST",server_url_signIn)
                            .setBodyParameter("email", em)
                            .setBodyParameter("password", pas)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    /** Server returns a json object, format is specified in the backend
                                     documentation
                                     */
                                    Log.e("got", result+"");

                                    if (result != null && result.get("name") != null) {
                                        Toast.makeText(getApplicationContext(), "User-Verified",
                                                Toast.LENGTH_LONG).show();

                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                        myEdit.putString("email", em);
                                        myEdit.putString("name", result.get("name").getAsString());
                                        myEdit.putString("_id", result.get("_id").getAsString());

                                        myEdit.apply();

                                        dialog.dismiss();

                                        startActivity(new Intent(SignIn.this, Home.class));
                                        finish();

                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private boolean isValid(){
        if(email.getText().toString().trim().length() == 0){
            email.startAnimation(shakeAnimation);
            email.setError("Enter valid Email");
            return false;
        } else if(pass.getText().toString().trim().length() == 0){
            pass.startAnimation(shakeAnimation);
            pass.setError("Enter valid password");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(SignIn.this, Home.class));
        finish();
    }
}
