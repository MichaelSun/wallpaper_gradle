package com.michael.wallpaper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.michael.wallpaper.R;
import com.michael.wallpaper.activity.MainActivity;
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
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardGridView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 14-6-26.
 */
public class CardPhotoStreamFragment extends Fragment implements OnRefreshListener {

    private static final String ARG_SERIES = "series";

    private PullToRefreshLayout mPullToRefreshLayout;

    private CardGridView mCardGridView;

    private CardGridArrayAdapter mCardGridArrayAdapter;

    private List<Belle> mBelles = new ArrayList<Belle>();

    private List<Card> mBelleCards = new ArrayList<Card>();

    private Series mSeries;

    private BelleHelper mBelleHelper;

    private static final int PAGE_COUNT = 50;

    public static CardPhotoStreamFragment newInstance(Series series) {
        CardPhotoStreamFragment fragment = new CardPhotoStreamFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SERIES, series);
        fragment.setArguments(args);
        return fragment;
    }

    public CardPhotoStreamFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_photo_stream_card, container, false);

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

        makeCardsByBelles(mBelles);
        mCardGridArrayAdapter = new CardGridArrayAdapter(getActivity(), mBelleCards);

        mCardGridView = (CardGridView) rootView.findViewById(R.id.card_grid);
        mCardGridView.setAdapter(mCardGridArrayAdapter);

//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ArrayList<String> uriList = new ArrayList<String>();
//                for (Belle belle : mBelles) {
//                    uriList.add(belle.url);
//                }
//                GalleryActivity.startViewLarge(getActivity(), mSeries.getTitle(), uriList, i);
//            }
//        });

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
        mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), PAGE_COUNT);
        ((MainActivity) getActivity()).onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            if (!mPullToRefreshLayout.isRefreshing()) {
                mPullToRefreshLayout.setRefreshing(true);
                mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), PAGE_COUNT);
                ((MainActivity) getActivity()).onRefresh();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(GetBelleListEvent event) {
        if (event.type == GetBelleListEvent.TYPE_SERVER_RANDOM) {
            mBelles.clear();
            if (event.belles != null) {
                mBelles.addAll(event.belles);
            }
            makeCardsByBelles(mBelles);
            mCardGridArrayAdapter = new CardGridArrayAdapter(getActivity(), mBelleCards);
            mCardGridView.setAdapter(mCardGridArrayAdapter);
            mPullToRefreshLayout.setRefreshComplete();
        } else if (event.type == GetBelleListEvent.TYPE_LOCAL) {
            mBelles.clear();
            if (event.belles != null) {
                mBelles.addAll(event.belles);
            }
            makeCardsByBelles(mBelles);
            mCardGridArrayAdapter.notifyDataSetChanged();
            // load from server
            if (mBelles.size() == 0) {
                if (!mPullToRefreshLayout.isRefreshing()) {
                    mPullToRefreshLayout.setRefreshing(true);
                    mBelleHelper.randomGetBelleListFromServer(mSeries.getType(), PAGE_COUNT);
                    ((MainActivity) getActivity()).onRefresh();
                }
            }
        }
    }

    public void onEventMainThread(NetworkErrorEvent event) {
        if (mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    public void onEventMainThread(ServerErrorEvent event) {
        if (mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    private void makeCardsByBelles(List<Belle> belles) {
        mBelleCards.clear();
        for (Belle belle : belles) {
            GplayGridCard card = new GplayGridCard(getActivity().getApplicationContext());
            card.headerTitle = "test";
            card.resourceIdThumbnail = belle.url;
            card.init();
            mBelleCards.add(card);
        }
    }

    private List<Belle> loadCollectedBelles() {
        List<Belle> belles = new ArrayList<Belle>();
        CollectHelper helper = new CollectHelper(getActivity());
        List<CollectedBelle> collectedBelles = helper.loadAll();
        if (collectedBelles != null) {
            for (CollectedBelle collectedBelle : collectedBelles) {
                Belle belle = new Belle(0, collectedBelle.getTime(), -1, collectedBelle.getUrl(), null);
                belles.add(belle);
            }
        }
        return belles;
    }

    private static final class ViewHolder {

        public ImageView photo;

        public ProgressBar progressBar;

        public ViewHolder(View rootView) {
            photo = (ImageView) rootView.findViewById(R.id.photo);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        }

    }

    public class GplayGridCard extends Card {

        protected String resourceIdThumbnail;

        protected String headerTitle;

        public GplayGridCard(Context context) {
//            super(context, R.layout.item_photo_stream);
            super(context);
        }

//        @Override
//        public void setupInnerViewElements(ViewGroup parent, View view) {
//            ViewHolder h = (ViewHolder) view.getTag();
//            if (h == null) {
//                h = new ViewHolder(view);
//                view.setTag(h);
//            }
//
//            final ViewHolder holder = h;
//
//            String photoUri = resourceIdThumbnail;
//
//            holder.progressBar.setVisibility(View.GONE);
//
//            ImageLoader.getInstance().displayImage(photoUri, holder.photo, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    holder.progressBar.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    holder.progressBar.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    holder.progressBar.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//                    holder.progressBar.setVisibility(View.GONE);
//                }
//            });
//        }

        private void init() {
//            CardHeader header = new CardHeader(getContext());
//            header.setButtonOverflowVisible(true);
//            header.setTitle(headerTitle);
//            header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
//                @Override
//                public void onMenuItemClick(BaseCard card, MenuItem item) {
//                    Toast.makeText(getContext(), "Item " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                }
//            });

//            addCardHeader(header);

            GplayGridThumb thumbnail = new GplayGridThumb(getContext());
            thumbnail.setUrlResource(resourceIdThumbnail);
            addCardThumbnail(thumbnail);
//
//            setOnClickListener(new OnCardClickListener() {
//                @Override
//                public void onClick(Card card, View view) {
//                    Do something
//                }
//            });
        }

        class GplayGridThumb extends CardThumbnail {

            public GplayGridThumb(Context context) {
                super(context);
            }

            @Override
            public void setupInnerViewElements(ViewGroup parent, View viewImage) {

                String photoUri = getUrlResource();
                if (!TextUtils.isEmpty(photoUri)) {
                    ImageLoader.getInstance().displayImage(photoUri, (ImageView) viewImage);
                }
            }
        }

    }

}
