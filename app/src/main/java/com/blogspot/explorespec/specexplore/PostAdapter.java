package com.blogspot.explorespec.specexplore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Item> items;

    public PostAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.postTitle.setText(item.getTitle());

        Document document = Jsoup.parse(item.getContent());
        Elements elements = document.select("img");
        Picasso
                .get()
                .load(elements.get(0).attr("src"))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert holder.postImage != null;
                        holder.postImage.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getDominantSwatch();

                                        holder.background.setCardBackgroundColor(textSwatch.getRgb());
                                        holder.postTitle.setTextColor(textSwatch.getTitleTextColor());
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("url", item.getUrl());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "You are not connected to the Internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        CardView background;
        ImageView postImage;
        TextView postTitle;

        public PostViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            postImage = itemView.findViewById(R.id.postImage);
            postTitle = itemView.findViewById(R.id.postTitle);
        }
    }
}
