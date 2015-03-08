package es.yepwarriors.yepwarriors.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;

import es.yepwarriors.yepwarriors.constants.Constantes;
import es.yepwarriors.yepwarriors.R;
import es.yepwarriors.yepwarriors.adapters.SectionsPagerAdapter;
import es.yepwarriors.yepwarriors.utils.FileUtilities;


public class MainActivityTabbed extends ActionBarActivity implements ActionBar.TabListener {


    private static final String TAG = MainActivityTabbed.class.getName();
    private final static int TAKE_PHOTO_REQUEST = 0;
    private final static int TAKE_VIDEO_REQUEST = 1;
    private final static int PICK_PHOTO_REQUEST = 2;
    private final static int PICK_VIDEO_REQUEST = 3;
    private final static int LIMIT_DURATION_VIDEO = 10;
    private final static int QUALITY_VIDEO = 0;
    Uri mMediaUri;
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establecemos el layout asociado a esta actividad
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        //Esto es para personalizar el acitionbar de dentro de la app.

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(130, 130, 130)));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.rgb(85,55,124)));

        if (currentUser == null) {
            // Creamos una Intent para abrir una activity
            Intent intent = new Intent(this, LoginActivity.class);

            // Para crear y luego borrar(siempre van asociadas) FLAG_ACTIVITY_NEW_TASK) y(intent.FLAG_ACTIVITY_CLEAR
            // una bandera para decirle al login que es la última actividad
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // La incializamos con el metodo startActivity
            startActivity(intent);
        }

        ParseUser.logInInBackground("Usuario", "contrasela", new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    //estas logado
                } else {
                    //error has metido mas el login
                }
            }
        });

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setIcon(mSectionsPagerAdapter.getIcon(i))
                            //.setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_tabbed, menu);
        return true;
    }

    @Override

    //uso este metodo para cambiar la opcion setting por un icono para deslogarse
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        //Boton para deslogarse me he creado en el menu un sign_out y en Strings tb
        else if (id == R.id.sign_out) {
            ParseUser.logOut();
            Intent i = new Intent(
                    MainActivityTabbed.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        } else if (id == R.id.action_edit_friends) {
            Intent intent = new Intent(this, EditFriendsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_camera) {
            dialogCameraChoices();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dialogCameraChoices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.camera_choices, mDialogListener());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private DialogInterface.OnClickListener mDialogListener() {

        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        takeVideo();
                        break;
                    case 2:
                        pickPhoto();
                        break;
                    case 3:
                        pickVideo();
                        break;
                }

            }
        };
    }

    public void takePhoto() {
        Log.d(TAG, "Haz una foto");

        // Creamos la instancia del intent que nos dará acceso a la cámara
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mMediaUri = FileUtilities.getOutputMediaFileUri(FileUtilities.MEDIA_TYPE_IMAGE);
        if (mMediaUri == null) {
            Log.d(TAG, "mMediaUri == null");
            // En caso de no existir el directorio mostraríamos un error
            // TODO incluir cuadro de diálogo
        } else {
            Log.d(TAG, "mMediaUri != null");
            // Le indicamos al intent dónde queremos que se guarde la imagen
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            // Si el directorio no es nulo entonces arrancamos la actividad
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
        }
    }

    public void takeVideo() {
        Log.d(TAG, "Haz una video");

        // Creamos la instancia del intent que nos dará acceso a la cámara
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        mMediaUri = FileUtilities.getOutputMediaFileUri(FileUtilities.MEDIA_TYPE_VIDEO);
        if (mMediaUri == null) {
            Log.d(TAG, "mMediaUri == null");
            // En caso de no existir el directorio mostraríamos un error
            // TODO incluir cuadro de diálogo
        } else {
            Log.d(TAG, "mMediaUri != null");
            // Le indicamos al intent dónde queremos que se guarde la imagen
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            // Limitamos el vídeo a 10 segundos
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, LIMIT_DURATION_VIDEO);
            // Establecemos la calidad del vídeo (donde 0 es nula y 10 es la máxima)
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, QUALITY_VIDEO);
            // Si el directorio no es nulo entonces arrancamos la actividad
            startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
        }
    }

    private void pickPhoto() {
        Log.d(MainActivityTabbed.TAG, "Elige una foto");

        Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        choosePhotoIntent.setType("image/*");
        startActivityForResult(choosePhotoIntent, MainActivityTabbed.PICK_PHOTO_REQUEST);
    }

    private void pickVideo() {
        Log.d(TAG, "Elige un video");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Escoge un vídeo de menos de 10Mb");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                chooseVideoIntent.setType("video/*");
                startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);
            }
        });
        builder.setTitle("Cuidado");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Solo enviamos la actualizacion si hemos hecho una foto/video
            if (requestCode == TAKE_PHOTO_REQUEST || requestCode == TAKE_VIDEO_REQUEST) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                // Avisamos a todos los suscriptores de que hemos actualizado la galeria
                sendBroadcast(mediaScanIntent);
            } else if (data != null) {
                mMediaUri = data.getData();
                if (requestCode == PICK_VIDEO_REQUEST) {
                    int fileSize = 0;
                    int sizeMax = 10 * 1024 * 1024;
                    InputStream inpurStream = null;
                    try {
                        inpurStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inpurStream.available();
                        inpurStream.close();
                    } catch (IOException e) {

                    }
                    if (fileSize > sizeMax) {
                        //TODO mostrar cuadro de diálogo con error
                        return;
                    }
                }
            } else {
                //TODO añadimos un mensaje de error
            }
            Intent recipientsIntent = new Intent(this,RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);
            String tipoFichero;
            if(requestCode==PICK_PHOTO_REQUEST||requestCode==TAKE_PHOTO_REQUEST)
                tipoFichero= Constantes.FileTypes.IMAGE;
            else
                tipoFichero= Constantes.FileTypes.VIDEO;
            recipientsIntent.putExtra(Constantes.ParseClasses.Messages.KEY_FILE_TYPE,tipoFichero);
            startActivity(recipientsIntent);

        } else if (resultCode != RESULT_CANCELED) {

        }
    }
}
