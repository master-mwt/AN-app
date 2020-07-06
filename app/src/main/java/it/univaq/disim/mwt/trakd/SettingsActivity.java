package it.univaq.disim.mwt.trakd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import it.univaq.disim.mwt.trakd.dialogs.LanguageSelectionDialogFragment;
import it.univaq.disim.mwt.trakd.dialogs.StoragePermissionDeniedDialogFragment;
import it.univaq.disim.mwt.trakd.services.UserCollectionService;
import it.univaq.disim.mwt.trakd.utils.FileHandler;
import it.univaq.disim.mwt.trakd.utils.StoragePermission;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialButton exportDBButton;
    private MaterialButton importDBButton;
    private MaterialButton exportDBToFirestoreButton;
    private MaterialButton importDBFromFirestoreButton;
    private MaterialButton changeLanguageButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        exportDBButton = findViewById(R.id.export_button);
        exportDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StoragePermission.isStoragePermissionGranted(SettingsActivity.this)){
                    FileHandler.saveBackup(SettingsActivity.this);
                } else {
                    StoragePermission.requestStoragePermission(SettingsActivity.this);
                }
            }
        });

        importDBButton = findViewById(R.id.import_button);
        importDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileHandler.pickBackup(SettingsActivity.this);
            }
        });

        exportDBToFirestoreButton = findViewById(R.id.export_firestore_button);
        exportDBToFirestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null){
                    Intent intent = new Intent(getApplicationContext(), UserCollectionService.class);
                    intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_DB_FIRESTORE_EXPORT);
                    intent.putExtra(UserCollectionService.KEY_DATA, mAuth.getCurrentUser().getEmail());
                    startService(intent);
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_content_backup_firestore_export), Toast.LENGTH_SHORT).show();
                }
            }
        });

        importDBFromFirestoreButton = findViewById(R.id.import_firestore_button);
        importDBFromFirestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null){
                    Intent intent = new Intent(getApplicationContext(), UserCollectionService.class);
                    intent.putExtra(UserCollectionService.KEY_ACTION, UserCollectionService.ACTION_DB_FIRESTORE_IMPORT);
                    intent.putExtra(UserCollectionService.KEY_DATA, mAuth.getCurrentUser().getEmail());
                    startService(intent);
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_content_backup_firestore_import), Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeLanguageButton = findViewById(R.id.change_language_button);
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageSelectionDialogFragment dialog = new LanguageSelectionDialogFragment();
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "language_change_dialog");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAuth.getCurrentUser() == null){
            exportDBToFirestoreButton.setEnabled(false);
            importDBFromFirestoreButton.setEnabled(false);
        } else {
            exportDBToFirestoreButton.setEnabled(true);
            importDBFromFirestoreButton.setEnabled(true);
        }
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
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "storage_denied_dialog");
    }
}
