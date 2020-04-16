package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class SignUp extends AppCompatActivity {

    EditText email, name, pass, pass2;
    Spinner country;
    private Animation shakeAnimation;
    //Server URL
    String server_url_signUp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);

        email = findViewById(R.id.Appsignupemail);
        name = findViewById(R.id.Appsignupfn);
        pass = findViewById(R.id.Appsignuppass1);
        pass2 = findViewById(R.id.Appsignuppass2);
        country = findViewById(R.id.country);

        Button allogin = findViewById(R.id.allogin);
        Button signUp = findViewById(R.id.Appsignupsignupbtn);

        allogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    final String nam = name.getText().toString().trim();
                    final String emai = email.getText().toString().trim();
                    final String pas = pass.getText().toString().trim();
                    TextView textView = (TextView) country.getSelectedView();
                    final String cnt = textView.getText().toString();

                    final ProgressDialog dialog = ProgressDialog.show(SignUp.this, "",
                            "Contacting server. Please wait...", true);
                    dialog.show();

//                    Ion.with(getApplicationContext())
//                            .load("POST",server_url_signUp)
//                            .setBodyParameter("name", nam)
//                            .setBodyParameter("password", pas)
//                            .setBodyParameter("email", emai)
//                            .asJsonObject()
//                            .setCallback(new FutureCallback<JsonObject>() {
//                                @Override
//                                public void onCompleted(Exception e, JsonObject result) {
//                                    /** Server returns a json object, format is specified in the backend
//                                     documentation
//                                     */
//
//                                    if (result != null) {
//                                        Log.e("got", result.toString());
//                                        Toast.makeText(getApplicationContext(), "User-Verified",
//                                                Toast.LENGTH_LONG).show();

                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                        myEdit.putString("email", emai);
                                        myEdit.putString("name", nam);
                                        myEdit.putString("password", pas);
                                        myEdit.putString("country", cnt);

                                        myEdit.apply();

                                        dialog.dismiss();

                                        startActivity(new Intent(SignUp.this, Home.class));
                                        finish();

//                                    } else {
//                                        dialog.dismiss();
//                                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });

                }
            }
        });

    }

    private boolean isValid(){
        if(name.getText().toString().trim().length() == 0){
            name.startAnimation(shakeAnimation);
            name.setError("Enter name");
            return false;
        } else if(email.getText().toString().trim().length() == 0){
            email.startAnimation(shakeAnimation);
            email.setError("Enter valid Email");
            return false;
        } else if(pass.getText().toString().trim().length() == 0){
            pass.startAnimation(shakeAnimation);
            pass.setError("Enter valid password");
            return false;
        } else if(pass2.getText().toString().trim().length() == 0){
            pass2.startAnimation(shakeAnimation);
            pass2.setError("Enter password");
            return false;
        } else if(!pass.getText().toString().equals(pass2.getText().toString())){
            pass.startAnimation(shakeAnimation);
            pass.setError("Password doesn't match");
            pass2.startAnimation(shakeAnimation);
            pass2.setError("Password doesn't match");
            return false;
        } else if(((TextView) country.getSelectedView()).getText().equals("Country")){
            country.startAnimation(shakeAnimation);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp.this, SignIn.class));
    }
}
