package com.bikcrum.logindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
