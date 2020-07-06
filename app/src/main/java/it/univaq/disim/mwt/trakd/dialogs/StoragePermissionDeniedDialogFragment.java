package it.univaq.disim.mwt.trakd.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import it.univaq.disim.mwt.trakd.R;
import it.univaq.disim.mwt.trakd.utils.StoragePermission;

public class StoragePermissionDeniedDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.storage_permission_denied_dialog_message))
                .setPositiveButton(getString(R.string.storage_permission_denied_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        StoragePermission.requestStoragePermission(getActivity());
                    }
                })
                .setNegativeButton(getString(R.string.storage_permission_denied_dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                })
                .setCancelable(false);
        return builder.create();
    }
}
