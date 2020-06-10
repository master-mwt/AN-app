package it.univaq.disim.mwt.android_native_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

import java.util.ArrayList;
import java.util.List;

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
        setContentView(R.layout.activity_main);

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
        viewPagerAdapter.addFragment(exploreTvShowsPopularFragment, "popular");
        viewPagerAdapter.addFragment(exploreTvShowsTopRatedFragment, "top rated");
        viewPager.setAdapter(viewPagerAdapter);

        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_explore_black_24dp);
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
