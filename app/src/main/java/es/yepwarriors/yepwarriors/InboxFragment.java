package es.yepwarriors.yepwarriors;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.Parse;
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
    private ProgressBar progressBar;
    private List<ParseObject> mMensajes;
    private ArrayList<String> mensajes;
    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //le cargo un layout al fragmento
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //le pongo un proges bar y lo oculto
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();

        mensajes=new ArrayList<>();
        adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_expandable_list_item_1,mensajes);
        setListAdapter(adapter);

        ParseQuery<ParseObject> query=ParseQuery.getQuery(ParseConstant.CLASS_MESSAGE);
        query.whereEqualTo(ParseConstant.KEY_ID_RECIPIENT, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstant.KEY_CREATE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null){
                    mMensajes=parseObjects;
                    for (ParseObject mensaje:mMensajes){
                        adapter.add(mensaje.getString(ParseConstant.KEY_NAME_SENDER));
                    }
                }
                else {
                    // TODO AlertDialog
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject mensaje=mMensajes.get(position);
        String typeFile = mensaje.getString(ParseConstant.KEY_ID_TYPE_FILE);
        if(typeFile.equals(ParseConstant.IMAGE_TYPE)){
            ParseFile archivo = mensaje.getParseFile(ParseConstant.KEY_ID_FILE);
            Uri uriFile = Uri.parse(archivo.getUrl());
        }else{
            // TODO incluir la parte de vídeo
        }
    }
}

