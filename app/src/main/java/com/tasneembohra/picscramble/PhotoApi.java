package com.tasneembohra.picscramble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 *
 * Created by tasneem on 18/04/17.
 */

class PhotoApi {
     private PhotoListener mListener;
     private interface Api {
        @GET("feeds/photos_public.gne")
        Call<ResponseBody> getPhotos(@QueryMap Map<String, Object> params);
     }
     interface PhotoListener {
        void onSuccess(List<String> imageList);
        void onFailure();
    }



     PhotoApi(PhotoListener listener) {
        mListener = listener;
     }

    /**
     * Fetch photos from flikr public api
     */
     void getPhotos() {
        Map<String, Object> map = new HashMap<>();
        map.put("api_key", "4f342a11fcb7da275d8eadc90db3fbb3");
        map.put("format", "json");
        map.put("nojsoncallback", 1);
        Api api = new Retrofit.Builder()
                .client(new OkHttpClient.Builder().build())
                .baseUrl("https://api.flickr.com/services/")
                .build().create(Api.class);
        api.getPhotos(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean isError = false;
                if (response != null && response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        JSONArray itemObjects = object.optJSONArray("items");
                        if (itemObjects != null && itemObjects.length() > 0) {
                            List<String> images = new ArrayList<>();
                            for (int i = 0 ; i < 9; i ++) {
                                images.add(itemObjects.optJSONObject(i).optJSONObject("media").optString("m"));
                            }
                            mListener.onSuccess(images);
                        } else {
                            isError = true;
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        isError = true;
                    }
                } else {
                    isError = true;
                }

                if (isError) {
                   mListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                mListener.onFailure();
            }
        });
    }
}
