package com.core.picwiz;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private boolean register = false;

    private TextInputLayout mTextInputLayoutUsername;


    private Button mButtonSignIn;
    private TextView mTextViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextInputLayoutUsername = (TextInputLayout) findViewById(R.id.text_input_layout_username);
        mButtonSignIn = (Button) findViewById(R.id.button_sign_in);
        mTextViewRegister = (TextView) findViewById(R.id.text_view_register);

        mTextInputLayoutUsername.setVisibility(View.GONE);
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
}
