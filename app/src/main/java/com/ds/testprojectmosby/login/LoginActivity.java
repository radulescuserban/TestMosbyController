package com.ds.testprojectmosby.login;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ds.testprojectmosby.MainActivity;
import com.ds.testprojectmosby.PrefUtils;
import com.ds.testprojectmosby.R;
import com.ds.testprojectmosby.services.FacebookService;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

public class LoginActivity extends MvpActivity<LoginActivityView, LoginActivityPresenter> implements LoginActivityView {

    private LoginButton loginButton;
    private FacebookCallback<LoginResult> facebookCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefUtils.init(getApplicationContext());

        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");

        if (PrefUtils.getCurrentUser() != null){
            homeRedirect();
        }

        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.setUserToken(loginResult.getAccessToken());
                presenter.fetchUserData();
            }

            @Override
            public void onCancel() {
                Log.i("Login Result", "CANCEL");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("Login Result", "onError");
            }
        };

        FacebookService facebookService = FacebookService.getInstance(facebookCallback);
    }

    private void homeRedirect() {
        Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(homeIntent);

        finish();

    }

    @NonNull
    @Override
    public LoginActivityPresenter createPresenter() {
        return new LoginActivityPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookService.getInstance(facebookCallback).getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onUserDataFetched(boolean success) {
        if (success) {
            homeRedirect();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage("Failed to get user data. Please try again.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.fetchUserData();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.cleanUser();
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();
            alertDialog.show();
        }


    }
}
