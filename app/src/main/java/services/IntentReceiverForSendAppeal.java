package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class IntentReceiverForSendAppeal extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("myLogss", "receiver Sender");
        context.startService(new Intent(context, SendAppealService.class));
    }
}
