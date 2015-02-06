package es.yepwarriors.yepwarriors;

import android.app.AlertDialog;
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


public class LoginActivity extends ActionBarActivity {

    //me creo una variable para recoger el Textview
    protected TextView mSignednUpTextview;

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Para esconder el actionBar en el que nos aparece el nombre de la aplicacion
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        username = (EditText) findViewById(R.id.txtUser);
        password = (EditText) findViewById(R.id.txtPassword);

        //en esta variable lo asocio al boton de login

        mSignednUpTextview = (TextView) findViewById(R.id.txtNoAccount);

        //me creo un escuchador

        mSignednUpTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //me creo un intent para abrir la activity SignupActivity

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

      // OnClick Listener botón login
    public void entrarLogin(View view) {
        //con el trim quita los espacios entre las palabras
        String sUsername = username.getText().toString().trim();
        String spassword = password.getText().toString().trim();
       /* if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }*/

        // ventana de progreso
        final ProgressDialog dialog =
                ProgressDialog.show(this,
                        getString(R.string.loging_message),
                        getString(R.string.waiting_message), true);

        ParseUser.logInInBackground(sUsername, spassword,
                new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivityTabbed.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    dialog.dismiss(); // oculto ventana progreso
                } else {
                    AlertDialog dialog =
                            createErrorDialog(getString(R.string.ponDatos));
                }
            }
        });


    }


    private AlertDialog createErrorDialog(String message) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(getString(R.string.dialog_error_title));
        builder.setPositiveButton(android.R.string.ok,null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }


}
