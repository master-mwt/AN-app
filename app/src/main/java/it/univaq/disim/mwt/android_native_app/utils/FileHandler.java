package it.univaq.disim.mwt.android_native_app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import it.univaq.disim.mwt.android_native_app.services.UserCollectionService;

public class FileHandler {

    public static final int REQUEST_PICK_BACKUP_CODE = 102;
    public static final int REQUEST_SAVE_BACKUP_CODE = 103;

    public static void pickBackup(Activity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        // activity is not kept in the list of recently launched activities
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivityForResult(intent, REQUEST_PICK_BACKUP_CODE);
    }

    public static void saveBackup(Activity activity){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "backup.json");
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivityForResult(intent, REQUEST_SAVE_BACKUP_CODE);
    }

    public static void onActivityResultFileHandler(Context context, int requestCode, int resultCode, Intent data){
        Intent intent = null;

        switch (requestCode){
            case REQUEST_PICK_BACKUP_CODE:
                if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null && data.getData().getPath() != null){

                    intent = new Intent(context, UserCollectionService.class);
                    intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_DB_IMPORT);
                    intent.putExtra(UserCollectionService.KEY_DATA, data.getData());
                    context.startService(intent);
                    Toast.makeText(context, "importing backup", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_SAVE_BACKUP_CODE:
                if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null && data.getData().getPath() != null){

                    intent = new Intent(context, UserCollectionService.class);
                    intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_DB_EXPORT);
                    intent.putExtra(UserCollectionService.KEY_DATA, data.getData());
                    context.startService(intent);
                    Toast.makeText(context, "exporting backup", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    
    public static void write(Context context, Uri uri, String text) {

        OutputStream outputStream = null;
        BufferedWriter bufferedWriter = null;

        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch(IOException e){
            Log.d(FileHandler.class.getName(), (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        } finally {
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    Log.d(FileHandler.class.getName(), (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
                }
            }
        }
    }

    public static String read(Context context, Uri uri) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch(IOException e){
            Log.d(FileHandler.class.getName(), (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        } finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.d(FileHandler.class.getName(), (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
                }
            }
        }
        return stringBuilder.toString();
    }
}
