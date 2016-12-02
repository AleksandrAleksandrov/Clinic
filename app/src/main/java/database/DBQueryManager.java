package database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import adapters.RemindObject;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class DBQueryManager {
    private SQLiteDatabase database;

    public DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public RemindObject getTask(long timeStamp) {
        RemindObject modelTask = null;
        Cursor cursor = database.query(DBHelper.REMIND_TABLE, null, null, new String[]{Long.toString(timeStamp)}, null, null, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.SPECIALIZATION_COLUMN));
//            long date = cursor.getLong(cursor.getColumnIndex(DBHelper.TASK_DATE_COLUMN));
//            int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_PRIORITY_COLUMN));
//            int status = cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_STATUS_COLUMN));

            //modelTask = new RemindObject(title, timeStamp);
        }

        cursor.close();

        return modelTask;
    }

    public List<RemindObject> getTasks(String selection, String[] selectionArgs, String orderBy) {
        List<RemindObject> tasks = new ArrayList<>();

        Cursor c = database.query(DBHelper.REMIND_TABLE, null, selection, selectionArgs, null, null, orderBy);

        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex(DBHelper.SPECIALIZATION_COLUMN));
//                long date = c.getLong(c.getColumnIndex(DBHelper.TASK_DATE_COLUMN));
//                int priority = c.getInt(c.getColumnIndex(DBHelper.TASK_PRIORITY_COLUMN));
//                int status = c.getInt(c.getColumnIndex(DBHelper.TASK_STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(DBHelper.DATE_COLUMN));

                //RemindObject modelTask = new RemindObject(title, timeStamp);
                //tasks.add(modelTask);
            } while (c.moveToNext());
        }
        c.close();

        return tasks;
    }
}