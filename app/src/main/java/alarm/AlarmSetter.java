package alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aleksandr.aleksandrov.clinic.Resources;

import database.DBHelper;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);
        //Calendar calendar = Calendar.getInstance();
        AlarmHelper.getInstance().init(context);
        AlarmHelper alarmHelper = AlarmHelper.getInstance();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(dbHelper.REMIND_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int sp = c.getColumnIndex(dbHelper.SPECIALIZATION_COLUMN);
            int dt = c.getColumnIndex(dbHelper.DATE_COLUMN);

            do {

                if (c.getLong(dt) + Resources.TIME < System.currentTimeMillis()) {
                    db.delete(dbHelper.REMIND_TABLE, dbHelper.DATE_COLUMN + " = " + c.getLong(dt), null);
                } else {
                    alarmHelper.setAlarm(c.getString(sp), c.getLong(dt) - Resources.TIME);
                }
            } while (c.moveToNext());
            c.close();


        }


//        List<RemindObject> tasks = new ArrayList<>();
//        tasks.addAll(dbHelper.query().getTasks(DBHelper.SPECIALIZATION_COLUMN,
//                new String[] {Integer.toString(ModelTask.STATUS_CURRENT), Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));
//
//        for (RemindObject task : tasks) {
//            if (task.getDate() != 0) {
//                alarmHelper.setAlarm(task);
//            }
//        }
    }





}