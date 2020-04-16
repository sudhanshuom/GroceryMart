package com.app.grocerymart;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.grocerymart.Adapters.HomeCategoriesAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {


    String titles[] = {"Beverages", "Bread/Bakery", "Canned/Jarred Goods", "Dry/Baking Goods", "Dairy", "Frozen Foods",
    "Meat", "Produced", "Personal Care", "Cleaners", "Paper Goods"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Category");

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        GridView gv = view.findViewById(R.id.categorygv);

        Drawable imgss[] = {
                getActivity().getResources().getDrawable(R.drawable.beverages),
                getActivity().getResources().getDrawable(R.drawable.bakery),
                getActivity().getResources().getDrawable(R.drawable.canned),
                getActivity().getResources().getDrawable(R.drawable.baking),
                getActivity().getResources().getDrawable(R.drawable.dairy),
                getActivity().getResources().getDrawable(R.drawable.frozen),
                getActivity().getResources().getDrawable(R.drawable.meat),
                getActivity().getResources().getDrawable(R.drawable.produce),
                getActivity().getResources().getDrawable(R.drawable.soap),
                getActivity().getResources().getDrawable(R.drawable.cleaners),
                getActivity().getResources().getDrawable(R.drawable.paper)
        };

        ArrayList<String> title = new ArrayList<>();
        ArrayList<Drawable> imgs = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            title.add(titles[i]);
            imgs.add(imgss[i]);
        }
        HomeCategoriesAdapter homeCategoriesAdapter = new HomeCategoriesAdapter(getContext(), title, imgs);

        gv.setAdapter(homeCategoriesAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategoryFragment fragment = new SubCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("root", titles[position]);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
