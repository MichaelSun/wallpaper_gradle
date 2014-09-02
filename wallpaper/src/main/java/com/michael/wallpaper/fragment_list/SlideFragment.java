package com.michael.wallpaper.fragment_list;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jesson.android.Jess;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.R;
import com.michael.wallpaper.dao.model.Series;
import com.michael.wallpaper.helper.SeriesHelper;
import com.michael.wallpaper.utils.AppRuntime;
import com.michael.wallpaper.views.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlideFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SlideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideFragment extends Fragment {

    static class SamplePagerItem {
        private final String mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;
        private final Series mSeries;

        SamplePagerItem(String title, int indicatorColor, int dividerColor, Series series) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
            mSeries = series;
        }

        android.support.v4.app.Fragment createFragment() {
            return ContentListFragment.newInstance(mSeries);
        }

        CharSequence getTitle() {
            return mTitle;
        }

        int getIndicatorColor() {
            return mIndicatorColor;
        }

        int getDividerColor() {
            return mDividerColor;
        }
    }

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    private String mTitle;

    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private List<Series> mSeriesList;

    private OnFragmentInteractionListener mListener;

    public static SlideFragment newInstance(String mapType) {
        Jess.LOGD("[[SlideFragment::newInstance]] type = " + mapType);

        SlideFragment fragment = new SlideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mapType);
        fragment.setArguments(args);
        return fragment;
    }

    public SlideFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Jess.LOGD("[[SlideFragment::onCreate]]");

        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            if (!TextUtils.isEmpty(mTitle)) {
                mSeriesList = SeriesHelper.getInstance().getSeriesListByType(mTitle);
            }
        }
        if (!AppConfig.GAOXIAO_WALLPAPER_PACKAGE_NAMMME.equals(AppRuntime.PACKAGE_NAME)) {
            mSeriesList = SeriesHelper.getInstance().getSeriesList();
        }

        Jess.LOGD("[[SlideFragment::onCreate]] series list = " + mSeriesList
                      + " Title : " + mTitle);

        if (mSeriesList != null) {
            for (Series series : mSeriesList) {
                mTabs.add(new SamplePagerItem(series.getTitle()
                                                 , getActivity().getResources().getColor(R.color.indicator)
                                                 , Color.GRAY, series));
            }
        }

        if (!TextUtils.isEmpty(mTitle) && mListener != null) {
            mListener.onFragmentInteraction(mTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Jess.LOGD("[[SlideFragment::onCreateView]]");
        return inflater.inflate(R.layout.fragment_slide, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Jess.LOGD("[[SlideFragment::onViewCreated]]");

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.table_textview, R.id.table_tv);
        mSlidingTabLayout.setViewPager(mViewPager);

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }

        });
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        Jess.LOGD("[[SlideFragment::onAttach]] title = " + mTitle);

        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                             + " must implement OnFragmentInteractionListener");
        }

        if (!TextUtils.isEmpty(mTitle)) {
            mListener.onFragmentInteraction(mTitle);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String data);
    }

    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }

    }

}
