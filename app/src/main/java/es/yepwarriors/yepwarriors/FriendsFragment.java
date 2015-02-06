package es.yepwarriors.yepwarriors;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 6/02/15.
 */
public class FriendsFragment extends ListFragment {

    ProgressBar progressBar;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //le cargo un layout al fragmento Fragmente Friends
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        //le pongo un proges bar y lo oculto
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        return rootView;
    }

    ArrayList<String> username;
    ArrayAdapter<String> adapter;
    List<ParseUser> mUsers;
    final static String TAG = EditarAmigosActivity.class.getName();

    //cargar los amigos para que aparezca en el fragmento de amigos

    @Override
    public void onResume() {
        super.onResume();

        username = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, username);

        setListAdapter(adapter);


        //recuperar todos los usuarios de la aplicacion PARSE

        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.USERNAME);
        query.setLimit(ParseConstant.MAX_USERS);


        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    mUsers = users;
                    for (ParseUser user : mUsers) {

                        adapter.add(user.getUsername());
                    }

                    //addFriendCheckmarks();

                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Log.e(TAG, "mierda!!!", e);
                }
            }
        });
    }


}
