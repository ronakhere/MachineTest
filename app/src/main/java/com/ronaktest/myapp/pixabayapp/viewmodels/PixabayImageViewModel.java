package com.ronaktest.myapp.pixabayapp.viewmodels;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ronaktest.myapp.R;
import com.ronaktest.myapp.pixabayapp.activities.DetailActivity;
import com.ronaktest.myapp.pixabayapp.models.PixabayImage;

public class PixabayImageViewModel extends BaseObservable {
    private PixabayImage pixabayImage;

    public PixabayImageViewModel(PixabayImage pixabayImage) {
        this.pixabayImage = pixabayImage;
    }

    public String getTags() {
        return pixabayImage.getTags();
    }

    public String getImageUrl() {
        return pixabayImage.getPreviewURL();
    }

    public String getHighResImageUrl() {
        return pixabayImage.getWebformatURL();
    }

    public String getLikes() {
        return pixabayImage.getLikes();
    }

    public String getComments() {
        return pixabayImage.getComments();
    }

    public String getFavorites() {
        return pixabayImage.getFavorites();
    }

    public String getUserName() {
        return pixabayImage.getUser();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)

                .into(view);
    }

    public View.OnClickListener openDetails() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), DetailActivity.class);
                String serialized = new Gson().toJson(pixabayImage);
                i.putExtra(DetailActivity.PIXABAY_IMAGE, serialized);
                v.getContext().startActivity(i);
            }
        };
    }
}
