package com.app.grocerymart.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class MyOrderItemAdapter extends RecyclerView.Adapter<MyOrderItemAdapter.ViewHolder> {

    private Context context;
    private JsonArray items;
    private DisplayImageOptions options;

    public MyOrderItemAdapter(Context ctx, JsonArray placedOrder) {
        this.context = ctx;
        this.items = placedOrder;

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

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
        View view= LayoutInflater.from(context).inflate(R.layout.item_of_item_for_my_order, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv.setText(items.get(position).getAsJsonObject().get("item").getAsJsonObject().get("title").getAsString());
        holder.am.setText("Amount: " + items.get(position).getAsJsonObject().get("item").getAsJsonObject().get("quantity").getAsString());
        holder.pr.setText("Rs.: " + items.get(position).getAsJsonObject().get("item").getAsJsonObject().get("price").getAsString());
        holder.quantity.setText("Quantity: " + items.get(position).getAsJsonObject().get("qty").getAsString());
        holder.totalPrice.setText("Total Price: " + items.get(position).getAsJsonObject().get("price").getAsString());

        ImageLoader
                .getInstance()
                .displayImage(items.get(position).getAsJsonObject().get("item").getAsJsonObject().get("imagePath").getAsString(),
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
        TextView quantity;
        TextView totalPrice;
        ImageView productImage;
        ViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.product);
            am = view.findViewById(R.id.amount);
            pr = view.findViewById(R.id.price);
            quantity = view.findViewById(R.id.quantity);
            totalPrice = view.findViewById(R.id.total_price);
            productImage = view.findViewById(R.id.productImage);
        }
    }
}