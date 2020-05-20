package com.app.grocerymart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.app.grocerymart.Database.CartData;
import com.app.grocerymart.R;
import com.google.gson.JsonArray;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.ViewHolder> {

    private Context context;
    private CartData mydb ;
//    private ArrayList<String> product;
//    private ArrayList<String> amount;
//    private ArrayList<String> price;
    //private SharedPreferences.Editor myEdit;
    private JsonArray items;
    private DisplayImageOptions options;

    public SubCatAdapter(Context ctx, ArrayList<String> pr, ArrayList<String> am, ArrayList<String> pric) {
        this.context = ctx;
//        this.product = pr;
//        this.amount = am;
//        this.price = pric;
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        //myEdit = sharedPreferences.edit();
        mydb = new CartData(ctx);

//        Log.e("subCat", product + "\n" + amount + "\n" + price);
    }

    public SubCatAdapter(Context context, JsonArray result) {
        this.context = context;
        this.items = result;

        mydb = new CartData(context);

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

                    if(mydb.insertItem(items.get(position).getAsJsonObject().get("_id").getAsString(),
                            items.get(position).getAsJsonObject().get("imagePath").getAsString(),
                            items.get(position).getAsJsonObject().get("title").getAsString(),
                            items.get(position).getAsJsonObject().get("description").getAsString(),
                            items.get(position).getAsJsonObject().get("quantity").getAsString(),
                            items.get(position).getAsJsonObject().get("price").getAsString(),
                            holder.amount + "")){

                    }else{
                        mydb.updateItem(items.get(position).getAsJsonObject().get("_id").getAsString(),
                                items.get(position).getAsJsonObject().get("imagePath").getAsString(),
                                items.get(position).getAsJsonObject().get("title").getAsString(),
                                items.get(position).getAsJsonObject().get("description").getAsString(),
                                items.get(position).getAsJsonObject().get("quantity").getAsString(),
                                items.get(position).getAsJsonObject().get("price").getAsString(),
                                holder.amount+"");
                    }
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
                    if(mydb.insertItem(items.get(position).getAsJsonObject().get("_id").getAsString(),
                            items.get(position).getAsJsonObject().get("imagePath").getAsString(),
                            items.get(position).getAsJsonObject().get("title").getAsString(),
                            items.get(position).getAsJsonObject().get("description").getAsString(),
                            items.get(position).getAsJsonObject().get("quantity").getAsString(),
                            items.get(position).getAsJsonObject().get("price").getAsString(),
                            holder.amount+"")) {

                    }else{
                        mydb.updateItem(items.get(position).getAsJsonObject().get("_id").getAsString(),
                                items.get(position).getAsJsonObject().get("imagePath").getAsString(),
                                items.get(position).getAsJsonObject().get("title").getAsString(),
                                items.get(position).getAsJsonObject().get("description").getAsString(),
                                items.get(position).getAsJsonObject().get("quantity").getAsString(),
                                items.get(position).getAsJsonObject().get("price").getAsString(),
                                holder.amount+"");
                    }
                    //myEdit.putInt(product.get(position), holder.amount);
                    //myEdit.apply();
                }else if(holder.amount == 0){
                    mydb.deleteItem(items.get(position).getAsJsonObject().get("_id").getAsString());
                }
            }
        });

        holder.tv.setText(items.get(position).getAsJsonObject().get("title").getAsString());
        holder.am.setText("Amount: " + items.get(position).getAsJsonObject().get("quantity").getAsString());
        holder.pr.setText("Rs.: " + items.get(position).getAsJsonObject().get("price").getAsString());

        ImageLoader.getInstance().displayImage(items.get(position).getAsJsonObject().get("imagePath").getAsString(),
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
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        TextView am;
        TextView pr;
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
            pr = view.findViewById(R.id.price);
            productImage = view.findViewById(R.id.productImage);
            ia = view.findViewById(R.id.plus);
            im = view.findViewById(R.id.minus);
            amount = 0;
        }
    }
}