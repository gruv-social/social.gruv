package com.gruv.navigation;

import android.view.View;

import com.gruv.activities.LandingActivity;

public class Navigation extends LandingActivity {

    public Navigation() {
    }


    public static void showProgress() {
        layoutProgress.setVisibility(View.VISIBLE);
    }

    public static void hideProgress() {
        layoutProgress.setVisibility(View.GONE);
    }
}
