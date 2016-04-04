package com.core.picwiz;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.ViewParent;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private SharedPreferences settings;
    private String email;
    private String username;

    private ActionBarDrawerToggle toggle;

    private TextView mTextViewHeaderEmail;
    private TextView mTextViewHeaderUsername;

    private ViewPager mViewPagerTab;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        settings = getSharedPreferences("config", MODE_PRIVATE);

        email = settings.getString("EMAIL", null);
        username = settings.getString("USERNAME", null);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPagerTab = (ViewPager) findViewById(R.id.container);
        mViewPagerTab.setAdapter(mSectionsPagerAdapter);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        //collapsingToolbarLayout.setTitle("test");
        setSupportActionBar(mToolbar);

        int[] tabIconRes = {
                R.drawable.ic_tab_home,
                R.drawable.ic_tab_explore,
                R.drawable.ic_tab_notification,
                R.drawable.ic_tab_profile
        };

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPagerTab);
        for (int i = 0; i < tabLayout.getTabCount(); i++)
            tabLayout.getTabAt(i).setIcon(tabIconRes[i]);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mTextViewHeaderUsername = (TextView) drawerLayout.findViewById(R.id.header_name_textView);
        mTextViewHeaderEmail = (TextView) drawerLayout.findViewById(R.id.header_email_textView);

        //mViewPagerTab.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        //});

        Log.i("Shared prefs: ", email + ":" + username);
        //mTextViewHeaderUsername.setText("fer");
        //mTextViewHeaderEmail.setText("email");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return HomeFragment.newInstance(position);
                case 1:
                    return ExploreFragment.newInstance(position);
                case 2:
                    return NotificationFragment.newInstance(position);
                case 3:
                    return ProfileFragment.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        private String[] tabTitles = {
                "Home",
                "Explore",
                "Activity",
                "Profile"
        };

        @Override
        public CharSequence getPageTitle(int position) {
            //switch (position) {
            //    case 0:
            //       return "Home";
            //    case 1:
            //        return "Explore";
            //    case 2:
            //        return "Notification";
            //    case 3:
            //        return "Profile";
            //}
            //return null;

            //Drawable image = ContextCompat.getDrawable(getApplicationContext(), tabIconRes[position]);
            //image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            //SpannableString sb = new SpannableString(" " + tabTitles[position]);
            //ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            //sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //return sb;
            return "";
        }
    }
}
