package it.petroledge.spotthatcar.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alessandro on 28/03/16.
 */
public class FileUtil {


    public static Bitmap getBitmap(String path, Context context) throws FileNotFoundException {

        FileUtil.copyAssets(context);
        path = path.substring(6, path.length());
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + path);

        if(f.exists()){

            return BitmapFactory.decodeFile(f.getAbsolutePath());
        }
        else {
            throw new FileNotFoundException(String.format("No file found at path: '{0}'", path));
        }
    }

    private static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile;

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + filename);
                    if(!outFile.exists())
                        outFile.createNewFile();
                    out = new FileOutputStream(outFile);
                }


                FileUtil.copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    /** Create a file Uri for saving an image */
    public static Uri getOutputImageFileUri() {
        return Uri.fromFile(getOutputImageFile());
    }

    /** Create a File for saving an image */
    @Nullable
    public static File getOutputImageFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PetrolEdge");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()){
                Log.d("PetrolEdge", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(String.format("%s%sIMG_%s.jpg", mediaStorageDir.getPath(), File.separator, timeStamp));
    }
}
