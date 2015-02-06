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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class EditarAmigosActivity extends ListActivity {

    final static String TAG = EditarAmigosActivity.class.getName();
    ProgressBar progressBar;
    List<ParseUser> mUsers;
    ArrayList <String> username;
    ArrayAdapter <String> adapter;
    ArrayList <String> objectsIds;
    ParseUser mCurrentUsers;
    ParseRelation<ParseUser> mFriendsRelation;


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

        if(getListView().isItemChecked(position)){
            mFriendsRelation.add(mUsers.get(position));
        }else{
            mFriendsRelation.remove(mUsers.get(position));
        }

        mFriendsRelation.add(mUsers.get(position));

        mCurrentUsers.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    // Ha funcionado
                }
                else{

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);



        username = new ArrayList<String>();
        objectsIds = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,username);
        setListAdapter(adapter);

        //recuperar todos los usuarios de la aplicacion PARSE

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.USERNAME);
        query.setLimit(ParseConstant.MAX_USERS);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List <ParseUser> users, ParseException e) {
                if (e == null) {
                    mUsers = users;
                    for(ParseUser user:mUsers){
                        objectsIds.add(user.getObjectId());
                        adapter.add(user.getUsername());
                    }
                    addFriendsCheckmarks();
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Log.e(TAG, "mierda!!!", e);
                }
            }
        });
    }

    private void addFriendsCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e==null){
                    for(ParseUser user:parseUsers){
                        if(objectsIds.contains(user.getObjectId())){
                            getListView().setItemChecked(objectsIds.indexOf(user.getObjectId()),true);
                        }
                    }
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
