package barqsoft.footballscores;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by abhi on 2/26/16.
 */
public class FootballScoreService extends RemoteViewsService{



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballScoreFactory(getApplicationContext(), intent);
    }
}
