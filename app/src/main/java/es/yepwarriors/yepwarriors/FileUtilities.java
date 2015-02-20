package es.yepwarriors.yepwarriors;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
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
            Log.d(TAG, "Nudo if " + isExternalStorageAvailable());
            File mediaStorageDir = null;
            // 1 Obtener el directorio del almacenamiento interno
            Log.d(TAG, "Valor mediaType " + mediaType);
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    Log.d(TAG, "Valor mediaStorageDir image " + mediaStorageDir);
                    break;
                case MEDIA_TYPE_VIDEO:
                    mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                    Log.d(TAG, "Valor mediaStorageDir video " + mediaStorageDir);
                    break;
            }
            // 2 Crear el subdirectorio
            if (mediaStorageDir != null) {
                File appDir=null;
                appDir= new File(mediaStorageDir, APP_NAME);
                Log.d(TAG,"appDir: "+ appDir);
                if (!mediaStorageDir.exists()) {
                    Log.d(TAG, appDir.getAbsolutePath() + " not exists");
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d(TAG, "Directory " + appDir.getAbsolutePath() + " not created");
                        return null;
                    }
                }
                String fileName = "";
                Date now = new Date();
                String timestamp = new SimpleDateFormat(
                        "yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);
                switch (mediaType) {
                    case MEDIA_TYPE_IMAGE:
                        fileName = "IMG_" + timestamp + ".jpg";
                        break;
                    case MEDIA_TYPE_VIDEO:
                        fileName = "VID_" + timestamp + ".mp4";
                        break;
                }
                String pathFile = appDir.getAbsolutePath() +File.separator +fileName;
                Log.d(TAG, "pathFile : " + pathFile);
                File mediaFile = null;
                mediaFile = new File(pathFile);
                Log.d(TAG, "mediaFile.getAbsolutePath : " + mediaFile.getAbsolutePath());

                if (mediaFile != null) {
                    Log.d(TAG, "File : " + mediaFile.getAbsolutePath());
                    return Uri.fromFile(mediaFile);
                }
            }
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
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }
}
