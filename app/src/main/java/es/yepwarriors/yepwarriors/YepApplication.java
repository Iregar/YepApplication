package es.yepwarriors.yepwarriors;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


/**
 * Created by ivan on 16/01/15.
 */
public class YepApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "cgXn33mBRQ7HPq7nLZDDfHkNhAxVOYR9w35FsWQg", "ky8hxjuA374Q7cuGyP743S8PaDzd5Oyejorcuy9w");
    }
}
