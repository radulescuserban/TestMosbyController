package com.ds.testprojectmosby;

import android.content.Context;

import com.ds.testprojectmosby.pojos.User;
import com.facebook.AccessToken;

/**
 * Created by Serban Theodor on 23-Nov-16.
 */

public class PrefUtils {

    public static final String USER_PREFS = "user_prefs";

    private static final String CURRENT_USER = "current_user_value";
    private static final String FACEBOOK_TOKEN = "access_token";

    public static void init(Context context) {
        ComplexPreferences.init(context);
    }

    public static void setFacebookToken(AccessToken accessToken) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences();
        complexPreferences.putObject(FACEBOOK_TOKEN, accessToken);
        complexPreferences.commit();
    }

    public static AccessToken getFacebookToken() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences();
        AccessToken accessToken = complexPreferences.getObject(FACEBOOK_TOKEN, AccessToken.class);
        return accessToken;
    }

    public static void setCurrentUser(User currentUser){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences();
        complexPreferences.putObject(CURRENT_USER, currentUser);
        complexPreferences.commit();
    }

    public static User getCurrentUser(){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences();
        User currentUser = complexPreferences.getObject(CURRENT_USER, User.class);
        return currentUser;
    }

    public static void clearCurrentUser(){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences();
        complexPreferences.clearData(USER_PREFS);
        complexPreferences.clearData(FACEBOOK_TOKEN);
        complexPreferences.commit();
    }

    public static void removeAccessToken() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences();
        complexPreferences.clearData(FACEBOOK_TOKEN);
        complexPreferences.commit();
    }
}
