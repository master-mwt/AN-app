package it.univaq.disim.mwt.android_native_app.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import it.univaq.disim.mwt.android_native_app.R;
import it.univaq.disim.mwt.android_native_app.utils.StoragePermission;

public class StoragePermissionDeniedDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.storage_permission_denied_dialog_message))
                .setPositiveButton(getString(R.string.storage_permission_denied_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StoragePermission.requestStoragePermission(getActivity());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.storage_permission_denied_dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: It is all right to finish ?
                        getActivity().finish();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        return builder.create();
    }
}
