package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abhi on 2/26/16.
 */
public class FootballScoreFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<WidgetScore> scoreList;
    private Context context;
    private int appWidgetId;

    public FootballScoreFactory(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {

        String[] date = new String[1];
        scoreList = new ArrayList<>();

        for (int i = 0; i < 7; i++){
            Date fragmentdate = new Date(System.currentTimeMillis()+((i-2)*86400000));
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            date[0] = (mformat.format(fragmentdate));

            Cursor cursor = context.getContentResolver().query(
                    Uri.parse("content://barqsoft.footballscores/date"),
                    null,
                    null,
                    date,
                    null
            );

            if(cursor != null){
                cursor.moveToFirst();
                try {
                    do{
                        scoreList.add(new WidgetScore(
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.LEAGUE_COL)),
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL)),
                                cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL)),
                                cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)),
                                cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL))
                        ));
                    } while (cursor.moveToNext());
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }
            }
        }

        Log.v(getClass().getSimpleName(), scoreList.toString());

    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        rv.setTextViewText(R.id.league_name, scoreList.get(position).getLeague());
        rv.setTextViewText(R.id.home, scoreList.get(position).getHome());
        rv.setTextViewText(R.id.away, scoreList.get(position).getAway());
        rv.setTextViewText(R.id.home_score, scoreList.get(position).getHome_goal() == -1 ? "-" : String.valueOf(scoreList.get(position).getHome_goal()));
        rv.setTextViewText(R.id.away_score, scoreList.get(position).getAway_goal() == -1 ? "-" : String.valueOf(scoreList.get(position).getAway_goal()));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
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
