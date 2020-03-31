package com.fantech.novoid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//********************************************
public class ImageLoader
//********************************************
{
    private RequestManager mGlideRequestManager;


    //********************************************
    public interface AvatarBitmapLoadListener
            //********************************************
    {
        public void onAvatarLoadingFinished(Bitmap avatarBitmap);
    }


    //********************************************
    public interface AvatarDrawableLoadListener
            //********************************************
    {
        public void onAvatarDrawableLoadingFinished(Drawable avatarDrawable);
    }

    //********************************************
    public ImageLoader(@NonNull RequestManager requestManager)
    //********************************************
    {
        mGlideRequestManager = requestManager;
    }

    //********************************************
    public ImageLoader(@NonNull Context context)
    //********************************************
    {
        mGlideRequestManager = Glide.with(context);
    }


    //********************************************
    public void loadThumbnail(String url, @NonNull AvatarBitmapLoadListener avatarBitmapLoadListener)
    //********************************************
    {
        if (TextUtils.isEmpty(url))
            avatarBitmapLoadListener.onAvatarLoadingFinished(null);

        mGlideRequestManager.asBitmap()
                            .load(url)
                            .centerCrop()
                            .thumbnail(0.1f)
                            .into(new CustomTarget<Bitmap>()
                            {
                                //********************************************
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition)
                                //********************************************
                                {
                                    avatarBitmapLoadListener.onAvatarLoadingFinished(resource);
                                }

                                //********************************************
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder)
                                //********************************************
                                {
                                }

                                //********************************************
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable)
                                //********************************************
                                {
                                    super.onLoadFailed(errorDrawable);
                                    avatarBitmapLoadListener.onAvatarLoadingFinished(null);
                                }
                            });
    }

    //********************************************
    public void loadThumbnail(String url, @NonNull ImageView imageView, @NonNull ImageLoadedLister imageLoadedLister)
    //********************************************
    {
        if (TextUtils.isEmpty(url))
            imageLoadedLister.onImageLoaded(false);

        mGlideRequestManager.asBitmap()
                            .load(url)
                            .centerCrop()
                            .thumbnail(0.1f)
                            .into(new CustomTarget<Bitmap>()
                            {
                                //********************************************
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition)
                                //********************************************
                                {
                                    imageView.setImageBitmap(resource);
                                    if (imageLoadedLister != null)
                                        imageLoadedLister.onImageLoaded(true);
                                }

                                //********************************************
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder)
                                //********************************************
                                {
                                }

                                //********************************************
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable)
                                //********************************************
                                {
                                    super.onLoadFailed(errorDrawable);
                                    if (imageLoadedLister != null)
                                        imageLoadedLister.onImageLoaded(false);
                                }
                            });
    }



    //********************************************
    public void loadThumbnail(String url, @NonNull ImageView imageView, @NonNull Drawable failedDrawable)
    //********************************************
    {
        if (TextUtils.isEmpty(url))
        {
            imageView.setImageDrawable(failedDrawable);
            return;
        }

        mGlideRequestManager.load(url)
                            .centerCrop()
                            .thumbnail(0.1f)
                            .into(new CustomTarget<Drawable>()
                            {
                                //********************************************
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
                                //********************************************
                                {
                                    imageView.setImageDrawable(resource);
                                }

                                //********************************************
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder)
                                //********************************************
                                {

                                }

                                //********************************************
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable)
                                //********************************************
                                {
                                    super.onLoadFailed(errorDrawable);
                                    imageView.setImageDrawable(failedDrawable);
                                }
                            });
    }

    //********************************************
    public void loadImage(String url, @NonNull ImageView imageView, @NonNull Drawable failedDrawable)
    //********************************************
    {
        if (TextUtils.isEmpty(url))
        {
            imageView.setImageDrawable(failedDrawable);
            return;
        }

        mGlideRequestManager.load(url)
                            .thumbnail(0.1f)
                            .centerCrop()
                            .into(new CustomTarget<Drawable>()
                            {
                                //********************************************
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition)
                                //********************************************
                                {
                                    imageView.setImageDrawable(resource);
                                }

                                //********************************************
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder)
                                //********************************************
                                {

                                }

                                //********************************************
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable)
                                //********************************************
                                {
                                    super.onLoadFailed(errorDrawable);
                                    imageView.setImageDrawable(failedDrawable);
                                }
                            });
    }
    public interface ImageLoadedLister
    {
        void onImageLoaded(boolean isImageLoaded);
    }

}
