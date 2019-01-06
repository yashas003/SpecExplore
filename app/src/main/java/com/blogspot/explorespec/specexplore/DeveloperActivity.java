package com.blogspot.explorespec.specexplore;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class DeveloperActivity extends AppCompatActivity {
    ImageView facebook;
    ImageView email;
    ImageView instagram;
    ImageView twitter;
    ImageView gplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        facebook = findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitFbProfile();
            }
        });

        twitter = findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followTwitter();
            }
        });

        instagram = findViewById(R.id.instagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followInstagram();
            }
        });

        email = findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

        gplus = findViewById(R.id.googleplus);
        gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitGplusProfile();
            }
        });
    }

    private void visitGplusProfile() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", "114850325130576922986");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + "114850325130576922986" + "/posts")));
        }
    }

    private void visitFbProfile() {
        String facebookUrl = "https://www.facebook.com/yashas.gowda.18062533";
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                Uri uri = Uri.parse("fb://page/yashas.gowda.18062533");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        } catch (PackageManager.NameNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    private void followTwitter() {
        Intent intent;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=871760900889337856"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Yashas0003"));
        }
        this.startActivity(intent);
    }

    private void followInstagram() {
        Uri uri = Uri.parse("http://instagram.com/_u/call_me_003");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/call_me_003")));
        }
    }

    private void sendMail() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "yashas348@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
