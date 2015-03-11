package es.yepwarriors.yepwarriors;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.ParseUser;

import es.yepwarriors.yepwarriors.constants.Constantes;
import es.yepwarriors.yepwarriors.ui.MainActivity;


/**
 * Created by ivan on 16/01/15.
 */
public class YepApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "cgXn33mBRQ7HPq7nLZDDfHkNhAxVOYR9w35FsWQg", "ky8hxjuA374Q7cuGyP743S8PaDzd5Oyejorcuy9w");

        PushService.setDefaultPushCallback(this, MainActivity.class,R.drawable.ic_stat_ic_launcher);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
    public static void updateParseInstallation (ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        //installation.put(Constantes.ParseClasses.Messages.KEY_USER_ID,user.getObjectId());
        if (user == null) {
            installation.remove(Constantes.ParseClasses.Messages.KEY_USER_ID);
        } else {
            installation.put(Constantes.ParseClasses.Messages.KEY_USER_ID, ParseUser.getCurrentUser());
        }
        installation.saveInBackground();
    }
}
