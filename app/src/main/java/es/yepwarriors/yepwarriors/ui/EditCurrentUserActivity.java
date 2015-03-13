package es.yepwarriors.yepwarriors.ui;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;

import es.yepwarriors.yepwarriors.R;

public class EditCurrentUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_current_user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.home:
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
