package alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.aleksandr.aleksandrov.clinic.Resources;

import adapters.RemindObject;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class AlarmHelper {
    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;

    public static AlarmHelper getInstance() {
        if (instance == null) {
            instance = new AlarmHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(RemindObject modelTask) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", modelTask.getDoctor());
        intent.putExtra(Resources.TAG_TIME_STAMP, modelTask.getTime());
        //intent.putExtra("color", modelTask.getPriorityColor());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) modelTask.getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, modelTask.getTime(), pendingIntent);
    }
    public void setAlarm(String title, long timeStamp) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra(Resources.TAG_TIME_STAMP, timeStamp);
        //intent.putExtra("color", modelTask.getPriorityColor());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) timeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeStamp, pendingIntent);
    }

    public void removeAlarm(long taskTimeStamp) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) taskTimeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
