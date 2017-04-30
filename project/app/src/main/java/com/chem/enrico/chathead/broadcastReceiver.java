package com.chem.enrico.chathead;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class broadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent restartService = new Intent(context, listener.class);

        Log.d("killed", "killed");
        context.startService(restartService);
    }
}
