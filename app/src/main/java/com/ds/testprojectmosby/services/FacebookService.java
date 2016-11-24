package com.ds.testprojectmosby.services;

import com.ds.testprojectmosby.PrefUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FacebookService {

    private static FacebookService facebookService;
    private static CallbackManager callbackManager;
    private static AccessTokenTracker accessTokenTracker;


    public FacebookService() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    PrefUtils.removeAccessToken();
                } else {
                    PrefUtils.setFacebookToken(currentAccessToken);
                }
            }
        };

        accessTokenTracker.startTracking();
    }

    public static FacebookService getInstance(FacebookCallback<LoginResult> facebookCallback) {
        if (facebookService == null) {
            facebookService = new FacebookService();
        }

        setCallback(facebookCallback);
        return facebookService;
    }

    private static void setCallback(FacebookCallback<LoginResult> facebookCallback) {
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void startTrackingToken() {
        accessTokenTracker.startTracking();
    }

    public void stopTrackingToken() {
        accessTokenTracker.startTracking();
    }
}
