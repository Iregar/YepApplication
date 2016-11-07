package es.yepwarriors.yepwarriors.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;

import android.view.MenuItem;
import android.widget.ImageView;
import android.net.Uri;
import com.squareup.picasso.Picasso;

import es.yepwarriors.yepwarriors.R;


public class ViewImageActivity extends ActionBarActivity {

    protected static long DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        ImageView image = (ImageView)findViewById(R.id.imageView);
        Uri uriImage = getIntent().getData();
        Picasso.with(this).load(uriImage.toString()).into(image);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        },DELAY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
