package com.ds.testprojectmosby.defaultscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.RouterTransaction;
import com.ds.testprojectmosby.EmptyView;
import com.ds.testprojectmosby.PrefUtils;
import com.ds.testprojectmosby.R;
import com.ds.testprojectmosby.detail.DetailController;
import com.ds.testprojectmosby.pojos.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby.mvp.conductor.MvpController;

import org.json.JSONObject;

/**
 * Created by Serban Theodor on 17-Nov-16.
 */

public class DefaultScreenController extends MvpController<EmptyView, DefaultScreenPresenter> implements EmptyView{

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    private User user;

    private static final String TAG = DefaultScreenController.class.getSimpleName();

    private TextView tvController;
    private int counter = 0;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        final View view = inflater.inflate(R.layout.controller_default, container);

        if(PrefUtils.getCurrentUser() != null) {
            RouterTransaction routerTransaction = RouterTransaction.with(new DetailController())
                    .tag("detail");

            if (getRouter().getControllerWithTag("detail") != null) {
                getRouter().popToTag("detail");
                getRouter().replaceTopController(routerTransaction);
            } else {
                getRouter().pushController(routerTransaction);
            }

        }

        tvController = (TextView) view.findViewById(R.id.tv_controller);
        tvController.setText("Default screeen!" + counter);

        counter++;
        return view;
    }

    @NonNull
    @Override
    public DefaultScreenPresenter createPresenter() {
        return new DefaultScreenPresenter();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton)getView().findViewById(R.id.btn_fb_login);

        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        btnLogin= (TextView) getView().findViewById(R.id.tv_fb_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        progressDialog.dismiss();
                    }
                });

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                PrefUtils.setCurrentUser(user);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(getActivity(),"welcome "+user.name,Toast.LENGTH_LONG).show();

                            RouterTransaction routerTransaction = RouterTransaction.with(new DetailController())
                                    .tag("detail");

                            if (getRouter().getControllerWithTag("detail") != null) {
                                getRouter().popToTag("detail");
                                getRouter().replaceTopController(routerTransaction);
                            } else {
                                getRouter().pushController(routerTransaction);
                            }

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };


}
