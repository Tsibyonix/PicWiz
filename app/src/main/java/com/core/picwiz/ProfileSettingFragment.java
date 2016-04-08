package com.core.picwiz;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSettingFragment extends android.app.Fragment {
    private SharedPreferences settings;
    private EditText mEditTextName;
    private EditText mEditTextTagline;
    private EditText mEditTextBio;

    private String name;
    private String tagline;
    private String email;

    private boolean timeout;
    private ProgressDialog progressDialog;

    private PicWizBackend picWizBackend;

    public ProfileSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        picWizBackend = new PicWizBackend(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_setting, container, false);
        progressDialog = new ProgressDialog(getActivity());
        settings = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        mEditTextName = (EditText) view.findViewById(R.id.edit_text_name_settings);
        mEditTextTagline = (EditText) view.findViewById(R.id.edit_text_tagline_settings);
        mEditTextBio = (EditText) view.findViewById(R.id.edit_text_about_settings);
        name = settings.getString("NAME", null);
        tagline = settings.getString("TAGLINE", null);
        email = settings.getString("EMAIL", null);
        String bio = null;//settings.getString("BIO", null);
        if (name != null)
            mEditTextName.setText(name);
        if (tagline != null)
            mEditTextTagline.setText(tagline);

        //mEditTextBio.
        //if (bio != null)
            //mEditTextBio.setText(bio);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.account_settings, menu);
    }

    private void postRequest() {
        int success = picWizBackend.getSuccess();
        String message = picWizBackend.getMessage();
        if (success == 1) {
            settings.edit().putString("NAME", name).apply();
            settings.edit().putString("TAGLINE", tagline).apply();
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_done:
                //async task
                progressDialog.show();
                final CountDownTimer countDownTimer = new CountDownTimer(6000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.i("time", String.valueOf(millisUntilFinished));
                        if (picWizBackend.getWait()) {
                            Log.i("while: ", picWizBackend.getSuccess()+": "+picWizBackend.getMessage());
                            progressDialog.hide();
                            this.cancel();
                            postRequest();
                        }
                    }

                    @Override
                    public void onFinish() {
                        timeout = true;
                        Log.i("time", "Clock finished");
                        progressDialog.hide();
                        this.cancel();
                        getActivity().finish();
                    }
                };

                name = mEditTextName.getText().toString();
                tagline = mEditTextTagline.getText().toString();
                if (name.isEmpty()) {
                    mEditTextName.setError("Name is required");
                    mEditTextName.requestFocus();
                }
                else if (tagline.isEmpty()) {
                    mEditTextTagline.setError("Tag line is required");
                    mEditTextTagline.requestFocus();
                }
                else {
                    picWizBackend.update(email, name, tagline);
                    countDownTimer.start();
                }
                break;
            case android.R.id.home:
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
