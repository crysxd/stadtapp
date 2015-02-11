package stadtapp.hfu.de.stadtapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import de.hfu.stadtapp.R;
import stadtapp.hfu.de.stadtapp.net.SightList;
import stadtapp.hfu.de.stadtapp.tabs.SightListFragment;
import stadtapp.hfu.de.stadtapp.tabs.SightMapFragment;

/**
 * Created by wuerthnc on 10.02.15.
 */
public class UserActivity extends DialogHostActivity {

    private MyPagerAdapter adapterViewPager;
    private ViewPager vpPager;
    private String myUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.myUser = this.getIntent().getExtras().getString("user");

        setContentView(R.layout.activity_main);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        ActionBar actionBar = getActionBar();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };

        vpPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < adapterViewPager.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(adapterViewPager.getPageTitle(i)).setTabListener(tabListener));
        }

        getActionBar().setIcon(R.drawable.ic_launcher_light);
        getActionBar().setTitle(this.myUser);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    try {
                        return new SightListFragment(UserActivity.this, UserActivity.this.myUser);

                    } catch(Exception e) {
                        e.printStackTrace();
                        return null;

                    }
                case 1:
                    try {
                    return new SightMapFragment(SightList.getSightList(UserActivity.this).getAllUserSights(UserActivity.this.myUser));

                    } catch(Exception e) {
                        e.printStackTrace();
                        return null;

                    }
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sights";
                case 1:
                    return "Map";
                default:
                    return null;
            }
        }
    }
}
