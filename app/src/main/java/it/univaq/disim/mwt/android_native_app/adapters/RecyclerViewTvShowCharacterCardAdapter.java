package it.univaq.disim.mwt.android_native_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.univaq.disim.mwt.android_native_app.R;
import it.univaq.disim.mwt.android_native_app.model.TvShowCharacter;
import it.univaq.disim.mwt.android_native_app.utils.VolleyRequest;

public class RecyclerViewTvShowCharacterCardAdapter extends RecyclerView.Adapter<RecyclerViewTvShowCharacterCardAdapter.ViewHolder> {

    private Context context;
    private List<TvShowCharacter> data;

    public RecyclerViewTvShowCharacterCardAdapter(Context context, List<TvShowCharacter> data) {
        this.context = context;
        this.data = data;
        if(this.data == null)
            this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_card_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.title.setText(data.get(position).getName());

        String imageUrl = data.get(position).getProfile_path();

        if(imageUrl != null && !"".equals(imageUrl) && !"null".equals(imageUrl)){

            String requestUrl = context.getString(R.string.tmdb_image_baselink) + imageUrl;

            VolleyRequest.getInstance(context).getImageLoader().get(requestUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.cardImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.getLogger(RecyclerViewTvShowCharacterCardAdapter.class.getName()).log(Level.SEVERE, (error.getCause() != null) ? error.getCause().getMessage() : error.getMessage());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView cardImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.recycler_view_card_adapter_title);
            cardImage = itemView.findViewById(R.id.recycler_view_card_adapter_image);

        }
    }
}
