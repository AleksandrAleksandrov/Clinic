package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class IntentReceiverForLoadSpecializations extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("myLogss", "receiver Loader");
        context.startService(new Intent(context, LoadSpecializationsService.class));
    }
}
