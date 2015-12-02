package barqsoft.footballscores.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.database.ScoresContract;
import barqsoft.footballscores.adapters.ScoresRVAdapter;
import barqsoft.footballscores.adapters.ScoresRVTouchListener;
import barqsoft.footballscores.service.ScoresFetchService;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String TAG=getClass().getSimpleName();
    private static final String DATE_INDEX_EXTRA = "DATE_INDEX";
    public ScoresRVAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String fragmentdate = "";
    private int last_selected_item = -1;
    @Bind(R.id.scores_recycler)
    public RecyclerView mRvScores;

    /**
     * Set up interface to handle onClick
     * This could also handle have methods to handle
     * onLongPress, or other gestures.
     */
    public interface ScoresClickListener {
        void onClick(View view, int position);
    }

   /* public MainActivityFragment() {
    } */

    public static MainActivityFragment newInstance(int i) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DATE_INDEX_EXTRA, i);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        if (getArguments() != null) {
            int i = getArguments().getInt(DATE_INDEX_EXTRA);
            setFragmentDate(i);
            Log.d(TAG, "   date " + Integer.toString(i) + " = " + fragmentdate);
        }
        else
            Log.d(TAG, "No args!!!!");
        update_scores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        mAdapter = new ScoresRVAdapter(getActivity());
        mRvScores.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvScores.setAdapter(mAdapter);
        mRvScores.addOnItemTouchListener(new ScoresRVTouchListener(getActivity(),
                mRvScores, new ScoresClickListener() {
            /**
             * onClick called back from the GestureDetector (ScoresRVTouchListener.java)
             */
            @Override
            public void onClick(View view, int position) {
                int selected_match_id = mAdapter.getMatchId(position);
                mAdapter.detail_match_id = selected_match_id;
                MainActivity.selected_match_id = selected_match_id;
                mAdapter.notifyDataSetChanged();
            }
        }));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG,"onActivityCreated()");
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void update_scores() {
        Intent service_start = new Intent(getActivity(), ScoresFetchService.class);
        getActivity().startService(service_start);
    }

    public void setFragmentDate(int i) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date mydate = new Date(System.currentTimeMillis() + ((i- ScoresFetchService.DAYS_BACK) * 86400000));
        fragmentdate = dateformat.format(mydate);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] selectionArgs = {fragmentdate};

        Log.i(TAG,"Query for date " + fragmentdate);

        return new CursorLoader(getActivity(),
                ScoresContract.scores_table.buildScoreWithDate(),
                null, null,
                selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //Log.i(TAG, "Query returned " + cursor.getCount() + " records");
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
