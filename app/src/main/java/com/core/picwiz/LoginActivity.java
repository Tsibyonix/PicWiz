package com.core.picwiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences settings;

    private boolean register = false;

    private ImageView mImageViewLogo;

    private LinearLayout mLinearLayoutLoginForm;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputLayout mTextInputLayoutUsername;

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextUsername;

    private Button mButtonSignIn;
    private TextView mTextViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settings = getSharedPreferences("config", MODE_PRIVATE);

        mImageViewLogo = (ImageView) findViewById(R.id.image_view_logo);

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

        animation();

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register) {
                    // proceed with register
                    Toast.makeText(LoginActivity.this, "Registering", Toast.LENGTH_SHORT).show();
                } else {
                    // proceed with sign in
                    Toast.makeText(LoginActivity.this, "Signing in", Toast.LENGTH_SHORT).show();
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

    void animation() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mImageViewLogo.startAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLinearLayoutLoginForm.setVisibility(View.VISIBLE);
                mLinearLayoutLoginForm.setAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
