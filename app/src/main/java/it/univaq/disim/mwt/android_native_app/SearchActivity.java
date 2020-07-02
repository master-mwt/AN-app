package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewTvShowCardAdapter;
import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;
import it.univaq.disim.mwt.android_native_app.utils.Network;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<TvShowPreview> data = new ArrayList<>();
    private RecyclerViewTvShowCardAdapter recyclerViewTvShowCardAdapter;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private int page;
    private String scroll_query;

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
        searchView = findViewById(R.id.search_searchview);

        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.search_tv_shows_recycle_view);

        nestedScrollView = findViewById(R.id.nested_scroll_view_search);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                page = 1;
                data.clear();

                /* Check network connection */
                Network.checkAvailability(getApplicationContext(), getSupportFragmentManager());

                TMDB.requestRemoteTvShowsSearch(getApplicationContext(), query , page);
                scroll_query = query;

                // nestedScrollView.scrollTo(0, 0);
                nestedScrollView.fullScroll(View.FOCUS_UP);
                nestedScrollView.smoothScrollTo(0, 0);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        page++;

                        /* Check network connection */
                        Network.checkAvailability(getApplicationContext(), getSupportFragmentManager());

                        TMDB.requestRemoteTvShowsSearch(getApplicationContext(), scroll_query , page);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.drawer_header_user_name);
        userName.setText((FirebaseAuth.getInstance().getCurrentUser() != null) ? FirebaseAuth.getInstance().getCurrentUser().getEmail(): "guest");
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
                intent = new Intent(this, ExploreActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_collection:
                drawerLayout.closeDrawers();
                intent = new Intent(this, UserCollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_auth:
                drawerLayout.closeDrawers();
                intent = new Intent(this, AuthActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_info:
                drawerLayout.closeDrawers();
                intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_settings:
                drawerLayout.closeDrawers();
                intent = new Intent(this, SettingsActivity.class);
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
