package com.app.grocerymart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.grocerymart.Adapters.MyOrderAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyOrderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.placed_order_rv);
        TextView noorder = view.findViewById(R.id.no_order);

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String email = sh.getString("email", "");

        if (email.equals("")) {
            startActivity(new Intent(getContext(), SignIn.class));
            Toast.makeText(getContext(), "Please Login First", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }else {

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Orders");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Ion.with(getContext())
                    .load("GET", "http://ec2-18-218-92-210.us-east-2.compute.amazonaws.com:3030/profileRes?" +
                            "user=" + email)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            if (result != null) {
                                Log.e("pares", result + "");
                                MyOrderAdapter myOrderAdapter = new MyOrderAdapter(getContext(), result);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(myOrderAdapter);
                            }

                            if (e != null) {
                                Log.e("pares", e + "");
                            }
                        }
                    });
        }

        return view;
    }
}
