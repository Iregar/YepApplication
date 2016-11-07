package es.yepwarriors.yepwarriors.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import es.yepwarriors.yepwarriors.adapters.UserAdapter;
import es.yepwarriors.yepwarriors.constants.Constantes;
import es.yepwarriors.yepwarriors.R;
import es.yepwarriors.yepwarriors.utils.FileHelper;

import android.support.v7.app.ActionBarActivity;

public class RecipientsActivity extends ActionBarActivity {

    final static String TAG = RecipientsActivity.class.getName();

    protected ParseUser mCurrentUser;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ProgressBar spinner;
    protected List<ParseUser> mFriends;
    protected MenuItem mSendMenuItem;
    protected Uri mMediaUri;
    protected String mTipoArchivo;
    protected GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(130, 130, 130)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.rgb(85,55,124)));

        setContentView(R.layout.user_grid);
        spinner = (ProgressBar) findViewById(R.id.progressBar);

        mGridView = (GridView)findViewById(R.id.friendsGrid);
        mGridView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mGridView.setOnItemClickListener(mOnItemClickListener);

        mMediaUri = getIntent().getData();
        mTipoArchivo = getIntent().getStringExtra(Constantes.ParseClasses.Messages.KEY_FILE_TYPE);

        TextView emptyTextView = (TextView)findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        // Como solo tenemos un elemento en el menú seleccionamos 0
        mSendMenuItem = menu.getItem(0);
        return true;
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
            case R.id.action_send:
                ParseObject message = createMessage(mMediaUri, mTipoArchivo);
                if (message != null) {
                    send(message);
                    // Finalizamos la actividad para que vuelva a la actividad padre
                    finish();
                } else {
                    //TODO AlertDialog mensaje error
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void send(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(RecipientsActivity.this,"Enviado Yujuuuuu!!!",Toast.LENGTH_SHORT).show();
                    sendPushNotifications();
                }else{
                    // TODO implementar AlertDialog
                }
            }
        });
    }

    private ParseObject createMessage(Uri mMediaUri, String mTipoArchivo) {
        ParseObject message = new ParseObject(Constantes.ParseClasses.Messages.CLASS);
        message.put(Constantes.ParseClasses.Messages.KEY_ID_SENDER, ParseUser.getCurrentUser().getObjectId());
        message.put(Constantes.ParseClasses.Messages.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(Constantes.ParseClasses.Messages.KEY_ID_RECIPIENTS, getRecipientIds());

        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);

        if (fileBytes != null) {
            // Si es una imagen la reducimos
            if (mTipoArchivo.equals(Constantes.FileTypes.IMAGE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
            // Guardamos el nombre del archivo
            String fileName = FileHelper.getFileName(this, mMediaUri, mTipoArchivo);
            ParseFile pFile = new ParseFile(fileName, fileBytes);
            message.put(Constantes.ParseClasses.Messages.KEY_FILE, pFile);
            message.put(Constantes.ParseClasses.Messages.KEY_FILE_TYPE, mTipoArchivo);

            return message;
        } else {
            // TODO implementar AlertDialog
            return null;
        }

    }

    private ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<>();
        for (int i = 0; i < mGridView.getCount(); i++) {
            if (mGridView.isItemChecked(i)) {
                recipientIds.add(mFriends.get(i).getObjectId());
            }
        }
        return recipientIds;
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
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    spinner.setVisibility(View.INVISIBLE);
                    List<ParseUser> friendsList = new ArrayList<ParseUser>();
                    for(ParseUser friend : friends) {
                        // Si el usuario logado es distinto que el usuario de la lista sobre el que iteramos
                        // entonces lo añadiremos a la lista de usuarios a mostrar
                        if (!mCurrentUser.getUsername().equals(friend.getUsername())) {
                            friendsList.add(friend);
                        }
                    }
                    mFriends=friendsList;
                    if (mGridView.getAdapter() == null) {
                        UserAdapter adapter = new UserAdapter(RecipientsActivity.this, mFriends);
                        mGridView.setAdapter(adapter);
                    } else {
                        ((UserAdapter)mGridView.getAdapter()).refill(mFriends);
                    }
                } else {
                    //TODO Mostrar mensaje de dialogo
                    // Utiles.createErrorDialog(getString(R.string.ponDatos), getString(R.string.alert_login), LoginActivity.this);
                    Log.e(TAG, "ParseException caught: ", e);
                }
            }
        });
    }

    protected OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (mGridView.getCheckedItemCount() > 0)
                mSendMenuItem.setVisible(true);
            else
                mSendMenuItem.setVisible(false);

            ImageView checkImageView = (ImageView)view.findViewById(R.id.checkImageView);
            //compruebo si esta el usuario este pulsado o no y si esta maracdo lo añado
            //sino lo borro
            if (mGridView.isItemChecked(position)) {
                // añado el receptor
                checkImageView.setVisibility(View.VISIBLE);
            } else {
                // elimino el receptor
                checkImageView.setVisibility(View.INVISIBLE);
            }
        }
    };

    protected void sendPushNotifications(){
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereContainedIn(Constantes.ParseClasses.Messages.KEY_USER_ID,getRecipientIds());

        // Enviamos la notificacion push.
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(getString(R.string.push_message, ParseUser.getCurrentUser().getUsername()));
        push.sendInBackground();
    }
}
