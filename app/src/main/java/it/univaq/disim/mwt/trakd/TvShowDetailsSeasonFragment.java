package it.univaq.disim.mwt.trakd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.univaq.disim.mwt.trakd.adapters.RecyclerViewSeasonCardAdapter;
import it.univaq.disim.mwt.trakd.model.Season;

public class TvShowDetailsSeasonFragment extends Fragment {
    private static final String ARG_SEASONS = "seasons";

    private ArrayList<Season> data;
    private RecyclerView recyclerView;
    private RecyclerViewSeasonCardAdapter recyclerViewSeasonCardAdapter;

    public TvShowDetailsSeasonFragment() {
        // Required empty public constructor
    }

    public static TvShowDetailsSeasonFragment newInstance(ArrayList<Season> seasons) {
        TvShowDetailsSeasonFragment fragment = new TvShowDetailsSeasonFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SEASONS, seasons);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (ArrayList<Season>) getArguments().getSerializable(ARG_SEASONS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerViewSeasonCardAdapter = new RecyclerViewSeasonCardAdapter(getContext(), data);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recyclerViewSeasonCardAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show_details_seasons, container, false);

        recyclerView = view.findViewById(R.id.seasons_recycle_view);

        // Inflate the layout for this fragment
        return view;
    }
}
