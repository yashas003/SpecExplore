package com.blogspot.explorespec.specexplore;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class AboutActivity extends AppCompatActivity {
    ConstraintLayout donate;
    ConstraintLayout license;
    ConstraintLayout library;
    ConstraintLayout terms;
    ConstraintLayout dev;
    TextView version;
    ImageView email;
    ImageView twitter;
    ImageView facebook;
    ImageView instagram;
    Toolbar tbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkBlack));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlack));
        } else {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        tbar = findViewById(R.id.aboutTBar);
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

        email = findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        twitter = findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitTwitter();
            }
        });

        instagram = findViewById(R.id.instagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitInstagram();
            }
        });

        facebook = findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitFacebook();
            }
        });

        donate = findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donate = new Intent(AboutActivity.this, DonateActivity.class);
                startActivity(donate);
            }
        });

        license = findViewById(R.id.license);
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, OssLicensesMenuActivity.class));
            }
        });

        library = findViewById(R.id.library);
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent library = new Intent(AboutActivity.this, LibraryActivity.class);
                startActivity(library);
            }
        });

        terms = findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AboutActivity.this);
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.terms_dialog, null);
                TextView textview = view.findViewById(R.id.text);
                textview.setText(R.string.terms_and_conditions);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AboutActivity.this);
                alertDialog.setTitle("Terms and conditions");
                alertDialog.setView(view);
                alertDialog.setPositiveButton("I AGREE", null);
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });

        dev = findViewById(R.id.developer);
        dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dev = new Intent(AboutActivity.this, DeveloperActivity.class);
                startActivity(dev);
            }
        });

        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String myVersionName = "Version";

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version = findViewById(R.id.version);
        version.setText("Version " + myVersionName);
    }

    private void visitFacebook() {
        String facebookUrl = "https://www.facebook.com/explorespec/";
        try {
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                Uri uri = Uri.parse("fb://page/explorespec");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        } catch (PackageManager.NameNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

    private void visitInstagram() {
        Uri uri = Uri.parse("http://instagram.com/_u/spec.explore");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/spec.explore")));
        }
    }

    private void visitTwitter() {
        Intent intent;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1055053178046038016"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ExploreSpec"));
        }
        this.startActivity(intent);
    }

    private void sendEmail() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "explorespec@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(AboutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
