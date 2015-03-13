package es.yepwarriors.yepwarriors.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

import es.yepwarriors.yepwarriors.constants.Constantes;
import es.yepwarriors.yepwarriors.adapters.MessageAdapter;
import es.yepwarriors.yepwarriors.R;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

/**
 * Created by ivan on 6/02/15.
 */
public class InboxFragment extends ListFragment {
    final static String TAG = InboxFragment.class.getName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<ParseObject> mMessages;
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static InboxFragment newInstance(int sectionNumber) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity fAct = getActivity();

        messages = new ArrayList<String>();

        retrieveMessages(fAct);
    }

    private void retrieveMessages(FragmentActivity fAct) {
        final View progressBar = fAct.findViewById(R.id.progressBar);
        ParseUser pUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constantes.ParseClasses.Messages.CLASS);
        query.whereEqualTo(Constantes.ParseClasses.Messages.KEY_ID_RECIPIENTS, pUser.getObjectId());
        query.addDescendingOrder(Constantes.ParseClasses.Messages.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (parseObjects != null) {
                    mMessages = parseObjects;
                    MessageAdapter adapter = new MessageAdapter(
                            getListView().getContext(), mMessages);
                    setListAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    // TODO Message Error
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ParseObject message = mMessages.get(position);
        String fileType = message.getString(Constantes.ParseClasses.Messages.KEY_FILE_TYPE);
        if (fileType.equals(Constantes.FileTypes.IMAGE)) {
            ParseFile file = message.getParseFile(Constantes.ParseClasses.Messages.KEY_FILE);
            Uri fileUri = Uri.parse(file.getUrl());
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        } else {
            //Intent intent = new Intent(Intent.ACTION_VIEW);
        }

        // Una vez que el mensaje es leído
        List <String> ids = message.getList(Constantes.ParseClasses.Messages.KEY_ID_RECIPIENTS);
        if(ids.size()>1){
            // Borrar el receptor de la lista
            ids.remove(ParseUser.getCurrentUser().getObjectId());
            ArrayList<String> idsToRemove = new ArrayList<>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
            message.removeAll(Constantes.ParseClasses.Messages.KEY_ID_RECIPIENTS,idsToRemove);
            message.saveInBackground();
        }else{
            // Ultimo receptor, borar el mensaje de la lista
            message.deleteInBackground();
        }
    }

    protected OnRefreshListener mOnRefreshListener =new OnRefreshListener(){
        @Override
        public void onRefresh() {
            //Toast.makeText(getActivity(),"We´e refreshing!",Toast.LENGTH_SHORT).show();
            retrieveMessages(getActivity());
        }
    };
}

