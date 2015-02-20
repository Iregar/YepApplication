package es.yepwarriors.yepwarriors;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class ActivityRecipients extends ListActivity {

    final static String TAG = ActivityRecipients.class.getName();

    ParseUser mCurrentUser;
    ParseRelation<ParseUser> mFriendsRelation;
    ProgressBar spinner;
    ArrayList<String> usernames;
    ArrayAdapter<String> adapter;
    List<ParseUser> mFriends;
    MenuItem mSendMenuItem;
    private Uri mMediaUri;
    private String mTipoArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_recipients);

        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        Intent intent = getIntent();
        mMediaUri = intent.getData();
        mTipoArchivo = intent.getStringExtra(ParseConstant.KEY_ID_TYPE_FILE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_recipients, menu);
        // Como solo tenemos un elemento en el menú seleccionamos 0
        mSendMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            ParseObject message = createMessage();
            if (message != null) {
                sendMessage(message);
            } else {
                //TODO AlertDialog mensaje error
            }
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendMessage(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(ActivityRecipients.this,"Enviado Yujuuuuu!!!",Toast.LENGTH_SHORT).show();
                }else{
                    // TODO implementar AlertDialog
                }
            }
        });
    }

    private ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstant.CLASS_MESSAGE);
        // Nombre del archivo
        // Tipo de archivo (imagen o video)
        // Id del recipiente o recipientes
        // Nombre del receptor
        message.put(ParseConstant.KEY_ID_SENDER, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstant.KEY_NAME_SENDER, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstant.KEY_ID_RECIPIENT, getRecipientsIds());
        byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
        if (fileBytes != null) {
            // Si es una imagen la reducimos
            if (mTipoArchivo.equals(ParseConstant.IMAGE_TYPE)) {
                fileBytes = FileHelper.reduceImageForUpload(fileBytes);
            }
            // Guardamos el nombre del archivo
            String fileName = FileHelper.getFileName(this, mMediaUri, mTipoArchivo);
            ParseFile fileParse = new ParseFile(fileName, fileBytes);
            message.put(ParseConstant.KEY_ID_FILE, fileParse);
        } else {
            // TODO implementar AlertDialog
        }
        return message;
    }

    private ArrayList<String> getRecipientsIds() {
        ArrayList<String> recipients = new ArrayList<>();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (getListView().isItemChecked(i)) {
                recipients.add(mFriends.get(i).getObjectId());
            }
        }
        return recipients;
    }

    @Override
    public void onResume() {
        super.onResume();
        setListView();
        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstant.FRIENDS_RELATION);
        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending(ParseConstant.USERNAME);
        spinner.setVisibility(View.VISIBLE);
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    spinner.setVisibility(View.INVISIBLE);
                    mFriends = users;
                    for (ParseUser user : users) {
                        adapter.add(user.getUsername());
                    }
                } else {
                    Log.e(TAG, "ParseException caught: ", e);
                }
            }
        });
    }

    private void setListView() {
        usernames = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames);
        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (l.getCheckedItemCount() > 0)
            mSendMenuItem.setVisible(true);
        else
            mSendMenuItem.setVisible(false);
    }
}
