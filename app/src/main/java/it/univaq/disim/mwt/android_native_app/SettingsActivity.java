package it.univaq.disim.mwt.android_native_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import it.univaq.disim.mwt.android_native_app.dialogs.StoragePermissionDeniedDialogFragment;
import it.univaq.disim.mwt.android_native_app.utils.FileHandler;
import it.univaq.disim.mwt.android_native_app.utils.StoragePermission;

public class SettingsActivity extends AppCompatActivity {

    private MaterialButton exportButton;
    private MaterialButton importButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StoragePermission.isStoragePermissionGranted(SettingsActivity.this)){
                    FileHandler.saveBackup(SettingsActivity.this);
                } else {
                    StoragePermission.requestStoragePermission(SettingsActivity.this);
                }
            }
        });

        importButton = findViewById(R.id.import_button);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: needed permissions ?
                FileHandler.pickBackup(SettingsActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FileHandler.onActivityResultFileHandler(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == StoragePermission.REQUEST_PERMISSION_CODE){
            if(StoragePermission.isStoragePermissionGranted(this)){
                FileHandler.saveBackup(this);
            } else {
                showPermissionNotGrantedDialog();
            }
        }
    }

    private void showPermissionNotGrantedDialog(){
        StoragePermissionDeniedDialogFragment dialog = new StoragePermissionDeniedDialogFragment();
        dialog.show(getSupportFragmentManager(), "storage_denied_dialog");
    }
}
