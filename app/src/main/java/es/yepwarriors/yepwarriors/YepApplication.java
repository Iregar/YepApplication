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

        Parse.initialize(this, "KBQtwtA2c5lNQxlaOOwojZArFUUqWR6lq6ZV6Sey", "Yf2tBH4WBUy1G96ZpQvElpJbR8mo4iG3FkRld4P3");
    }
}
