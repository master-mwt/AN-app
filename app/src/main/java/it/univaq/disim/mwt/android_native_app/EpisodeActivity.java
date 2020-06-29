package it.univaq.disim.mwt.android_native_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.model.Episode;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class EpisodeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Episode chosenEpisode;
    private List<Episode> episodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

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

        viewPager = findViewById(R.id.main_viewpager);
        tabLayout = findViewById(R.id.main_tab_layout);

        episodes = (List<Episode>) getIntent().getSerializableExtra("episodes");
        chosenEpisode = (Episode) getIntent().getSerializableExtra("chosen_episode");
    }

    @Override
    protected void onStart() {
        super.onStart();

        EpisodeActivity.ViewPagerAdapter viewPagerAdapter = new EpisodeActivity.ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        int index = 0;

        for(int i = 0; i < episodes.size(); i++){
            Episode episode = episodes.get(i);
            if(episode.equals(chosenEpisode)){
                index = i;
            }

            EpisodeFragment fragment = EpisodeFragment.newInstance(episode);
            viewPagerAdapter.addFragment(fragment, getString(R.string.fragment_episode_initial_title) + episode.getEpisode_number());
        }

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(index).select();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.drawer_header_user_name);
        userName.setText((FirebaseAuth.getInstance().getCurrentUser() != null) ? FirebaseAuth.getInstance().getCurrentUser().getEmail(): "guest");
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
