package es.yepwarriors.yepwarriors;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by benjagarrido on 13/2/15.
 */
public class FileUtilities {
    final static int MEDIA_TYPE_IMAGE = 1;
    final static int MEDIA_TYPE_VIDEO = 2;

    private final static String APP_NAME = "YEP";
    private final static String TAG = FileUtilities.class.getName();

    public static Uri getOutputMediaFileUri(int mediaType) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = null;
            File appDir = null;
            // 1 Obtener el directorio del almacenamiento interno
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    break;
                case MEDIA_TYPE_VIDEO:
                    mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                    break;
            }
            // 2 Crear el subdirectorio
            appDir = new File(mediaStorageDir, APP_NAME);
            if (!appDir.exists()) {
                Log.d(TAG, "Directory" + appDir.getAbsolutePath() + "not create");
                return null;
            }
            // 3 Crear un nombre del fichero
            String fileName = "";
            Date nowDate = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(nowDate);
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    fileName = "IMG_" + timeStamp + ".jpg";
                    break;
                case MEDIA_TYPE_VIDEO:
                    fileName = "VID_" + timeStamp + ".mp4";
                    break;
            }
            // 4 Crear un objeto File con el nombre del fichero
            String pathFile = appDir.getAbsolutePath()+File.separator+fileName;
            File mediaFile= new File(pathFile);
            Log.d(TAG,"File: "+mediaFile.getAbsolutePath());
            // 5 Devolver el URI del fichero
            return Uri.fromFile(mediaFile);
        }
        return null;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static AlertDialog createErrorDialog(String message, Context cont) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(cont);

        builder.setMessage(message);
        builder.setTitle(cont.getResources().getText(R.string.dialog_error_title));
        builder.setPositiveButton(android.R.string.ok,null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }
}
