package com.bikcrum.logindemo.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import com.bikcrum.logindemo.R;

public class LoginActivity extends AppCompatActivity {

    private Button signInBtn;
    private TextInputLayout emailFieldLayout;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;

    private AppCompatButton signUpBtn;

    private int i = 0;
    private View animateView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInBtn = findViewById(R.id.sign_in_btn);
        emailFieldLayout = findViewById(R.id.email_field_layout);
        emailField = findViewById(R.id.name_field);
        passwordField = findViewById(R.id.password_field);

        animateView = findViewById(R.id.animate_view);

        signUpBtn = findViewById(R.id.sign_up_btn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginDetails();
            }
        });

        signUpBtn = findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

    }

    private void checkLoginDetails() {

        emailFieldLayout.setErrorEnabled(true);
        emailFieldLayout.setError("Email id not registered");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }, 3000);

    }
/*
    private void toNextPage() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            int cx = (signInBtn.getLeft() + signInBtn.getRight()) / 2;
            int cy = (signInBtn.getTop() + signInBtn.getBottom()) / 2;

            Animator animator = null;

            animator = ViewAnimationUtils.createCircularReveal(animateView, cx, cy, 0, getResources().getDisplayMetrics().heightPixels * 1.2f);

            animator.setDuration(500);
            animator.setInterpolator(new AccelerateInterpolator());
            animateView.setVisibility(View.VISIBLE);
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    animateView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            animateView.setVisibility(View.INVISIBLE);
        }
    }*/
}
