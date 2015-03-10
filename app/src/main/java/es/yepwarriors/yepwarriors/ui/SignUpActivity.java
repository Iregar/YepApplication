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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_activiy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //me creo el metodo para logarme
    public void logarse(View v) {
//recojo en unas variables Strin las variables creadas arriba
        String sUsername = userNameFiled.getText().toString().trim();
        String sPassword = passwordFiled.getText().toString().trim();
        String sEmailAdress = emailAddressField.getText().toString().trim();
        if (checkFieldsEmpty(sUsername, sPassword, sEmailAdress)) {
            AlertDialog.Builder builder
                    = new AlertDialog.Builder(SignUpActivity.this);
//muestra el mensaje del dailogo
            AlertDialog dialog =
                    createErrorDialog(getString(
                            R.string.empty_field_message));
            dialog.show();
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

    //me creo metodo para mostrar mensaje de error (me he creido String con el mensaje)
    private AlertDialog createErrorDialog(String message) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage(message);
        builder.setTitle(getString(R.string.dialog_error_title));
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        return builder.create();
    }

    public void cancel(View v) {
        finish();
    }
}