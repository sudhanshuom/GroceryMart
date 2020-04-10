package com.app.grocerymart;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.grocerymart.Adapters.HomeCategoriesAdapter;
import com.app.grocerymart.Adapters.SubCatAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class SubCategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sub_category, container, false);

        String root = getArguments().getString("root");
        Button done = view.findViewById(R.id.done);

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        RecyclerView recyclerView = view.findViewById(R.id.subcategoryrv);

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> amount = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();

        title.add("Coffee");
        amount.add("50 gm");
        price.add("25");

        title.add("Tea");
        amount.add("50 gm");
        price.add("20");

        title.add("Orange Juice");
        amount.add("250 ml");
        price.add("50");

        title.add("Strawberry Juice");
        amount.add("250ml");
        price.add("50");

        SubCatAdapter subCatAdapter = new SubCatAdapter(getContext(), title, amount, price);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(subCatAdapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(SubCategoryFragment.this).commit();
            }
        });

        return view;
    }

}
