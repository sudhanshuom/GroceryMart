package com.app.grocerymart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.grocerymart.Adapters.HomeCategoriesAdapter;
import com.app.grocerymart.Adapters.ImageAdapter;
import com.app.grocerymart.Singelton.Categories;
import com.app.grocerymart.Widget.AutoScrollViewPager;
import com.app.grocerymart.Widget.HeaderGridView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    String titles[] = {"Beverages", "Bread/Bakery", "Canned/Jarred Goods", "Dry/Baking Goods"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Grocery Mart");

        NavigationView navigationView;
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View headerView = layoutInflater.inflate(R.layout.header_of_grid_hf, null);
        View footerView = layoutInflater.inflate(R.layout.footer_of_grid_hf, null);

        CardView note = headerView.findViewById(R.id.note);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SignIn.class));
                getActivity().finish();
            }
        });

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String email = sh.getString("email", "");

        if (email.equals("")) {
            note.setVisibility(View.VISIBLE);
        }else{
            note.setVisibility(View.GONE);
        }



        HeaderGridView gv = view.findViewById(R.id.categorygv);
        AutoScrollViewPager viewPager = headerView.findViewById(R.id.pager);
        AutoScrollViewPager viewPager2 = footerView.findViewById(R.id.pager2);

        TextView tv = footerView.findViewById(R.id.viewmore);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CategoryFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.nav_host_fragment, fragment).commit();
            }
        });

        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> urls2 = new ArrayList<>();

//        Drawable imgss[] = {
//                getActivity().getResources().getDrawable(R.drawable.beverages),
//                getActivity().getResources().getDrawable(R.drawable.bakery),
//                getActivity().getResources().getDrawable(R.drawable.canned),
//                getActivity().getResources().getDrawable(R.drawable.baking),
////                getActivity().getResources().getDrawable(R.drawable.dairy),
////                getActivity().getResources().getDrawable(R.drawable.frozen),
////                getActivity().getResources().getDrawable(R.drawable.meat),
////                getActivity().getResources().getDrawable(R.drawable.produce),
////                getActivity().getResources().getDrawable(R.drawable.soap),
////                getActivity().getResources().getDrawable(R.drawable.cleaners),
////                getActivity().getResources().getDrawable(R.drawable.paper)
//        };

        urls.add("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg");
        urls.add("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg");
        urls.add("https://www.birthdaywishes.expert/wp-content/uploads/2015/10/cover-photo-good-morning-images.jpg");
        urls.add("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg");
        urls.add("https://www.birthdaywishes.expert/wp-content/uploads/2015/10/cover-photo-good-morning-images.jpg");
        urls.add("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg");


        urls2.add("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg");
        urls2.add("https://www.birthdaywishes.expert/wp-content/uploads/2015/10/cover-photo-good-morning-images.jpg");
        urls2.add("https://images.pexels.com/photos/36717/amazing-animal-beautiful-beautifull.jpg");

        viewPager.setAdapter(new ImageAdapter(getContext(), urls));
        viewPager.startAutoScroll();
        viewPager.setInterval(3000);
        viewPager.setCycle(true);

        viewPager2.setAdapter(new ImageAdapter(getContext(), urls2));
        viewPager2.startAutoScroll();
        viewPager2.setInterval(3000);
        viewPager2.setCycle(true);

        ArrayList<String> title = new ArrayList<>();
        ArrayList<Drawable> imgs = new ArrayList<>();
        JsonArray array = new JsonArray();

        for (int i = 0; i < 4; i++) {
//            title.add(titles[i]);
//            imgs.add(imgss[i]);
            array.add(Categories.getInstance().getJsonArray().get(i));
        }


        HomeCategoriesAdapter homeCategoriesAdapter = new HomeCategoriesAdapter(getContext(), array);
        gv.addHeaderView(headerView);
        gv.addFooterView(footerView);

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
