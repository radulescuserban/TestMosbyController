package com.ds.testprojectmosby;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Serban Theodor on 17-Nov-16.
 */

public interface MainActivityView extends MvpView {

    void showText(String message);
}
