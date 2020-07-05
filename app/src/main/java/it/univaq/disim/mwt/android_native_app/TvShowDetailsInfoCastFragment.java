package it.univaq.disim.mwt.android_native_app;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewTvShowCharacterCardAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowCharacter;
import it.univaq.disim.mwt.android_native_app.model.TvShowDetails;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;
import it.univaq.disim.mwt.android_native_app.services.UserCollectionService;
import it.univaq.disim.mwt.android_native_app.utils.Network;

public class TvShowDetailsInfoCastFragment extends Fragment {
    private static final String ARG_TV_SHOW_DETAILS = "tv_show_details";

    private TextView name;
    private TextView voteRating;
    private RatingBar ratingBar;
    private TextView overview;
    private TextView originalLanguage;
    private TextView status;
    private TextView type;
    private TextView originCountry;
    private TextView languages;
    private TextView genres;
    private TextView lastEpisode;
    private TextView nextEpisode;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MaterialButton collectionButton;
    private TvShowDetails tvShowDetails;
    private ArrayList<TvShowCharacter> data = new ArrayList<>();
    private RecyclerViewTvShowCharacterCardAdapter recyclerViewTvShowCharacterCardAdapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_CREDITS:
                        progressBar.setVisibility(View.INVISIBLE);
                        data.addAll(intent.<TvShowCharacter>getParcelableArrayListExtra(DataParserService.EXTRA));
                        recyclerViewTvShowCharacterCardAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public TvShowDetailsInfoCastFragment() {
        // Required empty public constructor
    }

    public static TvShowDetailsInfoCastFragment newInstance(TvShowDetails tvShowDetails) {
        TvShowDetailsInfoCastFragment fragment = new TvShowDetailsInfoCastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TV_SHOW_DETAILS, tvShowDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tvShowDetails = (TvShowDetails) getArguments().getSerializable(ARG_TV_SHOW_DETAILS);
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onStart() {
        super.onStart();

        /* Check network connection */
        Network.checkAvailability(getContext(), getFragmentManager());

        name.setText((tvShowDetails.getName() != null) ? tvShowDetails.getName() : " - ");
        voteRating.setText(String.format(getString(R.string.vote_structure), tvShowDetails.getVote_average(), tvShowDetails.getVote_count()));
        if(tvShowDetails.getVote_average() == 0){
            ratingBar.setRating(0);
        } else {
            ratingBar.setRating((float) (tvShowDetails.getVote_average() * 5) / 10);
        }
        overview.setText((tvShowDetails.getOverview() != null) ? tvShowDetails.getOverview() : " - ");
        originalLanguage.setText((tvShowDetails.getOriginal_language() != null) ? getString(R.string.original_language) + tvShowDetails.getOriginal_language() : getString(R.string.original_language) + " - ");
        status.setText((tvShowDetails.getStatus() != null) ? getString(R.string.status) + tvShowDetails.getStatus() : getString(R.string.status) + " - ");
        type.setText((tvShowDetails.getType() != null) ? getString(R.string.type) + tvShowDetails.getType() : getString(R.string.type) + " - ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            originCountry.setText((tvShowDetails.getOrigin_country() != null) ? getString(R.string.original_countries) + tvShowDetails.getOrigin_country().stream().collect(Collectors.joining(", ")) : getString(R.string.original_countries) + " - ");
            languages.setText((tvShowDetails.getLanguages() != null) ? getString(R.string.languages) + tvShowDetails.getLanguages().stream().collect(Collectors.joining(", ")) : getString(R.string.languages) + " - ");
            genres.setText((tvShowDetails.getGenres() != null) ? getString(R.string.genres) + tvShowDetails.getGenres().stream().collect(Collectors.joining(", ")) : getString(R.string.genres) + " - ");
        } else {
            originCountry.setText((tvShowDetails.getOrigin_country() != null) ? getString(R.string.original_countries) + tvShowDetails.getOrigin_country() : getString(R.string.original_countries) + " - ");
            languages.setText((tvShowDetails.getLanguages() != null) ? getString(R.string.languages) + tvShowDetails.getLanguages() : getString(R.string.languages) + " - ");
            genres.setText((tvShowDetails.getGenres() != null) ? getString(R.string.genres) + tvShowDetails.getGenres() : getString(R.string.genres) + " - ");
        }
        lastEpisode.setText((tvShowDetails.getLast_episode_to_air() != null) ? getString(R.string.last_episode) + tvShowDetails.getLast_episode_to_air() : getString(R.string.last_episode) + " - ");
        nextEpisode.setText((tvShowDetails.getNext_episode_to_air() != null) ? getString(R.string.next_episode) + tvShowDetails.getNext_episode_to_air() : getString(R.string.next_episode) + " - ");

        if(tvShowDetails.isIn_collection()){
            collectionButton.setText(getString(R.string.tvshow_details_button_remove_from_collection));
            collectionButton.setBackgroundColor(getResources().getColor(R.color.colorMarked, getContext().getTheme()));
        } else {
            collectionButton.setText(getString(R.string.tvshow_details_button_add_to_collection));
            collectionButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));
        }

        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvShowDetails.isIn_collection()){
                    TvShowPreview data = new TvShowPreview();
                    data.setTv_show_id(tvShowDetails.getTv_show_id());

                    Intent intent = new Intent(getContext(), UserCollectionService.class);
                    intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_DELETE_TV_SHOW_FROM_COLLECTION);
                    intent.putExtra(UserCollectionService.KEY_DATA, data);
                    Objects.requireNonNull(getContext()).startService(intent);

                    tvShowDetails.setIn_collection(false);
                    collectionButton.setText(getString(R.string.tvshow_details_button_add_to_collection));
                    collectionButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary, getContext().getTheme()));
                } else {
                    TvShowPreview data = new TvShowPreview();
                    data.setTv_show_id(tvShowDetails.getTv_show_id());
                    data.setName(tvShowDetails.getName());
                    data.setPoster_path(tvShowDetails.getPoster_path());

                    Intent intent = new Intent(getContext(), UserCollectionService.class);
                    intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_SAVE_TV_SHOW_TO_COLLECTION);
                    intent.putExtra(UserCollectionService.KEY_DATA, data);
                    Objects.requireNonNull(getContext()).startService(intent);

                    tvShowDetails.setIn_collection(true);
                    collectionButton.setText(getString(R.string.tvshow_details_button_remove_from_collection));
                    collectionButton.setBackgroundColor(getResources().getColor(R.color.colorMarked, getContext().getTheme()));
                }
            }
        });

        data.clear();

        recyclerViewTvShowCharacterCardAdapter = new RecyclerViewTvShowCharacterCardAdapter(getContext(), data);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recyclerViewTvShowCharacterCardAdapter);

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_CREDITS);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        TMDB.requestRemoteTvShowCredits(getContext(), tvShowDetails.getTv_show_id());

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show_details_info_cast, container, false);

        name = view.findViewById(R.id.name);

        voteRating = view.findViewById(R.id.vote_rating);

        ratingBar = view.findViewById(R.id.rating_bar);

        overview = view.findViewById(R.id.overview);

        originalLanguage = view.findViewById(R.id.original_language);

        status = view.findViewById(R.id.status);

        type = view.findViewById(R.id.type);

        originCountry = view.findViewById(R.id.origin_country);

        languages = view.findViewById(R.id.languages);

        genres = view.findViewById(R.id.genres);

        lastEpisode = view.findViewById(R.id.last_episode);

        nextEpisode = view.findViewById(R.id.next_episode);

        recyclerView = view.findViewById(R.id.cast_recycle_view);

        progressBar = view.findViewById(R.id.cast_recycler_view_progress);

        collectionButton = view.findViewById(R.id.collection_button);

        // Inflate the layout for this fragment
        return view;
    }
}
