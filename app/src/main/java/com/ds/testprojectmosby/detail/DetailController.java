package com.ds.testprojectmosby.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.ds.testprojectmosby.EmptyView;
import com.ds.testprojectmosby.MyApplication;
import com.ds.testprojectmosby.PrefUtils;
import com.ds.testprojectmosby.R;
import com.ds.testprojectmosby.defaultscreen.DefaultScreenController;
import com.ds.testprojectmosby.pojos.User;
import com.facebook.login.LoginManager;
import com.hannesdorfmann.mosby.mvp.conductor.MvpController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Serban Theodor on 17-Nov-16.
 */

public class DetailController extends MvpController<EmptyView, DetailPresenter> {

    private TextView btnLogout;
    private User user;
    private ImageView profileImage;
    private Bitmap bitmap;
    private TextView tvController;
    private int counter = 0;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        final View view = inflater.inflate(R.layout.controller_detail, container);
        tvController = (TextView) view.findViewById(R.id.tv_controller);
        tvController.setText("Detail screen! " + counter);
        counter++;

        user = PrefUtils.getCurrentUser(getApplicationContext());
        profileImage= (ImageView) view.findViewById(R.id.iv_fb_image);

        // fetching facebook's profile picture
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {
                    imageURL = new URL("https://graph.facebook.com/" + "1435767690067161" + "/picture?type=large");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                profileImage.setImageBitmap(bitmap);
            }
        }.execute();


        btnLogout = (TextView) view.findViewById(R.id.btn_fb_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.clearCurrentUser(getApplicationContext());


                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();

                RouterTransaction routerTransaction = RouterTransaction.with(new DefaultScreenController())
                        .tag("default");

                if (getRouter().getControllerWithTag("default") != null) {
                    getRouter().popToTag("default");
                    getRouter().replaceTopController(routerTransaction);
                } else {
                    getRouter().pushController(routerTransaction);
                }


            }
        });

        return view;
    }

    @NonNull
    @Override
    public DetailPresenter createPresenter() {
        return new DetailPresenter();
    }
}
