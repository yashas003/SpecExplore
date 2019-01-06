package com.blogspot.explorespec.specexplore;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class DonateActivity extends AppCompatActivity {
    Toolbar tbar;
    CardView donatePaypal;
    CardView donateBitcoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkBlack));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlack));
        } else {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        tbar = findViewById(R.id.detailTool);
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

        donatePaypal = findViewById(R.id.paypal);
        donatePaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateMe();
            }
        });

        donateBitcoin = findViewById(R.id.bitcoin);
        donateBitcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyBitcoinAddress();
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void copyBitcoinAddress() {

        String address = "qr7mjllay5eujc0r7k3x9x6gqdvzag9zuy7whasndr";
        String text = address.trim();
        if (text.length() > 0) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboardMgr = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardMgr.setText(text);
            } else {
                Toast.makeText(this, "Address copied to clipboard.", Toast.LENGTH_SHORT).show();
                android.content.ClipboardManager clipboardMgr = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied text", text);
                clipboardMgr.setPrimaryClip(clip);
            }
        }
    }

    private void donateMe() {
        Uri uri = Uri.parse("https://paypal.me/yashas3");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
