package it.univaq.disim.mwt.android_native_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.EpisodeActivity;
import it.univaq.disim.mwt.android_native_app.R;
import it.univaq.disim.mwt.android_native_app.model.Episode;

public class RecyclerViewEpisodeAdapter extends RecyclerView.Adapter<RecyclerViewEpisodeAdapter.ViewHolder> {

    private Context context;
    private List<Episode> data;

    public RecyclerViewEpisodeAdapter(Context context, List<Episode> data) {
        this.context = context;
        this.data = data;
        if(this.data == null)
            this.data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.title.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        MaterialCardView materialCardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.episode_title);
            materialCardView = itemView.findViewById(R.id.episode_card);

            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EpisodeActivity.class);
                    intent.putExtra("episodes", (Serializable) data);
                    intent.putExtra("chosen_episode", data.get(getBindingAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
