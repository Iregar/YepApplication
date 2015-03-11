package es.yepwarriors.yepwarriors.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by benjagarrido on 21/2/15.
 * This method create an Error Dialog Message with input parameters
 */
public class Utiles {
    public static void createErrorDialog(String message,String title, Context cont) {
        AlertDialog alertDialog = new AlertDialog.Builder(cont).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
