package com.michael.wallpaper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.michael.wallpaper.R;
import com.michael.wallpaper.api.belle.Belle;
import com.michael.wallpaper.views.STGVImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 14-8-31.
 */
public class PhotoStreamListAdapter extends BaseAdapter {

    private Context mContext;

    private List<Belle> mBelles = new ArrayList<Belle>();

    public PhotoStreamListAdapter(Context context, List<Belle> belles) {
        mContext = context;
        mBelles.addAll(belles);
    }

    public void notifyDataChanged(List<Belle> belles) {
        synchronized (this) {
            mBelles.clear();
            mBelles.addAll(belles);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        int count = (mBelles == null ? 0 : mBelles.size());
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_photo_stream, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String photoUri = mBelles.get(i).url;
        String desc = mBelles.get(i).desc;

        holder.photo.mWidth = mBelles.get(i).thumb_large_width;
        holder.photo.mHeight = mBelles.get(i).thumb_large_height;

        if (TextUtils.isEmpty(desc)) {
            holder.textView.setVisibility(View.GONE);
        } else {
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(desc);
        }

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

        return view;
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
