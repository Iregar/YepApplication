package es.yepwarriors.yepwarriors.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import es.yepwarriors.yepwarriors.R;
import es.yepwarriors.yepwarriors.utils.Utiles;
import es.yepwarriors.yepwarriors.YepApplication;

public class LoginActivity extends ActionBarActivity {

    // Creamos las variables que utilizaremos a lo largo de la lógica
    protected TextView mSignednUpTextview;
    protected EditText mUsername;
    protected EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Al ser la ventana principal no queremos que aparezca la actionBar
        ActionBar actionBar = getSupportActionBar();
        // Creamos una variable que la instancie y la ocultamos con hide()
        actionBar.hide();

        //
        mUsername = (EditText) findViewById(R.id.txtUser);
        mPassword = (EditText) findViewById(R.id.txtPassword);

        // Este campo es una instancia del "botón" que se pulsa en caso de no disponer de
        // usuario (SIGN UP)
        mSignednUpTextview = (TextView) findViewById(R.id.txtNoAccount);

        // Asociamos un escuchador y un eveno (en este caso el onClick)
        mSignednUpTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Indico la actividad que quiero iniciar, en este caso la que me permita
                // crear un usuario (sign up)
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                // Inicio la actividad
                startActivity(intent);
            }
        });

    }

    /*
    Este es el método asociado al onClickListener del botón login
    es el encargado de realizar el logeo del usuario y de abrir el main
    de la aplicación
     */
    public void entrarLogin(View view) {
        // Recogemos de las cajas de texto los datos de username y password
        // El método trim() quita los espacios entre las palabras
        String sUsername = mUsername.getText().toString().trim();
        String spassword = mPassword.getText().toString().trim();

        // Ventana de progreso que se mostrará mientras se realiza el login
        final ProgressDialog pDialog =
                ProgressDialog.show(this,
                        getString(R.string.loging_message),
                        getString(R.string.waiting_message), true);

        ParseUser.logInInBackground(sUsername, spassword,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        // Una vez que el usuario se ha logado oculto ventana progreso
                        pDialog.dismiss();
                        if (e==null) {
                            // Actualizamos la instalacion para el uso de notificaciones push
                            YepApplication.updateParseInstallation(user);
                            // Si nos hemos logado correctamente deberemos pasar a la clase main
                            // o lo que es lo mismo, iniciar la actividad correspondientes (mainActivity.class)
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            // Limpiamos las actividades previas de la pila e indicamos una nueva
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // Iniciamos la actividad
                            startActivity(intent);
                        } else {
                            // Creamos un cuadro de diálogo en el caso de que haya un error en el login (datos)
                            Utiles.createErrorDialog(getString(R.string.ponDatos), getString(R.string.alert_login), LoginActivity.this);
                        }
                    }
                });


    }


}
