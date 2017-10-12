package com.example.united.mrk;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


public class CreateFileJson {
    public static void saveData(Context context, String mJsonResponse, String fileName) {
        try {
            FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + fileName+".txt");
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
           // Log.e("TAG", "Writing file Error :" + e.getMessage());
        }
       /* // Get the directory for the user's public pictures directory.
        final File path = Environment.getExternalStoragePublicDirectory(//Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DCIM + "/unitedtronik/");
        // Make sure the path directory exists.
        // Make it, if it doesn't exit
        path.mkdirs();
        final File file = new File(path, fileName+".txt");
        // Save your stream, don't forget to flush() it before closing it.
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(mJsonResponse);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }*/
    }

    public static String getData(Context context, String fileName) {
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName+".txt");
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
           // Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }

        /*final File path = Environment.getExternalStoragePublicDirectory(//Environment.DIRECTORY_PICTURES
                Environment.DIRECTORY_DCIM + "/unitedtronik/");
        try {
            File f = new File(path + "/" + fileName+".txt");
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }*/
    }
}
