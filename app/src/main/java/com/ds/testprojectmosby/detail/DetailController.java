package com.ds.testprojectmosby.detail;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ds.testprojectmosby.EmptyView;
import com.ds.testprojectmosby.R;
import com.hannesdorfmann.mosby.mvp.conductor.MvpController;

/**
 * Created by Serban Theodor on 17-Nov-16.
 */

public class DetailController extends MvpController<EmptyView, DetailPresenter> {

    private TextView tvController;
    private int counter = 0;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        final View view = inflater.inflate(R.layout.controller_default, container);
        tvController = (TextView) view.findViewById(R.id.tv_controller);
        tvController.setText("Detail screen! " + counter);
        counter++;
        return view;
    }

    @NonNull
    @Override
    public DetailPresenter createPresenter() {
        return new DetailPresenter();
    }
}
