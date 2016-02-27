package barqsoft.footballscores;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhi on 2/24/16.
 */
public class WidgetScore implements Parcelable{

    public static final String WIDGET_SCORE_KEY = "widget_score_key";

    private final String LEAGUE = "league";
    private final String HOME = "home";
    private final String AWAY = "away";
    private final String HOME_SCORE = "home_score";
    private final String AWAY_SCORE = "away_score";

    private String league;
    private String home;
    private String away;
    private int home_goal;
    private int away_goal;

    public WidgetScore(String league, String home, String away, int home_goal, int away_goal) {
        this.league = league;
        this.home = home;
        this.away = away;
        this.home_goal = home_goal;
        this.away_goal = away_goal;
    }

    protected WidgetScore(Parcel in) {
        Bundle bundle = in.readBundle();
        league = bundle.getString(LEAGUE);
        home = bundle.getString(HOME);
        away = bundle.getString(AWAY);
        home_goal = bundle.getInt(HOME_SCORE);
        away_goal = bundle.getInt(AWAY_SCORE);
    }

    public static final Creator<WidgetScore> CREATOR = new Creator<WidgetScore>() {
        @Override
        public WidgetScore createFromParcel(Parcel in) {
            return new WidgetScore(in);
        }

        @Override
        public WidgetScore[] newArray(int size) {
            return new WidgetScore[size];
        }
    };

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public int getHome_goal() {
        return home_goal;
    }

    public void setHome_goal(int home_goal) {
        this.home_goal = home_goal;
    }

    public int getAway_goal() {
        return away_goal;
    }

    public void setAway_goal(int away_goal) {
        this.away_goal = away_goal;
    }

    @Override
    public String toString() {
        return league + "\n" +
                home + "\n" +
                away + "\n" +
                home_goal + "\n" +
                away_goal + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle bundle = new Bundle();
        bundle.putString(LEAGUE, league);
        bundle.putString(HOME, home);
        bundle.putString(AWAY, away);
        bundle.putInt(HOME_SCORE, home_goal);
        bundle.putInt(AWAY_SCORE, away_goal);

        dest.writeBundle(bundle);

    }
}
