package com.bikcrum.logindemo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout nameFieldLayout;
    private TextInputEditText nameField;
    private TextInputLayout emailFieldLayout;
    private TextInputEditText emailField;
    private TextInputLayout contactNoFieldLayout;
    private TextInputEditText contactNoField;
    private TextInputLayout passwordFieldLayout;
    private TextInputEditText passwordField;
    private TextInputLayout confirmPasswordFieldLayout;
    private TextInputEditText confirmPasswordField;

    private ActionProcessButton submitBtn;

    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getActionBar() != null) {
            getActionBar().setDisplayShowHomeEnabled(true);
        }

        nameFieldLayout = findViewById(R.id.name_field_layout);
        nameField = findViewById(R.id.name_field);
        emailFieldLayout = findViewById(R.id.email_field_layout);
        emailField = findViewById(R.id.email_field);
        contactNoFieldLayout = findViewById(R.id.contact_field_layout);
        contactNoField = findViewById(R.id.contact_field);
        passwordFieldLayout = findViewById(R.id.password_field_layout);
        passwordField = findViewById(R.id.password_field);
        confirmPasswordFieldLayout = findViewById(R.id.confirm_password_field_layout);
        confirmPasswordField = findViewById(R.id.confirm_password_field);

        submitBtn = findViewById(R.id.submit_btn);

        Intent intent = getIntent();
        if (intent != null) {
            String emailId = intent.getStringExtra(Constant.EMAIL_ID);
            if (emailId != null && Util.isValidEmail(emailId.trim())) {
                emailField.setText(emailId.trim());
            }
        }
    }

    public void submit(View view) {
        nameFieldLayout.setErrorEnabled(false);
        emailFieldLayout.setErrorEnabled(false);
        contactNoFieldLayout.setErrorEnabled(false);
        passwordFieldLayout.setErrorEnabled(false);
        confirmPasswordFieldLayout.setErrorEnabled(false);

        checkDetails();
    }

    private void checkDetails() {
        boolean detailsOk = true;
        if (nameField.getText().toString().trim().isEmpty()) {
            nameFieldLayout.setErrorEnabled(true);
            nameFieldLayout.setError(getString(R.string.name_is_empty));
            detailsOk = false;
        }
        if (!Util.isValidEmail(emailField.getText().toString().trim())) {
            emailFieldLayout.setErrorEnabled(true);
            emailFieldLayout.setError(getString(R.string.invalid_email));
            detailsOk = false;
        }
        if (contactNoField.getText().toString().trim().isEmpty()) {
            contactNoFieldLayout.setErrorEnabled(true);
            contactNoFieldLayout.setError(getString(R.string.contact_is_empty));
            detailsOk = false;
        }
        if (passwordField.getText().toString().trim().isEmpty()) {
            passwordFieldLayout.setErrorEnabled(true);
            passwordFieldLayout.setError(getString(R.string.password_is_empty));
            detailsOk = false;
        }
        if (!confirmPasswordField.getText().toString().trim().equals(passwordField.getText().toString().trim())) {
            confirmPasswordFieldLayout.setErrorEnabled(true);
            confirmPasswordFieldLayout.setError(getString(R.string.password_no_match));
            detailsOk = false;
        }

        if (detailsOk) {
            submitDetails();
        }
    }

    private void submitDetails() {
        AttemptRegistration attemptLogin = new AttemptRegistration();
        attemptLogin.execute(
                nameField.getText().toString().trim(),
                emailField.getText().toString().trim(),
                contactNoField.getText().toString().trim(),
                passwordField.getText().toString().trim()
        );
    }


    private class AttemptRegistration extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            submitBtn.setProgress(50);
            submitBtn.setEnabled(false);
        }


        @Override
        protected JSONObject doInBackground(String... args) {

            String name = args[0];
            String email = args[1];
            String contactNo = args[2];
            String password = args[3];

            ArrayList<Object> params = new ArrayList<>();
            params.add(new BasicNameValuePair("task", "register"));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("contact_no", contactNo));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = jsonParser.makeHttpRequest(Constant.URL, "POST", params);

            return json;

        }

        protected void onPostExecute(JSONObject result) {
            submitBtn.setEnabled(true);
            try {
                if (result != null) {
                    Log.i("biky", "result = " + result.getString("message"));
                    switch (result.getString("message")) {
                        case Constant.EMAIL_ALREADY_EXIST:
                            emailFieldLayout.setErrorEnabled(true);
                            emailFieldLayout.setError(getString(R.string.already_registered));
                            submitBtn.setProgress(-1);
                            break;
                        case Constant.REGISTRATION_ERROR:
                            submitBtn.setProgress(-1);
                            Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        case Constant.REGISTRATION_SUCCESS:
                            submitBtn.setProgress(100);
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            intent.putExtra(Constant.EMAIL_ID, emailField.getText().toString().trim());
                            intent.putExtra(Constant.PASSWORD, passwordField.getText().toString().trim());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            break;
                    }
                } else {
                    submitBtn.setProgress(-1);
                    Toast.makeText(RegistrationActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
