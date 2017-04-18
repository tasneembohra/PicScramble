package com.tasneembohra.picscramble;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

/**
 *
 * Created by tasneem on 18/04/17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder>{
    private List<String> mImageList;
    private boolean flip[];
    private Context mContext;
    private int mIndex = -1;
    private Random mRandom;
    private int totalFlipped;

    public ImageAdapter(List<String> imageList, Context context) {
        mImageList = imageList;
        mContext = context;
        flip = new boolean[imageList.size()];
        mRandom = new Random();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String image = mImageList.get(position);
        if (flip[position]) {
            holder.flippedImageView.setVisibility(View.VISIBLE);
            holder.appCompatImageView.setVisibility(View.GONE);
        } else {
            holder.flippedImageView.setVisibility(View.GONE);
            holder.appCompatImageView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(image)) {
                Glide.with(mContext).load(image).error(android.R.drawable.ic_menu_report_image).into(holder.appCompatImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        AppCompatImageView appCompatImageView;
        AppCompatImageView flippedImageView;
        ItemViewHolder(View itemView) {
            super(itemView);
            appCompatImageView = (AppCompatImageView) itemView.findViewById(R.id.appCompatImageView);
            flippedImageView = (AppCompatImageView) itemView.findViewById(R.id.flippedImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mIndex == getAdapterPosition()) {
                totalFlipped --;
                Toast.makeText(mContext, "Awesome!", Toast.LENGTH_SHORT).show();
                flip[getAdapterPosition()] = false;
                notifyItemChanged(getAdapterPosition());
                if (totalFlipped > 0) showImage();
            }
        }
    }

    void flipAllImages() {
        for (int i = 0 ; i < flip.length ; i ++) {
            flip[i] = true;
        }
        totalFlipped = flip.length;
        notifyDataSetChanged();
        showImage();
    }

    private void showImage() {
        int index;
        while (true) {
            index = mRandom.nextInt(mImageList.size());
            if (flip[index]) {
                break;
            }
        }
        mIndex = index;
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_find_image);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        String image = mImageList.get(index);
        if (!TextUtils.isEmpty(image)) {
            Glide.with(mContext).load(image).error(android.R.drawable.ic_menu_report_image)
                    .into((AppCompatImageView) dialog.findViewById(R.id.image));
        }
        dialog.show();

        dialog.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
