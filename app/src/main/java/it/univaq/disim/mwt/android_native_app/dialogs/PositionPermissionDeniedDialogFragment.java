package it.univaq.disim.mwt.android_native_app.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import it.univaq.disim.mwt.android_native_app.utils.LocationPermission;

public class PositionPermissionDeniedDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("If you reject the permission you cannot use this service, please turn on the permission")
                .setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocationPermission.requestLocationPermission(getActivity());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
