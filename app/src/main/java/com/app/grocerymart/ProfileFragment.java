package com.app.grocerymart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = view.findViewById(R.id.pr_name);
        TextView email = view.findViewById(R.id.pr_email);
        TextView viewOrder = view.findViewById(R.id.view_orders);
        TextView viewAddress = view.findViewById(R.id.view_address);

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emai = sh.getString("email", "");
        String nam = sh.getString("name", "");

        if (emai.equals("")) {
            startActivity(new Intent(getContext(), SignIn.class));
            Toast.makeText(getContext(), "Please Login First", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }else{
            name.setText(nam);
            email.setText(emai);
        }

        viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MyOrderFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            }
        });

        viewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), Checkout.class);
                in.putExtra("fromProfile", true);
                startActivity(in);
            }
        });

        return view;
    }
}
