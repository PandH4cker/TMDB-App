package com.tmdbapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tmdbapp.R;
import com.tmdbapp.api.APIClient;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private static final String TAG = CastAdapter.class.getSimpleName();

    private List<String> actorsFullPosterPaths;
    private Context context;

    public CastAdapter(Context context, List<String> actorsFullPosterPaths) {
        this.actorsFullPosterPaths = actorsFullPosterPaths;
        this.context = context;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.cast_item, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {
        String posterPath = APIClient.getFullPosterPath(this.actorsFullPosterPaths.get(position));
        //Log.d(TAG, posterPath);
        if (!posterPath.equals("")) holder.bindTo(posterPath);
    }

    @Override
    public int getItemCount() {
        return this.actorsFullPosterPaths.size();
    }

    class CastViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout listItem;
        private final CircleImageView circleImageView;

        CastViewHolder(View itemView) {
            super(itemView);
            this.listItem = itemView.findViewById(R.id.list_item_cast);
            this.circleImageView = itemView.findViewById(R.id.rounded_icon);
        }

        private void bindTo(String posterPath) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    LayerDrawable layer =  (LayerDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.cast_image, null);
                    if (layer != null) {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
                        boolean testFactor = layer.setDrawableByLayerId(R.id.image_cast_item, bitmapDrawable);
                        circleImageView.setImageDrawable(layer);
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.d(TAG, "Error Picasso get bitmap: " + e.getMessage());
                    Toast.makeText(context.getApplicationContext(), "Error Picasso get bitmap: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.get().load(posterPath).into(target);
        }
    }
}
