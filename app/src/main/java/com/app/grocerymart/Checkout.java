package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;

public class Checkout extends AppCompatActivity {

    private TextInputLayout name, phone1, phone2, address1, address2, postal, local, district;
    private Animation shakeAnimation;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);

        name = findViewById(R.id.name_chout);
        phone1 = findViewById(R.id.phone1_chout);
        phone2 = findViewById(R.id.phone2_chout);
        address1 = findViewById(R.id.address1_chout);
        address2 = findViewById(R.id.address2_chout);
        postal = findViewById(R.id.area_pin_chout);
        local = findViewById(R.id.local_area_chout);
        district = findViewById(R.id.district_chout);
        Button continu = findViewById(R.id.continue_chout);

        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){

                }
                startActivity(new Intent(Checkout.this, PaymentActivity.class));
                finish();
            }
        });

    }

    private boolean isValid(){

        if(name.getEditText().getText().toString().trim().length() == 0){
            name.startAnimation(shakeAnimation);
            return false;
        }if(phone1.getEditText().getText().toString().trim().length() != 10){
            phone1.startAnimation(shakeAnimation);
            return false;
        }if(phone2.getEditText().getText().toString().trim().length() != 10){
            phone2.startAnimation(shakeAnimation);
            return false;
        }if(address1.getEditText().getText().toString().trim().length() == 0){
            address1.startAnimation(shakeAnimation);
            return false;
        }if(address2.getEditText().getText().toString().trim().length() == 0){
            address2.startAnimation(shakeAnimation);
            return false;
        }if(postal.getEditText().getText().toString().trim().length() == 0){
            postal.startAnimation(shakeAnimation);
            return false;
        }if(local.getEditText().getText().toString().trim().length() == 0){
            local.startAnimation(shakeAnimation);
            return false;
        }if(district.getEditText().getText().toString().trim().length() == 0){
            district.startAnimation(shakeAnimation);
            return false;
        }

        return true;
    }
}
