package com.michael.wallpaper.fragment_list;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jesson.android.widget.Toaster;
import com.michael.wallpaper.R;
import com.michael.wallpaper.activity.GalleryActivity;
import com.michael.wallpaper.activity.MainActivity;
import com.michael.wallpaper.adapter.PhotoStreamListAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ContentListFragment extends Fragment implements OnRefreshListener {
    private static final String ARG_SERIES = "series";

    private Series mSeries;

    private ListView mListView;

    private PullToRefreshLayout mPullToRefreshLayout;

    private PhotoStreamListAdapter mPhotoStreamListAdapter;

    private BelleHelper mBelleHelper;

    private List<Belle> mBelles = new ArrayList<Belle>();

    private static final int PAGE_COUNT = 50;

    private int mPageStartIndex;

    private GetBelleListEvent mGetBelleListEvent;

    private boolean mLoadingMore;

    private OnFragmentInteractionListener mListener;

    public static ContentListFragment newInstance(Series series) {
        ContentListFragment fragment = new ContentListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SERIES, series);
        fragment.setArguments(args);
        return fragment;
    }
    public ContentListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSeries = (Series) getArguments().getSerializable(ARG_SERIES);
        }

        EventBus.getDefault().register(this);
        mBelleHelper = new BelleHelper(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content_list, container, false);
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

        mListView = (ListView) rootView.findViewById(R.id.listview);
        mPhotoStreamListAdapter = new PhotoStreamListAdapter(getActivity(), mBelles);
        mListView.setAdapter(mPhotoStreamListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> uriList = new ArrayList<String>();
                for (Belle belle : mBelles) {
                    uriList.add(belle.url);
                }
                ArrayList<String> rawUrlList = new ArrayList<String>();
                for (Belle belle : mBelles) {
                    rawUrlList.add(belle.rawUrl);
                }
                ArrayList<String> descList = new ArrayList<String>();
                for (Belle belle : mBelles) {
                    if (TextUtils.isEmpty(belle.desc)) {
                        descList.add("");
                    } else {
                        descList.add(belle.desc);
                    }
                }
                String tilte = mSeries.getTitle();
                if (!TextUtils.isEmpty(mSeries.getTag3())) {
                    tilte = mSeries.getTitle() + "-" + mSeries.getTag3();
                }
                GalleryActivity.startViewLarge(getActivity(), tilte, uriList, rawUrlList, descList, i);
            }
        });

        if (mSeries.getType() >= 0) {
            mBelleHelper.getBelleListFromLocal(mSeries.getType());
        }

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((totalItemCount - 5) < 0) return;

                if (firstVisibleItem >= (totalItemCount - 5)) {
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

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageLoader.getInstance().stop();
    }

    private List<Belle> loadCollectedBelles() {
        List<Belle> belles = new ArrayList<Belle>();
        CollectHelper helper = new CollectHelper(getActivity());
        List<CollectedBelle> collectedBelles = helper.loadAll();
        if (collectedBelles != null) {
            for (CollectedBelle collectedBelle : collectedBelles) {
                Belle belle = new Belle(0, collectedBelle.getTime(), -1, null, collectedBelle.getUrl(), null, 0, 0);
                belles.add(belle);
            }
        }
        return belles;
    }

    @Override
    public void onRefreshStarted(View view) {
        if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
            mPageStartIndex = mPageStartIndex + PAGE_COUNT;
        } else {
            mPageStartIndex = 0;
        }
        mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), mPageStartIndex, PAGE_COUNT, mSeries.getCategory(), mSeries.getTitle(), mSeries.getTag3());
        ((MainActivity) getActivity()).onRefresh();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            if (!mPullToRefreshLayout.isRefreshing()) {
                if (mGetBelleListEvent != null && mGetBelleListEvent.hasMore) {
                    mPageStartIndex = mPageStartIndex + PAGE_COUNT;
                } else {
                    mPageStartIndex = 0;
                }
                mPullToRefreshLayout.setRefreshing(true);
                mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), mPageStartIndex, PAGE_COUNT, mSeries.getCategory(), mSeries.getTitle(), mSeries.getTag3());
                ((MainActivity) getActivity()).onRefresh();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(GetBelleListEvent event) {
        if (event.contentType != mSeries.getType()) {
            return;
        }

        Toaster.cancel();
        mLoadingMore = false;
        mGetBelleListEvent = event;
        if (event.type == GetBelleListEvent.TYPE_SERVER_RANDOM) {
            if (event.startIndex == 0) {
                mBelles.clear();
            }

            if (event.belles != null) {
                mBelles.addAll(event.belles);
            }
            if (mPhotoStreamListAdapter == null || event.startIndex == 0) {
                String tilte = mSeries.getTitle();
                if (!TextUtils.isEmpty(mSeries.getTag3())) {
                    tilte = mSeries.getTitle() + "-" + mSeries.getTag3();
                }
                mPhotoStreamListAdapter = new PhotoStreamListAdapter(getActivity(), mBelles);
                mListView.setAdapter(mPhotoStreamListAdapter);
            } else {
                mPhotoStreamListAdapter.notifyDataChanged(event.belles);
            }
            mPullToRefreshLayout.setRefreshComplete();
        } else if (event.type == GetBelleListEvent.TYPE_LOCAL) {
            mBelles.clear();
            if (event.belles != null) {
                mBelles.addAll(event.belles);
            }
            mPhotoStreamListAdapter.notifyDataSetChanged();
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
    }

    public void onEventMainThread(NetworkErrorEvent event) {
        if (event.contentType != mSeries.getType()) {
            return;
        }

        Toaster.cancel();
        mLoadingMore = false;

        if (mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    public void onEventMainThread(ServerErrorEvent event) {
        if (event.contentType != mSeries.getType()) {
            return;
        }

        Toaster.cancel();
        mLoadingMore = false;

        if (mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

}
