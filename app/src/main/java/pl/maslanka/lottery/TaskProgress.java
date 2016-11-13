package pl.maslanka.lottery;

import android.app.Activity;

/**
 * Created by Artur on 14.10.2016.
 */

public class TaskProgress  {

    final String message;

    TaskProgress(Long howMuchToWin, Activity activity) {
        this.message = activity.getResources().getString(R.string.done) + Long.toString(howMuchToWin) + activity.getResources().getString(R.string.drawings);
    }

}
