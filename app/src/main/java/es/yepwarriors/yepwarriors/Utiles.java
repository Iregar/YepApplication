package es.yepwarriors.yepwarriors;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by benjagarrido on 21/2/15.
 */
public class Utiles {
    public static AlertDialog createErrorDialog(String message, Context cont) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(cont);

        builder.setMessage(message);
        builder.setTitle(cont.getResources().getText(R.string.dialog_error_title));
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }
}
