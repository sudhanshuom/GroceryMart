package com.app.grocerymart.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.app.grocerymart.R;

import java.util.ArrayList;

public class HomeCategoriesAdapter extends BaseAdapter {

    Context context;
    ArrayList<String>  cat;
    ArrayList<Drawable>  img;

    public HomeCategoriesAdapter(Context ctx, ArrayList<String> cat, ArrayList<Drawable> imag) {
        this.context = ctx;
        this.cat = cat;
        img = imag;
    }

    @Override
    public int getCount() {
        return cat.size();
    }

    @Override
    public Object getItem(int position) {
        return cat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, @NonNull ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            assert mInflater != null;
            view = mInflater.inflate(R.layout.item_for_home_categories, null);
        }

        TextView tv = view.findViewById(R.id.category);
        ImageView im = view.findViewById(R.id.catimage);

        tv.setText(cat.get(position));
        im.setImageDrawable(img.get(position));

        return view;
    }

}