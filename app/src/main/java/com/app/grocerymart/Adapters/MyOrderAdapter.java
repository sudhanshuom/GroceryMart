package com.app.grocerymart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.grocerymart.Cart;
import com.app.grocerymart.MyOrderDetailsFragment;
import com.app.grocerymart.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context context;
    private JsonArray placedOrder;

    public MyOrderAdapter(Context ctx, JsonArray placedOrder) {
        this.context = ctx;
        this.placedOrder = placedOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_for_my_order, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final JsonObject object = placedOrder.get(position).getAsJsonObject();
        final JsonObject cart = object.get("cart").getAsJsonObject();
        JsonObject items = cart.get("items").getAsJsonObject();
        Set ids = items.entrySet();
        String name = "";
        final JsonArray itemss = new JsonArray();

        ArrayList<Integer> codes = new ArrayList<Integer>();
        for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
            JsonObject object1 = entry.getValue().getAsJsonObject();
            itemss.add(object1);
            Log.e("object", object1+"");
            name += object1.get("item").getAsJsonObject().get("title").getAsString() + ", ";
        }
        Log.e("itemss", itemss+"");

        holder.name.setText(name.substring(0, name.length() - 2));
        holder.quantiy.setText("Products: " + cart.get("totalQty").getAsString());
        holder.totalPrice.setText("Total Price: " + cart.get("totalPrice").getAsString());

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderDetailsFragment fragment = new MyOrderDetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("itemss", itemss.toString());
                bundle.putString("name", object.get("name").getAsString());
                bundle.putString("address", object.get("address").getAsString());
                bundle.putString("itemCount", cart.get("totalQty").getAsString());
                bundle.putString("totalPrice", cart.get("totalPrice").getAsString());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return placedOrder.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantiy;
        TextView totalPrice;
        TextView details;
        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            quantiy = view.findViewById(R.id.item_quantity);
            totalPrice = view.findViewById(R.id.total_price);
            details = view.findViewById(R.id.details);
        }
    }
}