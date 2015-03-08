package es.yepwarriors.yepwarriors.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import es.yepwarriors.yepwarriors.Adapters.UserAdapter;
import es.yepwarriors.yepwarriors.Model.Constantes;
import es.yepwarriors.yepwarriors.R;

public class EditFriendsActivity extends Activity {
    final static String TAG = EditFriendsActivity.class.getName();
    protected GridView mGridView;
    ProgressBar progressBar;
    List<ParseUser> mUsers;
    ArrayList<String> username;
    UserAdapter adapter;
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;
    ArrayList<String> ObjectsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_grid);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mGridView=(GridView)findViewById(R.id.friendsGrid);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        TextView emptyTextView = (TextView)findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);
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

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        //compruebo si esta el usuario este pulsado o no y si esta maracdo lo añado
//        //sino lo borro
//        if (getListView().isItemChecked(position)) {
//            mFriendsRelation.add(mUsers.get(position));
//        } else {
//            mFriendsRelation.remove(mUsers.get(position));
//        }
//        //con esto guardo la relacion en la nube
//        mCurrentUser.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    //genial
//                } else {
//                    Log.e(TAG, "error al guardar relacion");
//                }
//            }
//        });
//    }

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

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, username);
        //setListAdapter(adapter);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    mUsers = users;

                    String[] usernames = new String[mUsers.size()];
                    int i = 0;
                    for(ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    if (mGridView.getAdapter() == null) {
                        adapter = new UserAdapter(EditFriendsActivity.this, mUsers);
                        mGridView.setAdapter(adapter);
                    } else {
                        ((UserAdapter)mGridView.getAdapter()).refill(mUsers);
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
                            mGridView.setItemChecked(ObjectsIds.indexOf(userID), true);
                        }
                    }
                }
            }
        });
    }
}