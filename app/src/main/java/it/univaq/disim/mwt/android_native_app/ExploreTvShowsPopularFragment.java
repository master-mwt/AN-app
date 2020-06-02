package it.univaq.disim.mwt.android_native_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.api.TMDB;
import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;
import it.univaq.disim.mwt.android_native_app.services.DataParserService;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreTvShowsPopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreTvShowsPopularFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<TvShowPreview> data = new ArrayList<>();
    private RecyclerViewCardAdapter recyclerViewCardAdapter;
    private RecyclerView recyclerView;
    private int page;
    private ProgressBar progressBar;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                switch (action){
                    case DataParserService.FILTER_PARSE_TV_SHOWS_POPULAR:
                        progressBar.setVisibility(View.INVISIBLE);
                        data.addAll(intent.<TvShowPreview>getParcelableArrayListExtra(DataParserService.EXTRA));
                        recyclerViewCardAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public ExploreTvShowsPopularFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreTvShowsPopularFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreTvShowsPopularFragment newInstance(String param1, String param2) {
        ExploreTvShowsPopularFragment fragment = new ExploreTvShowsPopularFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        page = 1;
        data.clear();

        recyclerViewCardAdapter = new RecyclerViewCardAdapter(getContext(), data);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recyclerViewCardAdapter);

        IntentFilter intentFilter = new IntentFilter(DataParserService.FILTER_PARSE_TV_SHOWS_POPULAR);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, intentFilter);

        TMDB.requestRemoteTvShowsPopular(getContext(), page);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    progressBar.setVisibility(View.VISIBLE);
                    page++;
                    TMDB.requestRemoteTvShowsPopular(getContext(), page);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_tv_shows_popular, container, false);

        recyclerView = view.findViewById(R.id.explore_tv_shows_populars_recycle_view);
        progressBar = view.findViewById(R.id.recycler_view_progress);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }
}
