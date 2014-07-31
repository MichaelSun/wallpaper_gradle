package com.michael.wallpaper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.android.deskclock.widget.sgv.SgvAnimationHelper;
import com.android.deskclock.widget.sgv.StaggeredGridView;
import com.jesson.android.widget.Toaster;
import com.michael.wallpaper.R;
import com.michael.wallpaper.activity.MainActivity;
import com.michael.wallpaper.adapter.StaggerPhotoStreamAdapter;
import com.michael.wallpaper.api.belle.Belle;
import com.michael.wallpaper.dao.model.CollectedBelle;
import com.michael.wallpaper.dao.model.Series;
import com.michael.wallpaper.event.GetBelleListEvent;
import com.michael.wallpaper.event.NetworkErrorEvent;
import com.michael.wallpaper.event.ServerErrorEvent;
import com.michael.wallpaper.helper.BelleHelper;
import com.michael.wallpaper.helper.CollectHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import de.greenrobot.event.EventBus;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class StaggerPhotoStreamFragment extends Fragment implements OnRefreshListener {

    private static final String ARG_SERIES = "series";

    private PullToRefreshLayout mPullToRefreshLayout;

    private StaggeredGridView mGridView;

//    private ListView mListView;

    private StaggerPhotoStreamAdapter mPhotoStreamAdapter;

    private List<Belle> mBelles = new ArrayList<Belle>();

    private Series mSeries;

    private BelleHelper mBelleHelper;

    private static final int PAGE_COUNT = 50;

    private int mPageStartIndex;

    private View mFooterView;

    private GetBelleListEvent mGetBelleListEvent;

    private boolean mLoadingMore;

    public static StaggerPhotoStreamFragment newInstance(Series series) {
        StaggerPhotoStreamFragment fragment = new StaggerPhotoStreamFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SERIES, series);
        fragment.setArguments(args);
        return fragment;
    }

    public StaggerPhotoStreamFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSeries = (Series) getArguments().getSerializable(ARG_SERIES);
        ((MainActivity) activity).onSectionAttached(mSeries);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mBelleHelper = new BelleHelper(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stagger_photo_stream, container, false);

        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.SetupWizard setupWizard = ActionBarPullToRefresh.from(getActivity())
                                                             .allChildrenArePullable()
                                                             .listener(this);
        if (mSeries.getType() >= 0) {
            setupWizard.allChildrenArePullable();
        } else if (mSeries.getType() == -1) {
            setupWizard.theseChildrenArePullable(0);
            List<Belle> list = loadCollectedBelles();
            if (list != null) {
                mBelles.addAll(list);
            }
        }

        setupWizard.setup(mPullToRefreshLayout);

        mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
        int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);

        mGridView.setItemMargin(margin);
        mGridView.setPadding(margin, 0, margin, 0);

//        mFooterView = inflater.inflate(R.layout.layout_loading_footer, null);
//        mGridView.setFooterView(mFooterView);

//        mGridView.setOnLoadmoreListener(new StaggeredGridView.OnLoadmoreListener() {
//            @Override
//            public void onLoadmore() {
//                if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
//                    mPageStartIndex = mPageStartIndex + PAGE_COUNT;
//
//                    mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), mPageStartIndex,
//                                                                 PAGE_COUNT, mSeries.getCategory(),
//                                                                 mSeries.getTitle(), mSeries.getTag3());
//                }
//            }
//        });

        String tilte = mSeries.getTitle();
        if (!TextUtils.isEmpty(mSeries.getTag3())) {
            tilte = mSeries.getTitle() + "-" + mSeries.getTag3();
        }
        mPhotoStreamAdapter = new StaggerPhotoStreamAdapter(getActivity(), mBelles, tilte);
        mGridView.setAdapter(mPhotoStreamAdapter);
        mGridView.setAnimationMode(SgvAnimationHelper.AnimationIn.SLIDE_IN_NEW_VIEWS
                                      , SgvAnimationHelper.AnimationOut.SLIDE);
        mGridView.setScrollListener(new StaggeredGridView.ScrollListener() {

            @Override
            public void onScrollChanged(int offset, int currentScrollY, int maxScrollY) {
//                Log.d("[[StaggerPhotoStreamFragment]]", "offset = " + offset + " currentScrollY = " + currentScrollY
//                            + " maxScrollY = " + maxScrollY);
                if (currentScrollY >= (maxScrollY - 8)) {
                    if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
                        if (!mLoadingMore) {
                            synchronized (this) {
                                mPageStartIndex = mPageStartIndex + PAGE_COUNT;
                                mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), mPageStartIndex, PAGE_COUNT,
                                                                             mSeries.getCategory(), mSeries.getTitle(), mSeries.getTag3());
                                mLoadingMore = true;
                            }
                        }

                        Toaster.show(getActivity(), getActivity().getString(R.string.loading_more));
                    } else {
                        Toaster.show(getActivity(), getActivity().getString(R.string.no_more));
                    }
                }
            }
        });

        if (mSeries.getType() >= 0) {
            mBelleHelper.getBelleListFromLocal(mSeries.getType());
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageLoader.getInstance().stop();
    }

    @Override
    public void onRefreshStarted(View view) {
//        if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
//            mPageStartIndex = mPageStartIndex + PAGE_COUNT;
//        } else {
        mPageStartIndex = 0;
//        }
        mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), mPageStartIndex,
                                                     PAGE_COUNT, mSeries.getCategory(),
                                                     mSeries.getTitle(), mSeries.getTag3());
        ((MainActivity) getActivity()).onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            if (!mPullToRefreshLayout.isRefreshing()) {
//                if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
//                    mPageStartIndex = mPageStartIndex + PAGE_COUNT;
//                } else {
                mPageStartIndex = 0;
//                }
                mPullToRefreshLayout.setRefreshing(true);
                mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), mPageStartIndex, PAGE_COUNT, mSeries.getCategory(), mSeries.getTitle(), mSeries.getTag3());
                ((MainActivity) getActivity()).onRefresh();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(GetBelleListEvent event) {
        mLoadingMore = false;
        Toaster.cancel();
        mGetBelleListEvent = event;
        if (event.type == GetBelleListEvent.TYPE_SERVER_RANDOM) {
            if (event.startIndex == 0) {
                mBelles.clear();
            }

            if (event.belles != null) {
                mBelles.addAll(event.belles);
            }
            if (mPhotoStreamAdapter == null || event.startIndex == 0) {
                String tilte = mSeries.getTitle();
                if (!TextUtils.isEmpty(mSeries.getTag3())) {
                    tilte = mSeries.getTitle() + "-" + mSeries.getTag3();
                }
                mPhotoStreamAdapter = new StaggerPhotoStreamAdapter(getActivity(), mBelles, tilte);
                mGridView.setAdapter(mPhotoStreamAdapter);
//                mListView.setAdapter(mPhotoStreamAdapter);
            } else {
                mPhotoStreamAdapter.notifyDataChanged(event.belles);
            }
            mPullToRefreshLayout.setRefreshComplete();
        } else if (event.type == GetBelleListEvent.TYPE_LOCAL) {
            mBelles.clear();
            if (event.belles != null) {
                mBelles.addAll(event.belles);
            }
            mPhotoStreamAdapter.notifyDataChanged(mBelles);
            // load from server
            if (mBelles.size() == 0) {
                if (!mPullToRefreshLayout.isRefreshing()) {
                    mGetBelleListEvent = null;
                    mPageStartIndex = 0;
                    mPullToRefreshLayout.setRefreshing(true);
                    mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), mPageStartIndex, PAGE_COUNT, mSeries.getCategory(), mSeries.getTitle(), mSeries.getTag3());
                    ((MainActivity) getActivity()).onRefresh();
                }
            }
        }

        if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
            mFooterView.setVisibility(View.VISIBLE);
        } else {
            mFooterView.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(NetworkErrorEvent event) {
        Toaster.cancel();
        mLoadingMore = false;
        if (mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.setRefreshComplete();
        }

        if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
            mFooterView.setVisibility(View.VISIBLE);
        } else {
            mFooterView.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(ServerErrorEvent event) {
        Toaster.cancel();
        mLoadingMore = false;
        if (mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.setRefreshComplete();
        }

        if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
            mFooterView.setVisibility(View.VISIBLE);
        } else {
            mFooterView.setVisibility(View.GONE);
        }
    }

    private List<Belle> loadCollectedBelles() {
        List<Belle> belles = new ArrayList<Belle>();
        CollectHelper helper = new CollectHelper(getActivity());
        List<CollectedBelle> collectedBelles = helper.loadAll();
        if (collectedBelles != null) {
            for (CollectedBelle collectedBelle : collectedBelles) {
                Belle belle = new Belle(0, collectedBelle.getTime(), -1, null, collectedBelle.getUrl(), null, collectedBelle.getWidth()
                                           , collectedBelle.getHeight());
                belles.add(belle);
            }
        }
        return belles;
    }

}
