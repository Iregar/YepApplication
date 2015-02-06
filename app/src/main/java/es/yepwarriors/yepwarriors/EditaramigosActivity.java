package es.yepwarriors.yepwarriors;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class EditaramigosActivity extends ListActivity {

        final static String TAG = EditaramigosActivity.class.getName();
    ProgressBar progressBar;
    List<ParseUser> mUsers;
    ArrayList <String> username;
    ArrayAdapter <String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaramigos);
         progressBar = (ProgressBar) findViewById(R.id.progressBar);
 getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(this,username.get(position),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);

        //recuperar todos los usuarios de la aplicacion PARSE

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.USERNAME);
        query.setLimit(ParseConstant.MAX_USERS);

        username = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,username);
        setListAdapter(adapter);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List <ParseUser> users, ParseException e) {
                if (e == null) {
                    mUsers = users;
                    for(ParseUser user:mUsers){
                        adapter.add(user.getUsername());
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Log.e(TAG, "mierda!!!", e);
                }
            }
        });
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editaramigos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
