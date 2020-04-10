package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

//private Api key
//c1ecce51-50c9-4336-8b6f-bce94b6d5fe2
public class SignUp extends AppCompatActivity {

    EditText email, name, pass, pass2;
    Spinner country;
    private Animation shakeAnimation;

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
                    String nam = name.getText().toString().trim();
                    String emai = email.getText().toString().trim();
                    String pas = pass.getText().toString().trim();
                    TextView textView = (TextView) country.getSelectedView();
                    String cnt = textView.getText().toString();

                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    myEdit.putString("email", emai);
                    myEdit.putString("name", nam);
                    myEdit.putString("pass", pas);
                    myEdit.putString("country", cnt);

                    myEdit.apply();

                    startActivity(new Intent(SignUp.this, Home.class));
                    finish();

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
