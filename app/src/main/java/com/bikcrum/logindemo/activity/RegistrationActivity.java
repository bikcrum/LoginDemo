package com.bikcrum.logindemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bikcrum.logindemo.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(true);
        }
    }
}