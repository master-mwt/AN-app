package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.Season;
import it.univaq.disim.mwt.android_native_app.model.TvShowDetails;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;
import it.univaq.disim.mwt.android_native_app.utils.VolleyRequest;

public class TvShowDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TvShowDetails tvShowDetails;
    private ImageView imageView;

    private TvShowDetailsSeasonFragment tvShowDetailsSeasonFragment;
    private TvShowDetailsInfoCastFragment tvShowDetailsInfoCastFragment;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOW_DETAILS:
                        tvShowDetails = (TvShowDetails) intent.getSerializableExtra(DataParserService.EXTRA);
                        setTvShowImage(tvShowDetails.getPoster_path());

                        tvShowDetailsSeasonFragment = TvShowDetailsSeasonFragment.newInstance((ArrayList<Season>) tvShowDetails.getSeasons());
                        tvShowDetailsInfoCastFragment = TvShowDetailsInfoCastFragment.newInstance(tvShowDetails);

                        tabLayout.setupWithViewPager(viewPager);

                        TvShowDetailsActivity.ViewPagerAdapter viewPagerAdapter = new TvShowDetailsActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);
                        viewPagerAdapter.addFragment(tvShowDetailsSeasonFragment, getString(R.string.fragment_tvshow_details_season_title));
                        viewPagerAdapter.addFragment(tvShowDetailsInfoCastFragment, getString(R.string.fragment_tvshow_details_info_cast_title));
                        viewPager.setAdapter(viewPagerAdapter);

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
        setContentView(R.layout.activity_tv_show_details);

        toolbar = findViewById(R.id.tv_show_details_toolbar);
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

        viewPager = findViewById(R.id.main_viewpager);
        tabLayout = findViewById(R.id.main_tab_layout);

        imageView = findViewById(R.id.tv_show_details_toolbar_image);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOW_DETAILS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);

        long tv_show_id = getIntent().getLongExtra("data", -1);

        if(tv_show_id != -1){
            TMDB.requestRemoteTvShowDetails(this, tv_show_id);
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void setTvShowImage(String imageUrl){

        String baseUrl = getString(R.string.tmdb_image_baselink);

        if(imageUrl != null && !"".equals(imageUrl) && !"null".equals(imageUrl)){
            String requestUrl = baseUrl + imageUrl;

            VolleyRequest.getInstance(getApplicationContext()).getImageLoader().get(requestUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.w(TvShowDetailsActivity.class.getName(), (error.getCause() != null) ? error.getCause().getMessage() : error.getMessage());
                }
            });
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentListTitles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentListTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentListTitles.get(position);
        }
    }
}
