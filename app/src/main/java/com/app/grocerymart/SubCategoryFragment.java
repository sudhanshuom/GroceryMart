package com.app.grocerymart;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.grocerymart.Adapters.HomeCategoriesAdapter;
import com.app.grocerymart.Adapters.SubCatAdapter;
import com.app.grocerymart.Singelton.Categories;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class SubCategoryFragment extends Fragment {

    String categoryName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        categoryName = getArguments().getString("catName");

        String root = getArguments().getString("root");
        Button done = view.findViewById(R.id.done);

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        final RecyclerView recyclerView = view.findViewById(R.id.subcategoryrv);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("searchText", "beverages");

        Ion.with(getActivity().getApplicationContext())
                .load("GET","http://ec2-18-218-92-210.us-east-2.compute.amazonaws.com:3030/searchByCategory?searchText=beverages")
                //.setJsonObjectBody(jsonObject)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        /** Server returns a json object, format is specified in the backend
                         documentation
                         */
                        Log.e("got", result+"");

                        if (result != null) {
                            SubCatAdapter subCatAdapter = new SubCatAdapter(getContext(), result);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(subCatAdapter);

                        } else {
                            Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(SubCategoryFragment.this).commit();
            }
        });

        return view;
    }

}
