package com.core.picwiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView mTextViewName;
    private TextView mTextViewUsername;
    private TextView mTextViewTagline;
    SharedPreferences settings;
    private static Context contextFragment;

    public static ProfileFragment newInstance(Context context, int sectionNumber) {
        contextFragment = context;
        return new ProfileFragment();
    }

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mTextViewName = (TextView) view.findViewById(R.id.text_view_name);
        mTextViewUsername = (TextView) view.findViewById(R.id.text_view_username);
        mTextViewTagline = (TextView) view.findViewById(R.id.text_view_tag_line);
        settings = contextFragment.getSharedPreferences("config", Context.MODE_PRIVATE);
        String name = settings.getString("NAME", null);
        String tagline = settings.getString("TAGLINE", null);
        mTextViewUsername.setText(settings.getString("USERNAME", null));
        if (name == null || Objects.equals(name, "none"))
            mTextViewName.setVisibility(View.GONE);
        else
            mTextViewName.setText(name);
        if (tagline == null)
            mTextViewTagline.setVisibility(View.GONE);
        else
            mTextViewTagline.setText(tagline);
        return view;
    }
}
