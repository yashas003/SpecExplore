package com.blogspot.explorespec.specexplore;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class BloggerAPI {
    public static final String key = "AIzaSyD4_FvBP0PY_rGXgqXVXPVEIlwDgQpomoU";
    public static final String url = "https://www.googleapis.com/blogger/v3/blogs/4943595248546275073/posts/";

    public static PostService postService = null;

    public static PostService getService() {
        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);
        }
        return postService;
    }

    public interface PostService {
        @GET
        Call<PostList> getPostList(@Url String url);
    }
}
