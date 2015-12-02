package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.squareup.phrase.Phrase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.activities.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.ScoresFetchService;
//import barqsoft.footballscores.widget.ScoresRemoteViewsService;

public class ScoresWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Update all widgets (we just have one)
        for (int appWidgetId : appWidgetIds) {
            //Retrieve remote views
            //Update the widget
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_scores);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date(System.currentTimeMillis());
            CharSequence widgetTimestamp = Phrase.from(context, R.string.widget_title)
                    .put("timestamp", dateFormat.format(today))
                    .format();
            views.setTextViewText(R.id.scores_widget_title,widgetTimestamp);
            Intent openAppIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, 0);
            views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);

            views.setRemoteAdapter(R.id.scores_list, new Intent(context, ScoresRemoteViewsService.class));
            views.setEmptyView(R.id.scores_list, R.id.scores_empty);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }

    /**
     * Handle broadcast sent from ScoresFetchService.
     * Send scores to appWidgetManager to be displayed in the widget.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ScoresFetchService.BROADCAST_SCORES_UPDATED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.scores_list);
            this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
    }
}
