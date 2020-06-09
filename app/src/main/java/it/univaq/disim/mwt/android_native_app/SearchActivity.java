package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewTvShowCardAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
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

        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        drawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.menu_item_search:
                drawerLayout.closeDrawers();
                break;
            case R.id.menu_item_explore:
                drawerLayout.closeDrawers();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_collection:
                drawerLayout.closeDrawers();
                intent = new Intent(this, UserCollectionActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
