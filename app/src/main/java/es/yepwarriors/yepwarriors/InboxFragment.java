package es.yepwarriors.yepwarriors;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
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

/**
 * Created by ivan on 6/02/15.
 */
public class InboxFragment extends ListFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<ParseObject> mMessages;
    private ArrayList<String> messages;
    private ArrayAdapter<String> adapter;
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity fAct = getActivity();
        final View progressBar = fAct.findViewById(R.id.progressBar);
        messages = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(fAct, android.R.layout.simple_list_item_1, messages);
        setListAdapter(adapter);
        ParseUser pUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constantes.ParseClasses.Messages.CLASS);
        query.whereEqualTo(Constantes.ParseClasses.Messages.KEY_ID_RECIPIENTS, pUser.getObjectId());
        query.addDescendingOrder(Constantes.ParseClasses.Messages.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    mMessages = parseObjects;
                    for (ParseObject message : mMessages) {
                        adapter.add(message.getString(Constantes.ParseClasses.Messages.KEY_SENDER_NAME));
                    }
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
            Intent intent = new Intent(getActivity(), ShowImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        } else {
            //Intent intent = new Intent(Intent.ACTION_VIEW);
        }
    }
}

