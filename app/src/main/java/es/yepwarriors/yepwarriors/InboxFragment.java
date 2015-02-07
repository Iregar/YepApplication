package es.yepwarriors.yepwarriors;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by ivan on 6/02/15.
 */
public class InboxFragment extends ListFragment{

    ProgressBar progressBar;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       //le cargo un layout al fragmento
        View rootView = inflater.inflate(R.layout.fragment_main_activity_tabbed,container,false);

        //le pongo un proges bar y lo oculto
           progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        return rootView;
    }
}
