package com.oubeichen.resourcemonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Bootreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
		Intent s = new Intent(context, Monitorservice.class);
		context.startService(s);
    }

}
