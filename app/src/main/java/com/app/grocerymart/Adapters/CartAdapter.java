package com.app.grocerymart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.grocerymart.Cart;
import com.app.grocerymart.Database.CartData;
import com.app.grocerymart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    public ArrayList<String> itemIds;
    private DisplayImageOptions options;
//    public ArrayList<String> product;
//    public ArrayList<String> amount;
//    public ArrayList<String> price;
//    public ArrayList<Integer> qty;
    CartData mydb;
    //private SharedPreferences.Editor myEdit;

//    public CartAdapter(Context ctx, ArrayList<String> pr, ArrayList<String> am, ArrayList<String> pric, ArrayList<Integer> qty) {
//        this.context = ctx;
//        this.product = pr;
//        this.amount = am;
//        this.price = pric;
//        this.qty = qty;
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
//
//        myEdit = sharedPreferences.edit();
//
//        Log.e("subCat", product + "\n" + amount + "\n" + price);
//    }

    public CartAdapter(Context cart, ArrayList<String> itemIds) {
        this.context = cart;
        this.itemIds = itemIds;
        mydb = new CartData(cart);

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.broken_image)
                .showImageOnFail(R.drawable.broken_image)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_for_cart, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Cursor items = mydb.getItem(itemIds.get(position));
        Log.e("cartcur", items.getString(0) + "");
        holder.ia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.amount < 10){
                    holder.amount += 1;
                    holder.qty.setText(holder.amount + "");
                    if(mydb.insertItem(items.getString(0),
                            items.getString(1),
                            items.getString(2),
                            items.getString(3),
                            items.getString(4),
                            items.getString(5),
                            holder.amount + "")){

                    }else{
                        mydb.updateItem(items.getString(0),
                                items.getString(1),
                                items.getString(2),
                                items.getString(3),
                                items.getString(4),
                                items.getString(5),
                                holder.amount + "");
                    }

                    holder.totip.setText(String.format("%.2f",
                            Double.parseDouble(String.valueOf(holder.amount)) * Double.parseDouble(items.getString(5))));

                    if(holder.amount == 1){
                        holder.im.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                    }else{
                        holder.im.setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
                    }
                    Cart.updatePrice();
                    //myEdit.putInt(items.get(position).getAsJsonObject().get("title").getAsString(), holder.amount);
                    //myEdit.apply();
                }
            }
        });

        holder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.amount > 0){
                    holder.amount -= 1;

                    holder.qty.setText(holder.amount + "");
                    if(mydb.insertItem(items.getString(0),
                            items.getString(1),
                            items.getString(2),
                            items.getString(3),
                            items.getString(4),
                            items.getString(5),
                            holder.amount + "")){

                    }else{
                        mydb.updateItem(items.getString(0),
                                items.getString(1),
                                items.getString(2),
                                items.getString(3),
                                items.getString(4),
                                items.getString(5),
                                holder.amount + "");
                    }

                    holder.totip.setText(String.format("%.2f",
                            Double.parseDouble(String.valueOf(holder.amount)) * Double.parseDouble(items.getString(5))));

                    Cart.updatePrice();

                    if(holder.amount == 0){
                        mydb.deleteItem(items.getString(0));
                        Cart.updateView();
                        return;
                    }

                    if(holder.amount == 1){
                        holder.im.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
                    }else{
                        holder.im.setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
                    }
                    //myEdit.putInt(product.get(position), holder.amount);
                   // myEdit.apply();
                }
            }
        });


        holder.tv.setText(items.getString(2));
        holder.am.setText("Amount: " + items.getString(4));
        holder.pr.setText("Rs.: " + items.getString(5));
        holder.qty.setText(items.getString(6) + "");
        holder.totip.setText(String.format("%.2f",
                Double.parseDouble(items.getString(6)) * Double.parseDouble(items.getString(5))));

        holder.amount = Integer.parseInt(items.getString(6));


        if(holder.amount == 1){
            holder.im.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
        }else{
            holder.im.setImageDrawable(context.getResources().getDrawable(R.drawable.minus));
        }
        ImageLoader.getInstance().displayImage(items.getString(1),
                holder.productImage, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        String message = null;
                        switch (failReason.getType()) {
                            case IO_ERROR:
                                message = "Input/Output error";
                                break;
                            case DECODING_ERROR:
                                message = "Image can't be decoded";
                                break;
                            case NETWORK_DENIED:
                                message = "Downloads are denied";
                                break;
                            case OUT_OF_MEMORY:
                                message = "Out Of Memory error";
                                break;
                            case UNKNOWN:
                                message = "Unknown error";
                                break;
                        }
                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }
                });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemIds.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView am;
        TextView pr;
        TextView totip;
        TextView qty;
        ImageView productImage;
        ImageView ia;
        ImageView im;
        int amount=0;
        ViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.product);
            am = view.findViewById(R.id.amount);
            qty = view.findViewById(R.id.qty);
            totip = view.findViewById(R.id.totitemprice);
            pr = view.findViewById(R.id.price);
            productImage = view.findViewById(R.id.productImage);
            ia = view.findViewById(R.id.plus);
            im = view.findViewById(R.id.minus);
            amount = 0;
        }
    }
}