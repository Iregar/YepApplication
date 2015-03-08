package es.yepwarriors.yepwarriors.Controlador.Activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
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

import es.yepwarriors.yepwarriors.Model.Constantes;
import es.yepwarriors.yepwarriors.R;

public class EditFriendsActivity extends ListActivity {
    final static String TAG = EditFriendsActivity.class.getName();
    ProgressBar progressBar;
    List<ParseUser> mUsers;
    ArrayList<String> username;
    ArrayAdapter<String> adapter;
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;
    ArrayList<String> ObjectsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_grid);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //compruebo si esta el usuario este pulsado o no y si esta maracdo lo añado
        //sino lo borro
        if (getListView().isItemChecked(position)) {
            mFriendsRelation.add(mUsers.get(position));
        } else {
            mFriendsRelation.remove(mUsers.get(position));
        }
        //con esto guardo la relacion en la nube
        mCurrentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //genial
                } else {
                    Log.e(TAG, "error al guardar relacion");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
        //recuperar todos los usuarios de la aplicacion PARSE
        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(Constantes.Users.FIELD_USERNAME);
        query.setLimit(Constantes.Users.MAX_USERS);
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(Constantes.Users.FRIENDS_RELATION);
        username = new ArrayList<String>();
        ObjectsIds = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, username);
        setListAdapter(adapter);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    mUsers = users;
                    for (ParseUser user : mUsers) {
                        if (!mCurrentUser.getUsername().equals(user.getUsername())) {
                            adapter.add(user.getUsername());
                        }
                    }
                    addFriendCheckmarks();
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Log.e(TAG, "mierda!!!", e);
                }
            }
        });
    }

    //ver metodo para ver si nuestros alumnos estan marcados
    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    for (ParseUser user : users) {
                        String userID = user.getObjectId();
                        if (ObjectsIds.contains(user.getObjectId())) {
                            getListView().setItemChecked(ObjectsIds.indexOf(userID), true);
                        }
                    }
                }
            }
        });
    }
}