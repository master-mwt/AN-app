package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewTvShowCardAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;

public class ExploreTvShowsTopRatedFragment extends Fragment {
    private ArrayList<TvShowPreview> data = new ArrayList<>();
    private RecyclerViewTvShowCardAdapter recyclerViewTvShowCardAdapter;
    private RecyclerView recyclerView;
    private int page;
    private ProgressBar progressBar;
    private boolean retrievedAlready;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOWS_TOP_RATED:
                        progressBar.setVisibility(View.INVISIBLE);
                        data.addAll(intent.<TvShowPreview>getParcelableArrayListExtra(DataParserService.EXTRA));
                        recyclerViewTvShowCardAdapter.notifyDataSetChanged();
                        retrievedAlready = true;
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public ExploreTvShowsTopRatedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        page = 1;
        data.clear();
        retrievedAlready = false;

        recyclerViewTvShowCardAdapter = new RecyclerViewTvShowCardAdapter(getContext(), data);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recyclerViewTvShowCardAdapter);

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOWS_TOP_RATED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    progressBar.setVisibility(View.VISIBLE);
                    page++;
                    TMDB.requestRemoteTvShowsTopRated(getContext(), page);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!retrievedAlready){
            TMDB.requestRemoteTvShowsTopRated(getContext(), page);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_tv_shows_top_rated, container, false);

        recyclerView = view.findViewById(R.id.explore_tv_shows_top_rated_recycle_view);
        progressBar = view.findViewById(R.id.recycler_view_progress);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
