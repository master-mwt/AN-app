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
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.adapters.RecyclerViewTvShowCardAdapter;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.services.UserCollectionService;

public class UserCollectionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private RecyclerViewTvShowCardAdapter recyclerViewTvShowCardAdapter;
    private TextView emptyList;
    private List<TvShowPreview> collection = new ArrayList<>();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case UserCollectionService.FILTER_GET_USER_COLLECTION:
                        ArrayList<TvShowPreview> collectionList = intent.<TvShowPreview>getParcelableArrayListExtra(UserCollectionService.EXTRA);

                        if(collectionList != null){
                            collection.clear();
                            collection.addAll(collectionList);
                            recyclerViewTvShowCardAdapter.notifyDataSetChanged();
                        }

                        // TODO: Empty collection message
                        if(collection.isEmpty()){
                            recyclerView.setVisibility(View.INVISIBLE);
                            emptyList.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyList.setVisibility(View.INVISIBLE);
                        }

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
        setContentView(R.layout.activity_user_collection);

        toolbar = findViewById(R.id.main_toolbar);
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

        recyclerView = findViewById(R.id.collection_tv_shows_recycle_view);

        emptyList = findViewById(R.id.empty_list);

        recyclerViewTvShowCardAdapter = new RecyclerViewTvShowCardAdapter(this, collection);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(recyclerViewTvShowCardAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(UserCollectionService.FILTER_GET_USER_COLLECTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

        Intent intent = new Intent(this, UserCollectionService.class);
        intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_GET_USER_COLLECTION);
        startService(intent);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.drawer_header_user_name);
        userName.setText((FirebaseAuth.getInstance().getCurrentUser() != null) ? FirebaseAuth.getInstance().getCurrentUser().getEmail(): "guest");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.menu_item_search:
                drawerLayout.closeDrawers();
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_explore:
                drawerLayout.closeDrawers();
                intent = new Intent(this, ExploreActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_collection:
                drawerLayout.closeDrawers();
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
