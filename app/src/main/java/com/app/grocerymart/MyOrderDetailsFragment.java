package com.app.grocerymart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.grocerymart.Adapters.MyOrderAdapter;
import com.app.grocerymart.Adapters.MyOrderItemAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MyOrderDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_order_items, container, false);

        TextView name = view.findViewById(R.id.name);
        TextView address = view.findViewById(R.id.address);
        TextView itemCount = view.findViewById(R.id.item_count);
        TextView totalPrice = view.findViewById(R.id.total_price);
        String itemss = getArguments().getString("itemss");
        String strName = getArguments().getString("name");
        String strAddress = getArguments().getString("address");
        String strItemCount = getArguments().getString("itemCount");
        String strTotalPrice = getArguments().getString("totalPrice");

        name.setText(strName);
        address.setText(strAddress);
        itemCount.setText("Number of Items: " + strItemCount);
        totalPrice.setText("Total Price: " + strTotalPrice);
        JsonParser parser = new JsonParser();
        JsonArray items = parser.parse(itemss).getAsJsonArray();

        final RecyclerView recyclerView = view.findViewById(R.id.items);

        MyOrderItemAdapter myOrderAdapter = new MyOrderItemAdapter(getContext(), items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myOrderAdapter);

        return view;
    }
}
