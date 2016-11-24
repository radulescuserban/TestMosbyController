package com.ds.testprojectmosby.login;


import com.hannesdorfmann.mosby.mvp.MvpView;

interface LoginActivityView extends MvpView{
    void onUserDataFetched(boolean success);
}
