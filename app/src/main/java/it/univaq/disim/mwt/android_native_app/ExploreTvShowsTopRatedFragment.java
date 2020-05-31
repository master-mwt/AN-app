package it.univaq.disim.mwt.android_native_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.univaq.disim.mwt.android_native_app.model.TvShowPreview;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreTvShowsTopRatedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreTvShowsTopRatedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<TvShowPreview> data = new ArrayList<>();
    private recyclerViewCardAdapter recyclerViewCardAdapter;
    private RecyclerView recyclerView;

    public ExploreTvShowsTopRatedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreTvShowsTopRatedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreTvShowsTopRatedFragment newInstance(String param1, String param2) {
        ExploreTvShowsTopRatedFragment fragment = new ExploreTvShowsTopRatedFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_tv_shows_top_rated, container, false);

        TvShowPreview tvShowPreview1 = new TvShowPreview();
        TvShowPreview tvShowPreview2 = new TvShowPreview();
        TvShowPreview tvShowPreview3 = new TvShowPreview();
        TvShowPreview tvShowPreview4 = new TvShowPreview();
        tvShowPreview1.setName("item1");
        tvShowPreview2.setName("item2");
        tvShowPreview3.setName("item3");
        tvShowPreview4.setName("item4");
        data.add(tvShowPreview1);
        data.add(tvShowPreview2);
        data.add(tvShowPreview3);
        data.add(tvShowPreview4);
        recyclerViewCardAdapter = new recyclerViewCardAdapter(getContext(), data);

        recyclerView = view.findViewById(R.id.explore_tv_shows_top_rated_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(recyclerViewCardAdapter);

        return view;
    }
}
