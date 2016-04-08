package com.core.picwiz;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;

public class SecondaryActivity extends AppCompatActivity {
    private FrameLayout fragmentContainer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.secondary_toolbar);
        setSupportActionBar(mToolbar);
        String toLoad = getIntent().getExtras().getString("toLoad");
        fragmentContainer = (FrameLayout) findViewById(R.id.secondary_container);
        assert toLoad != null;
        switch (toLoad) {
            case "profile_settings":
                if (mToolbar != null) {
                    mToolbar.setTitle("Profile Setting");
                }
                getFragmentManager().beginTransaction()
                        .replace(fragmentContainer.getId(), new ProfileSettingFragment())
                        .commit();
                break;
            case "upload_picture":
                getFragmentManager().beginTransaction()
                        .replace(fragmentContainer.getId(), new PreUploadFragment())
                        .commit();
                break;
            case "profile":
                //getFragmentManager().beginTransaction()
                  //      .replace(fragmentContainer.getId(), new ProfileFragment())
                    //    .commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
