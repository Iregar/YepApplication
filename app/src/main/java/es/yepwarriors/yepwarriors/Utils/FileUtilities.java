package es.yepwarriors.yepwarriors.utils;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FileUtilities {
   public final static int MEDIA_TYPE_IMAGE = 1;
   public final static int MEDIA_TYPE_VIDEO = 2;

    public final static String APP_NAME = "YEP";
    public final static String TAG = FileUtilities.class.getName();

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
            //Crear subdirectorio
            File appDir = new File(mediaStorageDir, APP_NAME);
            if (!appDir.exists()) {
                if (!appDir.mkdirs()) {
                    Log.d(TAG, "Directory" + appDir.getAbsolutePath() + "notcreated");
                    return null;
                }
                Log.d(TAG, "Directory" + appDir.getAbsolutePath());
            }
            //crear nombre del fichero
            String fileName = "";
            Date now = new Date();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);
            switch (mediaType) {
                case MEDIA_TYPE_IMAGE:
                    fileName = "IMG_" + timestamp + ".jpg";
                    break;
                case MEDIA_TYPE_VIDEO:
                    fileName = "VID_" + timestamp + ".mp4";
                    break;
            }
            //Crear un objeto FIle con el nombre del fichero
            String pathFile = appDir.getAbsolutePath() + File.separator + fileName;
            File mediaFile = new File(pathFile);
            Log.d(TAG, "File:" + mediaFile.getAbsolutePath());
            //Devolver el URI del fichero
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
}
