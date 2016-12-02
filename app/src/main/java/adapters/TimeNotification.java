package adapters;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.aleksandr.aleksandrov.clinic.MainActivity;
import com.aleksandr.aleksandrov.clinic.MyApplication;
import com.aleksandr.aleksandrov.clinic.R;
import com.aleksandr.aleksandrov.clinic.Resources;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class TimeNotification extends BroadcastReceiver {

    NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
//        String title = intent.getStringExtra("title");
//        long timeStamp = intent.getLongExtra("time_stamp", 0);
//        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(R.drawable.logo, title, System.currentTimeMillis());
//        //Интент для активити, которую мы хотим запускать при нажатии на уведомление
//        Intent intentTL = new Intent(context, MainActivity.class);
//        notification.setLatestEventInfo(context, title, "Do something!", PendingIntent.getActivity(context, (int) timeStamp, intentTL, PendingIntent.FLAG_CANCEL_CURRENT));
//        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
//        nm.notify((int) timeStamp, notification);


        // Установим следующее напоминание.



        String title = intent.getStringExtra("title");
        long timeStamp = intent.getLongExtra("time_stamp", 0);
        //int color = intent.getIntExtra("color", 0);

        Intent resultIntent = new Intent(context, MainActivity.class);

        if (MyApplication.isActivityVisible()) {
            resultIntent = intent;
        }

        resultIntent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timeStamp, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(title);
        //builder.setColor(context.getResources().getColor(color));
        builder.setSmallIcon(R.drawable.logo);

        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) timeStamp, notification);


        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
