package com.michael.wallpaper.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.deskclock.widget.sgv.GridAdapter;
import com.michael.wallpaper.R;
import com.michael.wallpaper.activity.GalleryActivity;
import com.michael.wallpaper.api.belle.Belle;
import com.michael.wallpaper.views.STGVImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdi on 14-3-4.
 */
public class StaggerPhotoStreamAdapter extends GridAdapter {

    private Activity mActivity;

    private List<Belle> mAdapterBelles = new ArrayList<Belle>();

    private String mTitle;

    public StaggerPhotoStreamAdapter(Activity activity, List<Belle> belles, String title) {
        mActivity = activity;
        mAdapterBelles.addAll(belles);
        mTitle = title;
    }

    public void notifyDataChanged(List<Belle> belles) {
        synchronized (this) {
            mAdapterBelles.addAll(belles);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        int count = (mAdapterBelles == null ? 0 : mAdapterBelles.size());
        return count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_stagger_photo_stream, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String photoUri = mAdapterBelles.get(i).url;
        String desc = mAdapterBelles.get(i).desc;

        holder.textView.setVisibility(View.VISIBLE);
        holder.textView.setText(desc);

        holder.photo.mWidth = mAdapterBelles.get(i).thumb_large_width;
        holder.photo.mHeight = mAdapterBelles.get(i).thumb_large_height;

        holder.progressBar.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage(photoUri, holder.photo, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });

        //set listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> uriList = new ArrayList<String>();
                for (Belle belle : mAdapterBelles) {
                    uriList.add(belle.url);
                }
                ArrayList<String> rawUrlList = new ArrayList<String>();
                for (Belle belle : mAdapterBelles) {
                    rawUrlList.add(belle.rawUrl);
                }
                ArrayList<String> descList = new ArrayList<String>();
                for (Belle belle : mAdapterBelles) {
                    if (TextUtils.isEmpty(belle.desc)) {
                        descList.add("");
                    } else {
                        descList.add(belle.desc);
                    }
                }
                GalleryActivity.startViewLarge(mActivity, mTitle, uriList, rawUrlList, descList, i);
            }
        });

        return view;
    }

    @Override
    public int getItemColumnSpan(Object item, int position) {
        return 1;
    }

    private static final class ViewHolder {

        public STGVImageView photo;

        public ProgressBar progressBar;

        public TextView textView;

        public ViewHolder(View rootView) {
            photo = (STGVImageView) rootView.findViewById(R.id.photo);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            textView = (TextView) rootView.findViewById(R.id.desc);
        }

    }
}
