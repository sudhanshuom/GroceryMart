package com.app.grocerymart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import static android.content.Context.MODE_PRIVATE;

public class MyOrderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        TextView name = view.findViewById(R.id.pr_name);
        TextView email = view.findViewById(R.id.pr_email);

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("My Orders");

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emai = sh.getString("email", "");
        String nam = sh.getString("name", "");

        if (emai.equals("")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            startActivity(new Intent(getContext(), SignIn.class));
            Toast.makeText(getContext(), "Please Login First", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }else{
            name.setText(nam);
            email.setText(emai);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            }
        });
        return view;
    }
}
