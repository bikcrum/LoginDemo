package com.bikcrum.logindemo;

import android.content.Intent;
import android.drm.DrmStore;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dd.processbutton.iml.ActionProcessButton;

public class LoginActivity extends AppCompatActivity {

    private ActionProcessButton signInBtn;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int i = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInBtn = findViewById(R.id.sign_in_btn);


        runnable = new Runnable() {
            @Override
            public void run() {
                signInBtn.setProgress(i);
                Log.i("biky", "progress = " + i);
                i = (i + 10) % 100;
                handler.postDelayed(this, 500);
            }
        };

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                signInBtn.setProgress(0);
                handler.postDelayed(runnable, 500);
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}
