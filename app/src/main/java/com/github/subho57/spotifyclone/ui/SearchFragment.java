package com.github.subho57.spotifyclone.ui;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.subho57.spotifyclone.R;
import com.github.subho57.spotifyclone.manager.ListManager;
import com.github.subho57.spotifyclone.manager.PlaybackManager;
import com.github.subho57.spotifyclone.model.ArtistSearch;

import java.util.List;

import iammert.com.view.scalinglib.ScalingLayout;
import iammert.com.view.scalinglib.ScalingLayoutListener;
import iammert.com.view.scalinglib.State;

/**
 * Created by subho57
 */

public class SearchFragment extends Fragment {

    private static final String TAG = "Spotify SearchFragment";
    FragmentManager fragmentManager;
    private TextView textViewSearch;
    private EditText editTextSearch;
    private ScalingLayout scalingLayout;
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.search_text_button:
                    String query = editTextSearch.getText().toString();

                    Log.d(TAG, "Query: " + query);

                    scalingLayout.collapse();

                    if (query.equals("")) {
                        textViewSearch.setText("Search");
                    } else {

                        fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.add(R.id.fragment, SearchResultFragment.newInstance(query))
                                .addToBackStack(TAG)
                                .commit();


                        textViewSearch.setText(query);
                    }

                    break;
            }
        }
    };
    private RecyclerView searchListView;
    private ArtistListAdapter mAdapter;

    @SuppressLint("ResourceType")
    public static SearchFragment getFragmentInstance(FragmentManager fm, String tag) {
        SearchFragment fragment = (SearchFragment) fm.findFragmentByTag(tag);

        if (fragment == null) {
            fragment = new SearchFragment();

            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, fragment, tag).commitAllowingStateLoss();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // check if the previous state is SearchResultFragment
        PlaybackManager manager = PlaybackManager.getInstance();
        if (manager.isSearchResultFragmentAdded()) {
            fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment, SearchResultFragment.newInstance("empty"))
                    .addToBackStack(TAG)
                    .commit();
        }

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        textViewSearch = view.findViewById(R.id.textViewSearch);
        final RelativeLayout searchLayout = view.findViewById(R.id.searchLayout);
        final ImageButton searchButton = view.findViewById(R.id.search_text_button);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        searchButton.setOnClickListener(mListener);
        scalingLayout = view.findViewById(R.id.scalingLayout);
        searchListView = view.findViewById(R.id.Artist_search_list);

        searchListView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new ArtistListAdapter(ListManager.getInstance().getArtists());
        searchListView.setAdapter(mAdapter);

        scalingLayout.setListener(new ScalingLayoutListener() {
            @Override
            public void onCollapsed() {
                ViewCompat.animate(textViewSearch).alpha(1).setDuration(150).start();
                ViewCompat.animate(searchLayout).alpha(0).setDuration(150).setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        textViewSearch.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        searchLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                }).start();
            }

            @Override
            public void onExpanded() {
                ViewCompat.animate(textViewSearch).alpha(0).setDuration(200).start();
                ViewCompat.animate(searchLayout).alpha(1).setDuration(200).setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        searchLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        textViewSearch.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                }).start();
            }

            @Override
            public void onProgress(float progress) {

            }
        });

        scalingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scalingLayout.getState() == State.COLLAPSED) {
                    scalingLayout.expand();
                }
            }
        });

        view.findViewById(R.id.rootLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scalingLayout.getState() == State.EXPANDED) {
                    scalingLayout.collapse();

                    if (editTextSearch.getText().toString().equals(""))
                        textViewSearch.setText("Search");
                }
            }
        });

        return view;
    }

    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    private class ArtistListHolder extends RecyclerView.ViewHolder {
        private TextView artistName;
        private ImageView artistImage;
        private ArtistSearch artistSearch;

        private ArtistListHolder(View itemView) {
            super(itemView);

            artistImage = itemView.findViewById(R.id.search_artist_image_field);
            artistName = itemView.findViewById(R.id.search_artist_name);
        }

        private void bindArtist(ArtistSearch search) {
            artistSearch = search;

            artistName.setText(artistSearch.getName());
            artistImage.setImageBitmap(artistSearch.getImage());
        }
    }

    private class ArtistListAdapter extends RecyclerView.Adapter<ArtistListHolder> {
        private List<ArtistSearch> artistSearchList;

        private ArtistListAdapter(List<ArtistSearch> list) {
            artistSearchList = list;
        }

        @Override
        public ArtistListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            View view = layoutInflater.inflate(R.layout.artist_search, parent, false);

            return new ArtistListHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistListHolder holder, int position) {
            holder.bindArtist(artistSearchList.get(position));
        }

        @Override
        public int getItemCount() {
            return artistSearchList.size();
        }
    }
}































