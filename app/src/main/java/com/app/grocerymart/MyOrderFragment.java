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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.grocerymart.Adapters.MyOrderAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyOrderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.placed_order_rv);

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();
        ArrayList<String> quantity = new ArrayList<>();
        ArrayList<String> image = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(getContext(), name, quantity, status, image, date);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myOrderAdapter);

        return view;
    }
}
