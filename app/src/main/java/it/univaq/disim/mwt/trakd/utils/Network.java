package it.univaq.disim.mwt.trakd.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.FragmentManager;

import it.univaq.disim.mwt.trakd.dialogs.NetworkNotAvailableDialogFragment;

public class Network {

    public static boolean isConnectedOrConnecting(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConnectedOrConnecting = false;
        boolean isMobileConnectedOrConnecting = false;

        if(connectivityManager != null){
            for(android.net.Network network : connectivityManager.getAllNetworks()){
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                    isMobileConnectedOrConnecting |= networkInfo.isConnectedOrConnecting();
                }
                if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    isWifiConnectedOrConnecting |= networkInfo.isConnectedOrConnecting();
                }
            }
        }

        return isMobileConnectedOrConnecting || isWifiConnectedOrConnecting;
    }

    public static void checkAvailability(Context context, FragmentManager fragmentManager){
        if(!isConnectedOrConnecting(context)){
            NetworkNotAvailableDialogFragment fragment = new NetworkNotAvailableDialogFragment();
            fragment.setCancelable(false);
            fragment.show(fragmentManager, "network_not_available_dialog");
        }
    }
}
