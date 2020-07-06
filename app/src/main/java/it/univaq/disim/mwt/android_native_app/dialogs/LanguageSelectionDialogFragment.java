package it.univaq.disim.mwt.android_native_app.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

import it.univaq.disim.mwt.android_native_app.ExploreActivity;
import it.univaq.disim.mwt.android_native_app.R;
import it.univaq.disim.mwt.android_native_app.utils.LanguagePrefs;

public class LanguageSelectionDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String[] items = {getString(R.string.lang_english), getString(R.string.lang_italian)};
        final String languageIt = new Locale("it").getLanguage();
        final String languageEn = new Locale("en").getLanguage();
        String currentLanguage = LanguagePrefs.getLanguage(getContext());
        int checkedItem = (languageIt.equals(currentLanguage) ? 1 : 0);
        final String[] newLanguage = {""};

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext())
                .setTitle(getString(R.string.language_selection_dialog_title))
                .setPositiveButton(getString(R.string.language_selection_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!"".equals(newLanguage[0])){
                            LanguagePrefs.setLanguage(getContext(), newLanguage[0]);
                        }
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), ExploreActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.language_selection_dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newLanguage[0] = (which == 1) ? languageIt : languageEn;
                    }
                })
                .setCancelable(false);

        return builder.create();
    }
}
