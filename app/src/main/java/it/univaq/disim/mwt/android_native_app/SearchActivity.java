package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewTvShowCardAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<TvShowPreview> data = new ArrayList<>();
    private RecyclerViewTvShowCardAdapter recyclerViewTvShowCardAdapter;
    private RecyclerView recyclerView;
    private int page;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOWS_SEARCH:
                        data.addAll(intent.<TvShowPreview>getParcelableArrayListExtra(DataParserService.EXTRA));
                        recyclerViewTvShowCardAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_tv_shows_recycle_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        page = 1;
        data.clear();

        recyclerViewTvShowCardAdapter = new RecyclerViewTvShowCardAdapter(this, data);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(recyclerViewTvShowCardAdapter);

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOWS_SEARCH);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, intentFilter);

        TMDB.requestRemoteTvShowsSearch(getApplicationContext(), "breaking" , page);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    TMDB.requestRemoteTvShowsSearch(getApplicationContext(), "breaking" , page);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
