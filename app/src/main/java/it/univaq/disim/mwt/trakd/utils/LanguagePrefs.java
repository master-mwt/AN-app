package it.univaq.disim.mwt.trakd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LanguagePrefs {

    public static void setLanguage(@NonNull Context context, @NonNull String lang){
        SharedPreferences pref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(changeLang(context, lang)){
            editor.putString("language", lang);
            editor.apply();
        }
    }

    public static String getLanguage(@NonNull Context context){
        SharedPreferences pref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String languageIt = new Locale("it").getLanguage();
        String languageEn = new Locale("en").getLanguage();
        String currentLang = getCurrentLanguage(context);

        return pref.getString("language", (!languageEn.equals(currentLang) && !languageIt.equals(currentLang)) ? languageEn : currentLang);
    }

    private static String getCurrentLanguage(@NonNull Context context){
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    private static boolean changeLang(@NonNull Context context, @NonNull String lang){
        Configuration configuration = context.getResources().getConfiguration();
        String language = new Locale(lang).getLanguage();
        if(!"".equals(language)){
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            configuration.locale = locale;
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

            return true;
        }
        return false;
    }
}
