package com.test.jwj.underMoon.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.global.UnderMoonApplication;

import java.util.Hashtable;


/**
 * Author:Hikin
 * Data:2016/12/12
 */

public class ImageUtils {
//    public static void load(Context context, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        if (!SharedPreferenceUtil.getNoImageState()) {
//            Glide.with(context).load(url).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
//        }
//    }
    private static RequestOptions cacheOptions = new RequestOptions()
                                                        .error(R.mipmap.ic_launcher)
                                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    public static void load(Activity activity, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {
                Glide.with(UnderMoonApplication.mContext).load(url).apply(cacheOptions).into(iv);
            }
//        }
    }


    public static void load(Context context, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(context).load(url).apply(cacheOptions).into(iv);
//        }
    }

//    public static void loadAll(Context context, String url, ImageView iv) {    //不缓存，全部从网络加载
//        Glide.with(context).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
//                .error(R.mipmap.ic_launcher)
//                .into(iv);
//    }
//
//    public static void loadAll(Activity activity, String url, ImageView iv) {    //不缓存，全部从网络加载
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            if (!activity.isDestroyed()) {
//                Glide.with(activity).load(url).error(R.mipmap.ic_launcher).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
//            }
//        }
//    }

    public static void setViewWidthByHeight(ImageView view) {
        final ImageView mv = view;
        final ViewTreeObserver observer = mv.getViewTreeObserver();


        final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {

                int width = mv.getMeasuredWidth();
                int height = mv.getMeasuredHeight();

                Log.e("tag", "width" + width);
                Log.e("tag", "height" + height);

                android.view.ViewGroup.LayoutParams lp = mv.getLayoutParams();
                lp.height = mv.getMeasuredWidth();
                mv.setLayoutParams(lp);

                final ViewTreeObserver vto1 = mv.getViewTreeObserver();

                //调用一次之后移除，不影响性能
                vto1.removeOnPreDrawListener(this);


                return true;
            }
        };

        observer.addOnPreDrawListener(preDrawListener);
    }

    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath,new Hashtable<String, String>());
            }else {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap == null) return null;

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static Bitmap getNail(String url){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(url);
        return mmr.getFrameAtTime();
    }
}
