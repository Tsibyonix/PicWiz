package com.core.picwiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private String ID;
    private String email;

    private String inputEmail;
    private String inputUsername;

    private SharedPreferences settings;

    private boolean register = false;

    private ImageView mImageViewLogo;
    private ProgressBar mProgressBarLogin;

    private CoordinatorLayout mCoordinationLayoutMainLayout;

    private LinearLayout mLinearLayoutLoginForm;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputLayout mTextInputLayoutUsername;

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextUsername;

    private Button mButtonSignIn;
    private TextView mTextViewRegister;

    private Boolean timeout = false;
    private PicWizBackend picWizBackend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settings = getSharedPreferences("config", MODE_PRIVATE);

        mImageViewLogo = (ImageView) findViewById(R.id.image_view_logo);
        mProgressBarLogin = (ProgressBar) findViewById(R.id.progress_bar_login);

        mCoordinationLayoutMainLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_main_layout);

        mLinearLayoutLoginForm = (LinearLayout) findViewById(R.id.linear_layout_login_form);
        mTextInputLayoutEmail = (TextInputLayout) findViewById(R.id.text_input_layout_email);
        mTextInputLayoutPassword = (TextInputLayout) findViewById(R.id.text_input_layout_password);
        mTextInputLayoutUsername = (TextInputLayout) findViewById(R.id.text_input_layout_username);

        mEditTextEmail = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password);
        mEditTextUsername = (EditText) findViewById(R.id.edit_text_username);

        mButtonSignIn = (Button) findViewById(R.id.button_sign_in);
        mTextViewRegister = (TextView) findViewById(R.id.text_view_register);

        mTextInputLayoutUsername.setVisibility(View.GONE);

        picWizBackend = new PicWizBackend(LoginActivity.this);

        //settings.edit().putString("USER_ID", "test").apply();
        ID = settings.getString("USER_ID", null);
        email = settings.getString("EMAIL", null);

        animation(false);

        final CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("time", String.valueOf(millisUntilFinished));
                if (picWizBackend.getWait()) {
                    Log.i("while: ", picWizBackend.getSuccess()+": "+picWizBackend.getMessage());
                    this.cancel();
                    attemptRegister();
                }
            }

            @Override
            public void onFinish() {
                timeout = true;
                Log.i("time", "Clock finished");
                this.cancel();
            }
        };

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register) {
                    // proceed with register
                    mEditTextEmail.setError(null);
                    mEditTextUsername.setError(null);
                    mEditTextPassword.setError(null);
                    String email = mEditTextEmail.getText().toString().trim();
                    String password = mEditTextPassword.getText().toString().trim();
                    String username = mEditTextUsername.getText().toString().trim();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (!email.isEmpty()) {
                        if (!password.isEmpty()) {
                            if (!username.isEmpty()) {
                                // do shit
                                inputEmail = email;
                                inputUsername = username;
                                mLinearLayoutLoginForm.setVisibility(View.GONE);
                                mProgressBarLogin.setVisibility(View.VISIBLE);

                                picWizBackend.register(email, password, username);
                                countDownTimer.start();
                            } else {
                                mEditTextUsername.setError(getString(R.string.error_username_empty));
                                mEditTextUsername.requestFocus();
                            }
                        } else {
                            mEditTextPassword.setError(getString(R.string.error_password_empty));
                            mEditTextPassword.requestFocus();
                        }
                    } else {
                        mEditTextEmail.setError(getString(R.string.error_email_empty));
                        mEditTextEmail.requestFocus();
                    }
                } else {
                    // proceed with sign in
                    String email = mEditTextEmail.getText().toString();
                    String password = mEditTextPassword.getText().toString();
                    if (!email.isEmpty()) {
                        if (!password.isEmpty()) {
                            Snackbar.make(mCoordinationLayoutMainLayout.getRootView(), "Signing in", Snackbar.LENGTH_LONG).show();
                            mLinearLayoutLoginForm.setVisibility(View.GONE);
                            mProgressBarLogin.setVisibility(View.VISIBLE);
                        } else {
                            mEditTextPassword.setError(getString(R.string.error_password_empty));
                            mEditTextPassword.requestFocus();
                        }
                    } else {
                        mEditTextEmail.setError(getString(R.string.error_email_empty));
                        mEditTextEmail.requestFocus();
                    }
                }
            }
        });
    }

    public void onRegister(View view) {
        if (register) {
            mTextInputLayoutUsername.setVisibility(View.GONE);
            mButtonSignIn.setText(R.string.action_sign_in);
            mTextViewRegister.setText(R.string.prompt_or_register);
            register = false;
        } else {
            mTextInputLayoutUsername.setVisibility(View.VISIBLE);
            mButtonSignIn.setText(R.string.action_register);
            mTextViewRegister.setText(R.string.prompt_or_sign_in);
            register = true;
        }
    }

    private void attemptRegister() {
        picWizBackend.setResponseReceivedFalse();
        String message = picWizBackend.getMessage();
        String host = picWizBackend.getHost();
        int success = picWizBackend.getSuccess();
        String id = null;
        if (success == 1)
            id = picWizBackend.getId();

        if (success == 0) {
            mLinearLayoutLoginForm.setVisibility(View.VISIBLE);
            switch (host) {
                case "email":
                    mEditTextEmail.setError(message);
                    mEditTextEmail.requestFocus();
                    break;
                case "username":
                    mEditTextUsername.setError(message);
                    mEditTextUsername.requestFocus();
                    break;
                case "password":
                    mEditTextPassword.setError(message);
                    mEditTextPassword.requestFocus();
            }
        } else {
            settings.edit().putString("USER_ID", id).apply();
            settings.edit().putString("EMAIL", inputEmail).apply();
            settings.edit().putString("USERNAME", inputUsername).apply();
            //add new activity
            finish();
        }
    }

    private void animation(Boolean re) {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        if (!re) {
            mImageViewLogo.startAnimation(fadeIn);
        } else {
            fieldAnimation(fadeIn);
        }

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (ID == null) {
                    fieldAnimation(fadeIn);
                } else {
                    // get the user ID authenticated, and show the snack. If authentication fails,
                    // clear the 'email' and 'id' in the sharedPref and set ID = null.
                    // recall animation() and pass true, this will stop the img logo from animation and fadein the fields.
                    Snackbar.make(mCoordinationLayoutMainLayout.getRootView(), "Logging in as: " + email, Snackbar.LENGTH_INDEFINITE).show();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fieldAnimation(Animation animation) {
        mLinearLayoutLoginForm.setVisibility(View.VISIBLE);
        mLinearLayoutLoginForm.setAnimation(animation);
    }
}
