package com.ds.testprojectmosby.login;


import android.os.Bundle;
import android.util.Log;

import com.ds.testprojectmosby.PrefUtils;
import com.ds.testprojectmosby.pojos.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivityPresenter  extends MvpBasePresenter<LoginActivityView> {

    CallbackManager callbackManager;

    @Override
    public void attachView(LoginActivityView view) {
        super.attachView(view);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Login Result", "SUCCESS");
            }

            @Override
            public void onCancel() {
                Log.i("Login Result", "CANCEL");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("Login Result", "onError");
            }
        });
    }

    public void setUserToken(AccessToken facebookToken) {
        PrefUtils.setFacebookToken(facebookToken);
    }
    public void fetchUserData() {
        GraphRequest facebookRequest = GraphRequest.newMeRequest(PrefUtils.getFacebookToken(),
                new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                User user = new User();
                try {
                    user.facebookID = object.getString("id");
                    user.name = object.getString("name");
                    user.email = object.getString("email");
                    PrefUtils.setCurrentUser(user);
                    getView().onUserDataFetched(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().onUserDataFetched(false);
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        facebookRequest.setParameters(parameters);
        facebookRequest.executeAsync();

    }

    public void cleanUser() {
        PrefUtils.removeAccessToken();
    }
}
