package es.yepwarriors.yepwarriors.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import es.yepwarriors.yepwarriors.R;
import es.yepwarriors.yepwarriors.YepApplication;
import es.yepwarriors.yepwarriors.utils.Utiles;

public class SignUpActivity extends ActionBarActivity {
    private EditText userNameFiled;
    private EditText passwordFiled;
    private EditText emailAddressField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//Para esconder el actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        userNameFiled = (EditText) findViewById(R.id.txtName);
        passwordFiled = (EditText) findViewById(R.id.txtPassword);
        emailAddressField = (EditText) findViewById(R.id.txtEmail);
    }

    //me creo el metodo para logarme
    public void logarse(View v) {
//recojo en unas variables Strin las variables creadas arriba
        String sUsername = userNameFiled.getText().toString().trim();
        String sPassword = passwordFiled.getText().toString().trim();
        String sEmailAdress = emailAddressField.getText().toString().trim();
        if (checkFieldsEmpty(sUsername, sPassword, sEmailAdress)) {
            Utiles.createErrorDialog(getString(R.string.empty_field_message), getString(R.string.alert_signup), SignUpActivity.this);
        } else {
            ParseUser newUser = new ParseUser();
            newUser.setUsername(sUsername);
            newUser.setPassword(sPassword);
            newUser.setEmail(sEmailAdress);
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        //abirmos la ventana principal
                        // YepApplication.updateParseInstallation(ParseUser.getCurrentUser());
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        //para crear y luego borrar(siempre van asociadas) FLAG_ACTIVITY_NEW_TASK) y(intent.FLAG_ACTIVITY_CLEAR
                        // una bandera para decirle al login que es la última actividad
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        //mensaje de error
                        Toast.makeText(SignUpActivity.this, "Upsss", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //metodo para validar si estan escritos los campos de Login
    private boolean checkFieldsEmpty(String sUsername, String sPassword, String sEmailAdress) {
        return sUsername.isEmpty() |
                sPassword.isEmpty() |
                sEmailAdress.isEmpty();
    }

    public void cancel(View v) {
        finish();
    }
}