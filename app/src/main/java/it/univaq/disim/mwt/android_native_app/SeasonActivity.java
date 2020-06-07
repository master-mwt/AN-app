package it.univaq.disim.mwt.android_native_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.mwt.android_native_app.model.Season;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class SeasonActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Season chosenSeason;
    private List<Season> seasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        viewPager = findViewById(R.id.main_viewpager);
        tabLayout = findViewById(R.id.main_tab_layout);

        seasons = (List<Season>) getIntent().getSerializableExtra("seasons");
        chosenSeason = (Season) getIntent().getSerializableExtra("chosen_season");
    }

    @Override
    protected void onStart() {
        super.onStart();

        SeasonActivity.ViewPagerAdapter viewPagerAdapter = new SeasonActivity.ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        int index = 0;

        for(int i = 0; i < seasons.size(); i++){
            Season season = seasons.get(i);
            if(season.equals(chosenSeason)){
                index = i;
            }

            SeasonFragment fragment = SeasonFragment.newInstance(season);
            viewPagerAdapter.addFragment(fragment, season.getName());
        }

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(index).select();

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
