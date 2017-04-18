package com.tasneembohra.picscramble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoApi.PhotoListener {

    private ImageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        new PhotoApi(this).getPhotos();
    }

    @Override
    public void onSuccess(List<String> imageList) {
        mAdapter = new ImageAdapter(imageList, this);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.flipAllImages();
            }
        }, 15000);

    }

    @Override
    public void onFailure() {
        mProgressBar.setVisibility(View.GONE);
        ((AppCompatTextView)findViewById(R.id.errorText)).setText("Error while loading image from Flikr.");
    }
}
