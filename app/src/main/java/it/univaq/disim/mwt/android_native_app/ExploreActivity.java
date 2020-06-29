package it.univaq.disim.mwt.android_native_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
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

import it.univaq.disim.mwt.android_native_app.utils.Notification;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ExploreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ExploreTvShowsPopularFragment exploreTvShowsPopularFragment;
    private ExploreTvShowsTopRatedFragment exploreTvShowsTopRatedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_navigation_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_view_open,
                R.string.navigation_view_close
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.main_viewpager);
        tabLayout = findViewById(R.id.main_tab_layout);

        exploreTvShowsPopularFragment = new ExploreTvShowsPopularFragment();
        exploreTvShowsTopRatedFragment = new ExploreTvShowsTopRatedFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(exploreTvShowsPopularFragment, getString(R.string.fragment_tvshows_popular_title));
        viewPagerAdapter.addFragment(exploreTvShowsTopRatedFragment, getString(R.string.fragment_tvshows_top_rated_title));
        viewPager.setAdapter(viewPagerAdapter);

        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_explore_black_24dp);

        /* Create notification channel as soon as app starts */
        Notification.createNotificationChannel(this);
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
