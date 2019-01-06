package com.blogspot.explorespec.specexplore;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LibraryActivity extends AppCompatActivity {
    Toolbar tbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkBlack));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlack));
        } else {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        tbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            tbar.setBackgroundColor(getResources().getColor(R.color.darkBlack));
            tbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
            tbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back_white));
            tbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            tbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
            tbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
