package barqsoft.footballscores.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.phrase.Phrase;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.database.ScoresContract;
import barqsoft.footballscores.database.ScoresContract.scores_table;
import barqsoft.footballscores.utils.Utils;

/**
 * Get the scores from the database and show them in the views
 * This is basically a remote ListView Adapter
 */
public class ScoresRemoteViewsService extends RemoteViewsService {
    public static final String TAG = ScoresRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //Create a RemoteViewsFactory, load its views with scores, and return
        return new RemoteViewsFactory() {
            Cursor mCursor;
            final String[] PROJECTION = {
                    scores_table.LEAGUE_COL,
                    scores_table.HOME_COL,
                    scores_table.AWAY_COL,
                    scores_table.HOME_GOALS_COL,
                    scores_table.AWAY_GOALS_COL,
                    scores_table.MATCH_DAY,
                    scores_table.MATCH_ID
            };

            final static int COL_ID_LEAGUE = 0;
            final static int COL_ID_HOMENAME = 1;
            final static int COL_ID_AWAYNAME = 2;
            final static int COL_ID_HOME_GOALS = 3;
            final static int COL_ID_AWAY_GOALS = 4;
            final static int COL_ID_MATCHDAY = 5;
            final static int COL_ID_MATCHID = 6;

            @Override
            public void onCreate() {
                Log.i(TAG, "onCreate");
            }

            @Override
            public void onDataSetChanged() {
                Log.i(TAG, "onDataSetChanged");
                if (mCursor != null) {
                    mCursor.close();
                }
                //Binding Context to the correct process
                //http://stackoverflow.com/questions/9497270/widget-with-content-provider-impossible-to-use-readpermission
                final long identityToken = Binder.clearCallingIdentity();
                Uri uri = ScoresContract.scores_table.buildScoreWithDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String today = dateFormat.format(new Date());
                Log.i(TAG,"Widget displaying scores for " + today);
                mCursor = getContentResolver().query(uri,
                        PROJECTION,
                        null,
                        new String[]{today},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                Log.i(TAG, "onDestroy");
                if (mCursor != null) {
                    mCursor.close();
                    mCursor = null;
                }
            }

            @Override
            public int getCount() {
                Log.i(TAG, "getCount");
                if (mCursor == null)
                    return 0;
                else
                    return mCursor.getCount();
            }

            /**
             * Get and display the scores for this cursor position
             *
             * Example:
             * https://android.googlesource.com/platform/development/+/master
             *       /samples/WeatherListWidget/src/com
             *       /example/android/weatherlistwidget/WeatherWidgetService.java
             *
             */
            @Override
            public RemoteViews getViewAt(int position) {
                //If position is invalid, cursor is null, or can't move
                //cursor to position, return null
                if (position == AdapterView.INVALID_POSITION ||
                        mCursor == null || !mCursor.moveToPosition(position)) {
                    return null;
                }
                //Otherwise get views and display the scores
                final RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_scores_item);
                String league = Utils.getLeague(getResources(),
                        mCursor.getInt(COL_ID_LEAGUE));
               String matchDay = Utils.getMatchDay(mCursor.getInt(COL_ID_MATCHDAY),
                       mCursor.getInt(COL_ID_LEAGUE));

                String homeName = mCursor.getString(COL_ID_HOMENAME);
                String awayName = mCursor.getString(COL_ID_AWAYNAME);
                Log.d(TAG,"Widget scores item " + homeName + " " + awayName);
                String score = Utils.getScores(mCursor.getInt(COL_ID_HOME_GOALS),
                        mCursor.getInt(COL_ID_AWAY_GOALS));

                views.setTextViewText(R.id.widget_league, league);
                views.setImageViewResource(R.id.widget_home_crest, Utils.getTeamCrestByTeamName(homeName));
                views.setImageViewResource(R.id.widget_away_crest, Utils.getTeamCrestByTeamName(awayName));
                views.setTextViewText(R.id.widget_home_name, homeName);
                views.setTextViewText(R.id.widget_away_name, awayName);
                views.setTextViewText(R.id.widget_score, score);
                views.setTextViewText(R.id.widget_matchday, matchDay);

                views.setContentDescription(R.id.widget_home_crest, homeName);
                views.setContentDescription(R.id.widget_away_crest, awayName);
                views.setContentDescription(R.id.widget_home_name, homeName);
                views.setContentDescription(R.id.widget_away_name, awayName);
                views.setContentDescription(R.id.widget_matchday, matchDay);
                views.setContentDescription(R.id.widget_score, score);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                Log.i(TAG, "getLoadingView");
                return null;
            }

            @Override
            public int getViewTypeCount() {
                Log.i(TAG, "getViewTypeCount");
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (mCursor.moveToPosition(position))
                    return mCursor.getLong(COL_ID_MATCHID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
