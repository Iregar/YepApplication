package es.yepwarriors.yepwarriors.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import es.yepwarriors.yepwarriors.adapters.UserAdapter;
import es.yepwarriors.yepwarriors.constants.Constantes;
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
        mGridView.setOnItemClickListener(mOnItemClickListener);

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

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(Constantes.Users.FRIENDS_RELATION);

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(Constantes.Users.FIELD_USERNAME);
        query.setLimit(Constantes.Users.MAX_USERS);
        //username = new ArrayList<String>();
        //ObjectsIds = new ArrayList<String>();

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
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    for (int i =0;i<mUsers.size();i++){
                        ParseUser user = mUsers.get(i);
                        for (ParseUser friend : friends) {
                            if (friend.getObjectId().equals(user.getObjectId())) {
                                mGridView.setItemChecked(i, true);
                            }
                        }
                    }
                }
                else{
                    Log.e(TAG, "mierda!!!", e);
                }
            }
        });
    }

    protected OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ImageView checkImageView = (ImageView)view.findViewById(R.id.checkImageView);
            //compruebo si esta el usuario este pulsado o no y si esta maracdo lo añado
            //sino lo borro
            if (mGridView.isItemChecked(position)) {
                mFriendsRelation.add(mUsers.get(position));
                checkImageView.setVisibility(View.VISIBLE);
            } else {
                mFriendsRelation.remove(mUsers.get(position));
                checkImageView.setVisibility(View.INVISIBLE);
            }
            //con esto guardo la relacion en la nube
            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "error al guardar relacion");
                    }
                }
            });
        }
    };
}