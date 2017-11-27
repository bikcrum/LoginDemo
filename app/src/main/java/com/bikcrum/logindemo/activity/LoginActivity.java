package com.bikcrum.logindemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bikcrum.logindemo.R;
import com.bikcrum.logindemo.utility.Constant;
import com.bikcrum.logindemo.utility.JSONParser;
import com.bikcrum.logindemo.utility.Util;
import com.dd.processbutton.iml.ActionProcessButton;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private JSONParser jsonParser = new JSONParser();

    private ActionProcessButton signInBtn;
    private TextInputLayout emailFieldLayout;
    private TextInputEditText emailField;
    private TextInputLayout passwordFieldLayout;
    private TextInputEditText passwordField;

    private AppCompatButton signUpBtn;
    private AppCompatButton forgotPasswordBtn;
    private CheckBox rememberMe;

    private int i = 0;
    private View animateView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInBtn = findViewById(R.id.sign_in_btn);
        emailFieldLayout = findViewById(R.id.email_field_layout);
        emailField = findViewById(R.id.email_field);
        passwordFieldLayout = findViewById(R.id.password_field_layout);
        passwordField = findViewById(R.id.password_field);
        rememberMe = findViewById(R.id.remember_me_checkbox);

        animateView = findViewById(R.id.animate_view);

        preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);

        if (preferences.getBoolean(Constant.REMEMBER_ME, true)) {
            rememberMe.setChecked(true);
            String emailId = preferences.getString(Constant.EMAIL_ID, null);
            String password = preferences.getString(Constant.PASSWORD, null);

            if (emailId != null) {
                emailField.setText(emailId);
            }
            if (password != null) {
                passwordField.setText(password);
            }
        } else {
            rememberMe.setChecked(false);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.EMAIL_ID, null);
            editor.putString(Constant.PASSWORD, null);
            editor.apply();
        }

        if (getIntent() != null) {
            String emailId = getIntent().getStringExtra(Constant.EMAIL_ID);
            String password = getIntent().getStringExtra(Constant.PASSWORD);

            if (emailId != null) {
                emailField.setText(emailId);
            }
            if (password != null) {
                passwordField.setText(password);
            }
        }

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                preferences.edit().putBoolean(Constant.REMEMBER_ME, checked).apply();
            }
        });

        signUpBtn = findViewById(R.id.sign_up_btn);
        forgotPasswordBtn = findViewById(R.id.forgot_password_btn);

        signInBtn.setMode(ActionProcessButton.Mode.ENDLESS);
        signInBtn.setProgress(0);

        signUpBtn = findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                if (emailFieldLayout.isErrorEnabled() || !emailField.toString().isEmpty()) {
                    intent.putExtra(Constant.EMAIL_ID, emailField.getText().toString());
                }
                startActivity(intent);
            }
        });

    }

    public void signIn(View view) {
        if (rememberMe.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.EMAIL_ID, emailField.getText().toString().trim());
            editor.putString(Constant.PASSWORD, passwordField.getText().toString().trim());
            editor.apply();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.EMAIL_ID, null);
            editor.putString(Constant.PASSWORD, null);
            editor.apply();
        }
        emailFieldLayout.setErrorEnabled(false);
        passwordFieldLayout.setErrorEnabled(false);

        String emailId = emailField.getText().toString();
        if (!Util.isValidEmail(emailId)) {
            emailFieldLayout.setErrorEnabled(true);
            emailField.setError(getString(R.string.invalid_email));
            return;
        }

        AttemptLogin attemptLogin = new AttemptLogin();
        attemptLogin.execute(emailField.getText().toString(), passwordField.getText().toString());
    }


    private class AttemptLogin extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            signInBtn.setProgress(50);
            signInBtn.setEnabled(false);
            signUpBtn.setEnabled(false);
            forgotPasswordBtn.setEnabled(false);
            rememberMe.setEnabled(false);
        }


        @Override
        protected JSONObject doInBackground(String... args) {


            String email = args[0];
            String password = args[1];

            ArrayList<Object> params = new ArrayList<>();
            params.add(new BasicNameValuePair("task", "login"));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));


            JSONObject json = jsonParser.makeHttpRequest(Constant.URL, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {
            signInBtn.setEnabled(true);
            signUpBtn.setEnabled(true);
            forgotPasswordBtn.setEnabled(true);
            rememberMe.setEnabled(true);
            try {
                if (result != null) {
                    switch (result.getString("message")) {
                        case Constant.EMAIL_NOT_FOUND:
                            emailFieldLayout.setErrorEnabled(true);
                            emailFieldLayout.setError(getString(R.string.please_sign_up));
                            signInBtn.setProgress(-1);
                            break;
                        case Constant.INCORRECT_PASSWORD:
                            passwordFieldLayout.setErrorEnabled(true);
                            passwordFieldLayout.setError(getString(R.string.incorrect_password));
                            signInBtn.setProgress(-1);
                            break;
                        case Constant.LOGIN_SUCCESSFUL:
                            signInBtn.setProgress(100);

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            finish();
                            break;
                    }
                } else {
                    signInBtn.setProgress(-1);
                    Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onStop() {
        if (rememberMe.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.EMAIL_ID, emailField.getText().toString().trim());
            editor.putString(Constant.PASSWORD, passwordField.getText().toString().trim());
            editor.apply();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.EMAIL_ID, null);
            editor.putString(Constant.PASSWORD, null);
            editor.apply();
        }
        super.onStop();
    }
}
