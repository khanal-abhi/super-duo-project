package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abhi on 2/26/16.
 */
public class FootballScoreFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int appWidgetId;
    private Cursor cursor;

    public FootballScoreFactory(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }


    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        if(cursor != null){
            cursor.close();
        }

        String[] date = new String[1];
        Date filterDate = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date[0] = format.format(filterDate);
        try {
            cursor = context.getContentResolver().query(
                    Uri.parse("content://barqsoft.footballscores/date"),
                    null,
                    null,
                    date,
                    null
            );
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {

        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if(position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position))
            return null;

        String leagueName, home, away, homeGoal, awayGoal;
        leagueName = home = away = homeGoal = awayGoal = "";

        if(cursor.moveToPosition(position)){
            leagueName = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
            home = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
            away = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
            homeGoal = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)));
            awayGoal = String.valueOf(cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL)));
        }

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        rv.setTextViewText(R.id.league_name, leagueName);
        rv.setTextViewText(R.id.home, home);
        rv.setTextViewText(R.id.away, away);
        rv.setTextViewText(R.id.home_score, homeGoal.contentEquals("-1") ? "-" : homeGoal);
        rv.setTextViewText(R.id.away_score, awayGoal.contentEquals("-1") ? "-" : awayGoal);

        // Crests
        rv.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(home));
        rv.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(away));

        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        rv.setOnClickPendingIntent(R.id.widget_clicker, pendingIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
