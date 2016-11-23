package com.ds.testprojectmosby;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.ds.testprojectmosby.defaultscreen.DefaultScreenController;
import com.ds.testprojectmosby.detail.DetailController;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.w3c.dom.Text;

/**
 * Created by Serban Theodor on 17-Nov-16.
 */

public class MainActivity extends MvpActivity<MainActivityView, MainActivityPresenter> implements MainActivityView{

    Button btnShowMessage, btnChangeScreen;
    TextView tvMessage;
    Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);

        ViewGroup container = (FrameLayout) findViewById(R.id.content_frame);
        router = Conductor.attachRouter(this, container, savedInstanceState);
        if(!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new DefaultScreenController())
            .tag("default"));
        }

        btnChangeScreen = (Button) findViewById(R.id.btn_change_screen);
        btnShowMessage = (Button) findViewById(R.id.btn_show_message);

        tvMessage = (TextView) findViewById(R.id.tv_message);

    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

    @NonNull
    @Override
    public MainActivityPresenter createPresenter() {
        return new MainActivityPresenter();
    }

    @Override
    public void showText(String message) {
        tvMessage.setText(message);
    }


    public void showMessage(View view){
        tvMessage.setText(presenter.getMessage());
        presenter.waitAndDisplay(3000);
    }

    public void changeScreen(View view){
        if(router.getControllerWithTag("default") != null) {
            router.pushController(RouterTransaction.with(router.getControllerWithTag("default")));
        } else {
            router.pushController(RouterTransaction.with(new DefaultScreenController()).tag("default"));
        }


    }
}
