package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {

    EditText email, pass;
    private Animation shakeAnimation;

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
                    String em = email.getText().toString();
                    String pas = pass.getText().toString();

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
        startActivity(new Intent(SignIn.this, Home.class));
        finish();
    }
}
