package es.yepwarriors.yepwarriors.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import android.support.v7.app.ActionBarActivity;

public class EditFriendsActivity extends ActionBarActivity {
    final static String TAG = EditFriendsActivity.class.getName();
    protected GridView mGridView;
    ProgressBar progressBar;
    List<ParseUser> mUsers;
    UserAdapter adapter;
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(130, 130, 130)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.rgb(85,55,124)));

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
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                // Solo dejaremos que se cargue la lista de amigos en el caso de que no exista error (e==null)
                if (e == null) {
                    List<ParseUser> userList = new ArrayList<ParseUser>();
                    // Eliminamos el usuario qeu está logado para que no aparezca en la lista de amigos
                    for(ParseUser user : users) {
                        // Si el usuario logado es distinto que el usuario de la lista sobre el que iteramos
                        // entonces lo añadiremos a la lista de usuarios a mostrar
                        if (!mCurrentUser.getUsername().equals(user.getUsername())) {
                            userList.add(user);
                        }
                    }
                    mUsers=userList;
                    if (mGridView.getAdapter() == null) {
                        // Inicializamos la instancia del adaptador con la lista de usuarios a mostrar
                        adapter = new UserAdapter(EditFriendsActivity.this, mUsers);
                        mGridView.setAdapter(adapter);
                    } else {
                        ((UserAdapter)mGridView.getAdapter()).refill(mUsers);
                    }
                    // Metodo que se encarga de marcar con check los amigos que ya tenemos añadidos (relaciones)
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