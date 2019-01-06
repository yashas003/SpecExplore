package com.blogspot.explorespec.specexplore;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout loadMore;
    FloatingActionButton refresh;
    RecyclerView recyclerView;
    Toolbar tbar;
    LinearLayoutManager manager;
    PostAdapter adapter;
    List<Item> items = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkBlack));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.darkBlack));
        } else {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        loadMore = findViewById(R.id.loadMore);
        recyclerView = findViewById(R.id.postList);
        manager = new LinearLayoutManager(this);
        adapter = new PostAdapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        tbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            tbar.setBackgroundColor(getResources().getColor(R.color.darkBlack));
            tbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

            refresh = findViewById(R.id.faBtn);
            refresh.setBackgroundTintList(getResources().getColorStateList(R.color.darkBlack));
            refresh.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_white));
            refresh.setRippleColor(getResources().getColor(R.color.altered));
        }

        refresh = findViewById(R.id.faBtn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    recreate();
                } else {
                    Toast.makeText(MainActivity.this, "You are not connected to the Internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    getData();
                }
            }
        });
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drop_down_menu, menu);

        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            MenuItem shareItem = menu.findItem(R.id.share);
            shareItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_share_white));
            MenuItem aboutItem = menu.findItem(R.id.about);
            aboutItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_about_white));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                String appPackageName = getApplicationContext().getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Using"));
                break;

            case R.id.about:
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {

        String url = BloggerAPI.url + "?key=" + BloggerAPI.key;

        if (token != "") {
            url = url + "&pageToken=" + token;
        }

        if (token == null) {
            Toast.makeText(this, "No more posts avilable", Toast.LENGTH_SHORT).show();
            return;
        }

        loadMore.setVisibility(View.VISIBLE);
        Call<PostList> postList = BloggerAPI.getService().getPostList(url);
        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList list = response.body();
                token = list.getNextPageToken();
                items.addAll(list.getItems());
                adapter.notifyDataSetChanged();
                loadMore.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error while retrieving content. Please refresh!!", Toast.LENGTH_SHORT).show();
                loadMore.setVisibility(View.GONE);
            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void checkConnection() {
        if (!isOnline()) {
            Toast.makeText(MainActivity.this, "You are not connected to the Internet.", Toast.LENGTH_SHORT).show();
        }
    }
}
