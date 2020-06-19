package it.univaq.disim.mwt.android_native_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.EpisodeActivity;
import it.univaq.disim.mwt.android_native_app.R;
import it.univaq.disim.mwt.android_native_app.model.Episode;
import it.univaq.disim.mwt.android_native_app.services.UserCollectionService;

public class RecyclerViewEpisodeAdapter extends RecyclerView.Adapter<RecyclerViewEpisodeAdapter.ViewHolder> {

    private Context context;
    private List<Episode> data;
    private boolean isTvShowInCollection;

    public RecyclerViewEpisodeAdapter(Context context, List<Episode> data, boolean isTvShowInCollection) {
        this.context = context;
        this.data = data;
        if(this.data == null)
            this.data = new ArrayList<>();
        this.isTvShowInCollection = isTvShowInCollection;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.title.setText(data.get(position).getName());
        if(this.isTvShowInCollection){
            holder.markEpisodeButtonInEpisodeList.setEnabled(true);
            if(data.get(position).isWatched()){
                System.out.println("watched: " + data.get(position).getName());
                holder.markEpisodeButtonInEpisodeList.setBackgroundColor(context.getResources().getColor(R.color.colorMarked, context.getTheme()));
            } else {
                holder.markEpisodeButtonInEpisodeList.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark, context.getTheme()));
            }
            holder.markEpisodeButtonInEpisodeList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Episode episode = data.get(position);

                    if(episode.isWatched()){

                        Intent intent = new Intent(context, UserCollectionService.class);
                        intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_DELETE_EPISODE_FROM_COLLECTION);
                        intent.putExtra(UserCollectionService.KEY_DATA, episode);
                        context.startService(intent);

                        episode.setWatched(false);
                        holder.markEpisodeButtonInEpisodeList.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark, context.getTheme()));
                        notifyDataSetChanged();
                    } else {

                        Intent intent = new Intent(context, UserCollectionService.class);
                        intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_SAVE_EPISODE_TO_COLLECTION);
                        intent.putExtra(UserCollectionService.KEY_DATA, episode);
                        context.startService(intent);

                        episode.setWatched(true);
                        holder.markEpisodeButtonInEpisodeList.setBackgroundColor(context.getResources().getColor(R.color.colorMarked, context.getTheme()));
                        notifyDataSetChanged();
                    }
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
        MaterialCardView materialCardView;
        MaterialButton markEpisodeButtonInEpisodeList;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.episode_title);
            materialCardView = itemView.findViewById(R.id.episode_card);
            markEpisodeButtonInEpisodeList = itemView.findViewById(R.id.mark_episode_button_in_episode_list);

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
