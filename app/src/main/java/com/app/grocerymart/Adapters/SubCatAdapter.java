package com.app.grocerymart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.grocerymart.R;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> product;
    private ArrayList<String> amount;
    private ArrayList<String> price;
    private SharedPreferences.Editor myEdit;

    public SubCatAdapter(Context ctx, ArrayList<String> pr, ArrayList<String> am, ArrayList<String> pric) {
        this.context = ctx;
        this.product = pr;
        this.amount = am;
        this.price = pric;
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        myEdit = sharedPreferences.edit();

        Log.e("subCat", product + "\n" + amount + "\n" + price);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_for_sub_category, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.ia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.amount < 10){
                    holder.amount += 1;
                    holder.qty.setText(holder.amount + "");

                    myEdit.putInt(product.get(position), holder.amount);
                    myEdit.apply();
                }
            }
        });

        holder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.amount > 0){
                    holder.amount -= 1;
                    holder.qty.setText(holder.amount + "");

                    myEdit.putInt(product.get(position), holder.amount);
                    myEdit.apply();
                }
            }
        });

        holder.tv.setText(product.get(position));
        holder.am.setText("Amount: " + amount.get(position));
        holder.pr.setText("Rs.: " + price.get(position));

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView am;
        TextView pr;
        TextView qty;
        ImageView ia;
        ImageView im;
        int amount=0;
        ViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.product);
            am = view.findViewById(R.id.amount);
            qty = view.findViewById(R.id.qty);
            pr = view.findViewById(R.id.price);
            ia = view.findViewById(R.id.plus);
            im = view.findViewById(R.id.minus);
            amount = 0;
        }
    }
}