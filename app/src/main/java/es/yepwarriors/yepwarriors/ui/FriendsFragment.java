package es.yepwarriors.yepwarriors.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import es.yepwarriors.yepwarriors.Model.Constantes;
import es.yepwarriors.yepwarriors.Adapters.UserAdapter;
import es.yepwarriors.yepwarriors.R;


/*
Fragmento en el que aparecen nuestros amigos cargados
 */
public class FriendsFragment extends Fragment {
    final static String TAG = FriendsFragment.class.getName();
    List<ParseUser> mUsers;
    ArrayList<String> usernames;
    UserAdapter adapter;
    ProgressBar spinner;
    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;
    protected GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_grid, container, false);

        spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        mGridView =(GridView)rootView.findViewById(R.id.friendsGrid);

        TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();

        mFriendsRelation = mCurrentUser.getRelation(Constantes.Users.FRIENDS_RELATION);

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(Constantes.Users.FIELD_USERNAME);

        spinner.setVisibility(View.VISIBLE);

        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
//sucess
                    spinner.setVisibility(View.INVISIBLE);
                    mUsers = users;

                    String[] usernames = new String[mUsers.size()];
                    int i = 0;
                    for(ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    if (mGridView.getAdapter() == null) {
                        adapter = new UserAdapter(getActivity(), mUsers);
                        mGridView.setAdapter(adapter);
                    } else {
                        ((UserAdapter)mGridView.getAdapter()).refill(mUsers);
                    }
                    mGridView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                    //TODO Mostrar cuadro de dialogo
                }
            }
        });
    }
}