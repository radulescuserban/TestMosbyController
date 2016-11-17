package com.ds.testprojectmosby;

import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;

import java.util.logging.LogRecord;

/**
 * Created by Serban Theodor on 17-Nov-16.
 */

public class MainActivityPresenter extends MvpBasePresenter<MainActivityView> {


    private String message;


    @Override
    public void attachView(MainActivityView view) {
        super.attachView(view);
        message = "Salut!";
    }

    public String getMessage() {
        return message;
    }

    public void waitAndDisplay(long time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isViewAttached()) {
                    message = "Pa!";
                    getView().showText(message);
                }

            }
        }, time);
    }

}
