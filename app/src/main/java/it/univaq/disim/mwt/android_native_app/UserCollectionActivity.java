package it.univaq.disim.mwt.android_native_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;

public class UserCollectionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private TextView emptyList;
    private List<TvShowPreview> collection = new ArrayList<>();

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO: empty collection message handling and collection itself

        if(collection.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            emptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.INVISIBLE);
        }
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
            default:
                break;
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
