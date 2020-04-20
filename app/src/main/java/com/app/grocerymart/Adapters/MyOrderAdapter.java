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

import com.app.grocerymart.Cart;
import com.app.grocerymart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> product;
    private ArrayList<String> qty;
    private ArrayList<String> status;
    private ArrayList<String> image;
    private ArrayList<String> placedDate;
    private DisplayImageOptions options;

    public MyOrderAdapter(Context ctx, ArrayList<String> pr, ArrayList<String> qty, ArrayList<String> status
            , ArrayList<String> imag, ArrayList<String> pd) {
        this.context = ctx;
        this.product = pr;
        this.qty = qty;
        this.status = status;
        this.image = imag;
        this.placedDate = pd;

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
        View view= LayoutInflater.from(context).inflate(R.layout.item_for_my_order, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.name.setText(product.get(position));
        holder.quantiy.setText("Quantity: " + qty.get(position));
        holder.status.setText("Status: " + status.get(position));
        holder.date.setText("Placed on: "+placedDate.get(position));

        ImageLoader.getInstance().displayImage(image.get(position), holder.item_img, options, new SimpleImageLoadingListener() {
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
        return product.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView status;
        TextView quantiy;
        TextView date;
        ImageView item_img;
        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            status = view.findViewById(R.id.item_status);
            quantiy = view.findViewById(R.id.item_quantity);
            date = view.findViewById(R.id.placed_date);
            item_img = view.findViewById(R.id.item_image);
        }
    }
}