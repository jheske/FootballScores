package barqsoft.footballscores.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.database.ScoresContract;
import barqsoft.footballscores.database.ScoresContract.scores_table;
import barqsoft.footballscores.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoresRVAdapter extends RecyclerView.Adapter<ScoresRVAdapter.ScoresViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context mContext;
    private Cursor mCursor;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    public ScoresRVAdapter(Context context) {
        super();
        mContext = context;
    }

    public class ScoresViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.home_name)
        public TextView tvHomeName;
        @Bind(R.id.away_name)
        public TextView tvAwayName;
        @Bind(R.id.score_textview)
        public TextView tvScore;
        @Bind(R.id.date_textview)
        public TextView tvDate;
        @Bind(R.id.home_crest)
        public ImageView ivHomeCrest;
        @Bind(R.id.away_crest)
        public ImageView ivAwayCrest;

        ViewGroup detailsContainer;
        public double match_id;

        public ScoresViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            detailsContainer = (ViewGroup) itemView.findViewById(R.id.details_fragment_container);
        }
    }

    @Override
    public ScoresViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.scores_card_item, viewGroup, false);
        return new ScoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ScoresViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.tvHomeName.setText(mCursor.getString(scores_table.COL_ID_HOMENAME));
        holder.tvAwayName.setText(mCursor.getString(scores_table.COL_ID_AWAYNAME));
        holder.tvDate.setText(mCursor.getString(scores_table.COL_ID_MATCHTIME));
        holder.tvScore.setText(Utils.getScores(mCursor.getInt(scores_table.COL_ID_HOME_GOALS),
                mCursor.getInt(scores_table.COL_ID_AWAY_GOALS)));
        holder.match_id = mCursor.getDouble(scores_table.COL_ID_MATCHID);
        holder.ivHomeCrest.setImageResource(Utils.getTeamCrestByTeamName(
                mCursor.getString(scores_table.COL_ID_HOMENAME)));
        holder.ivAwayCrest.setImageResource(Utils.getTeamCrestByTeamName(
                mCursor.getString(scores_table.COL_ID_AWAYNAME)));
        //If this is the selected item, show more details
        LayoutInflater vi = (LayoutInflater) mContext.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        if(holder.match_id == detail_match_id)
        {
            Log.v(TAG, " insert extra details view");

            holder.detailsContainer.addView(v, 0,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                               ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utils.getMatchDay(mCursor.getInt(scores_table.COL_ID_MATCHDAY),
                    mCursor.getInt(scores_table.COL_ID_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utils.getLeague(mContext.getResources(),mCursor.getInt(scores_table.COL_ID_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    mContext.startActivity(createShareScoresIntent(holder.tvHomeName.getText() + " "
                            + holder.tvScore.getText()
                            + " " + holder.tvAwayName.getText() + " "));
                }
            });
        }
        else {
            holder.detailsContainer.removeAllViews();
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public int getMatchId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getInt(scores_table.COL_ID_MATCHID);
    }

    public void moveCursorToPosition(int position) {
        mCursor.moveToPosition(position);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public Intent createShareScoresIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}

