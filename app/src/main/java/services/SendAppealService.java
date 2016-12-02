package services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.aleksandr.aleksandrov.clinic.AppealActivity;
import com.aleksandr.aleksandrov.clinic.MainActivity;
import com.aleksandr.aleksandrov.clinic.R;
import com.aleksandr.aleksandrov.clinic.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

import alarm.AlarmHelper;
import alarm.AlarmReceiver;
import database.DBHelper;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class SendAppealService extends Service {

    public static boolean serviceState;
    JSONObject jsonObj = null;

    int startId;
    int exceptionCounter = 0;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;

    private String param = null, answer = null, error = null;


    DBHelper dbHelper;
    ContentValues cv;
    ContentValues cv2;
    SQLiteDatabase db;
    AlarmHelper alarmHelper;

    String reqTag = null, reqSpecName = null, reqDate = null, reqTime = null, reqName = null, reqEmail = null, reqPhone = null, reqDescription = null;
    int _ID;
    long timeStamp;



    public void onCreate() {
        super.onCreate();
        serviceState = true;
        dbHelper = new DBHelper(this);
    }

    public void onDestroy() {
        super.onDestroy();
        serviceState = false;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);

        // I don't know what does 1 means.
        _ID = intent.getIntExtra(Resources._ID, 1);
        reqTag = intent.getStringExtra(Resources.TAG_ID_SPEC);
        reqSpecName = intent.getStringExtra(Resources.TAG_APPSPECIALIZATION_NAME);
        reqDate = intent.getStringExtra(Resources.TAG_APP_DATE);
        reqTime = intent.getStringExtra(Resources.TAG_APP_TIME);
        reqName = intent.getStringExtra(Resources.TAG_APP_ONAME);
        reqEmail = intent.getStringExtra(Resources.TAG_APP_OEMAIL);
        reqPhone = intent.getStringExtra(Resources.TAG_APP_OPHONE);
        reqDescription = intent.getStringExtra(Resources.TAG_APP_DESCRIPTION);
        timeStamp = intent.getLongExtra(Resources.TAG_TIME_STAMP, 0);

        ed = sPref.edit();
        MyRun mr = new MyRun();
        new Thread(mr).start();
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyRun implements Runnable {



        @Override
        public void run() {

            AlarmHelper.getInstance().init(getApplication());
            alarmHelper = AlarmHelper.getInstance();

            cv2 = new ContentValues();
            db = dbHelper.getWritableDatabase();

            if (reqTag == null) {
                param = sPref.getString("REQUEST", "");
            } else {
                if (sPref.getString("REQUEST", "").equals("")) {
                    stop();
                }

            }

            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!sendLetter());
            ed.putString("REQUEST", "");
            ed.commit();

            stop();
        }
        void stop() {

            stopSelf(startId);
        }
    }

    private boolean sendLetter() {

        Cursor cc = db.query(dbHelper.SEND_APPEAL_AGAIN, null, null, null, null, null, null);
        int counter = 0;
        if (cc.moveToFirst()) {
            do {
                try {
                    URL url = new URL("http:" + Resources.SEND_APPEAL + Resources.TAG_ID_SPEC + "=" + cc.getString(cc.getColumnIndex(Resources.TAG_ID_SPEC)) + "&" +
                            Resources.TAG_APP_DATE + "=" + cc.getString(cc.getColumnIndex(Resources.TAG_APP_DATE)) + "&" +
                            Resources.TAG_APP_TIME + "=" + cc.getString(cc.getColumnIndex(Resources.TAG_APP_TIME)) + "&" +
                            Resources.TAG_APP_ONAME + "=" + URLEncoder.encode(cc.getString(cc.getColumnIndex(Resources.TAG_APP_ONAME)), "UTF-8") + "&" +
                            Resources.TAG_APP_OEMAIL + "=" + cc.getString(cc.getColumnIndex(Resources.TAG_APP_OEMAIL)) + "&" +
                            Resources.TAG_APP_OPHONE + "=" + cc.getString(cc.getColumnIndex(Resources.TAG_APP_OPHONE)) + "&" +
                            Resources.TAG_APP_DESCRIPTION + "=" + URLEncoder.encode(cc.getString(cc.getColumnIndex(Resources.TAG_APP_DESCRIPTION)), "UTF-8"));

//                    URL url = new URL("http", Resources.SEND_APPEAL + param, null);
                    URLConnection urlConnection = url.openConnection();
//                    urlConnection.setDoOutput(true);
//                    urlConnection.setChunkedStreamingMode(0);
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setRequestProperty("User-Agent", "asdf");
//                    urlConnection.setDoOutput(true);
//                    int responseCode = urlConnection.getResponseCode();
//                    urlConnection.setReadTimeout(15*1000);
//                    urlConnection.connect();
//                    urlConnection.setDoOutput(true);
//                    if (urlConnection.getResponseCode() == 200 ) {

//                        OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

//                    Log.d("ok", responseCode + "");
//                    param = "appspecialization_id=3&date=2016-06-02&time=06:58&oname=Test&oemail=test&ophone=55215554&odescription=Test&personnel_id=16";
//                        wr.write(param);
//                    }


                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
//                    wr.close();
                    in.close();

                    jsonObj = new JSONObject(response.toString());
                    Log.d("get", "response" + jsonObj);

                    int success = jsonObj.getInt(Resources.TAG_SUCCESS);

                    if (success == 1) {
                        NotificationCompat.Builder successNotification = new NotificationCompat.Builder(getApplicationContext());
                        successNotification.setContentTitle(getResources().getString(R.string.app_name));
                        successNotification.setContentText("Вы записаны к " + cc.getString(cc.getColumnIndex(Resources.TAG_APPSPECIALIZATION_NAME)).toLowerCase());
                        //builder.setColor(context.getResources().getColor(color));
                        successNotification.setSmallIcon(R.drawable.logo);

                        successNotification.setDefaults(Notification.DEFAULT_ALL);
                        // builder.setContentIntent(pendingIntent);

                        Notification notification1 = successNotification.build();
                        notification1.flags |= Notification.FLAG_AUTO_CANCEL;

                        NotificationManager notificationManager1 = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager1.notify((int) cc.getLong(cc.getColumnIndex(Resources.TAG_TIME_STAMP)), notification1);




//                    answer = reqDate + " в " +reqTime;
//                    Log.d("myLogs", "success" + reqSpecName);
                        cv = new ContentValues();
                        cv.put(dbHelper.SPECIALIZATION_COLUMN, cc.getString(cc.getColumnIndex(Resources.TAG_APPSPECIALIZATION_NAME)));
                        cv.put(dbHelper.DATE_COLUMN, cc.getLong(cc.getColumnIndex(Resources.TAG_TIME_STAMP)));

                        db.insert(dbHelper.REMIND_TABLE, null, cv);

                        db.delete(dbHelper.SEND_APPEAL_AGAIN, Resources._ID + "= " + cc.getInt(cc.getColumnIndex(Resources._ID)), null);
                        if (AppealActivity.checked) {
                            alarmHelper.setAlarm(reqSpecName, timeStamp - Resources.TIME);
                        }
                        sendBroadcast(new Intent(MainActivity.BROADCAST_ACTION).putExtra("done", 1));
                    }
                    in.close();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    Log.d("fail", "first");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("fail", "second");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("fail", "third");
                    if (exceptionCounter <= 0) {
                        NotificationCompat.Builder successNotification = new NotificationCompat.Builder(getApplicationContext());
                        successNotification.setContentTitle(getResources().getString(R.string.app_name));
                        successNotification.setContentText("Не удалось подключиться к серверу");
                        //builder.setColor(context.getResources().getColor(color));
                        successNotification.setSmallIcon(R.drawable.logo);

                        successNotification.setDefaults(Notification.DEFAULT_ALL);
                        // builder.setContentIntent(pendingIntent);

                        Notification notification1 = successNotification.build();
                        notification1.flags |= Notification.FLAG_AUTO_CANCEL;

                        NotificationManager notificationManager1 = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager1.notify((int) cc.getLong(cc.getColumnIndex(Resources.TAG_TIME_STAMP)), notification1);
                        exceptionCounter++;
                    }
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("fail", "forth");
                }
            } while (cc.moveToNext());

//            Toast.makeText(this, cc.getColumnIndex(Resources.TAG_ID_SPEC) + " " + cc.getColumnIndex(Resources.TAG_APPSPECIALIZATION_NAME) + " " + cc.getColumnIndex(Resources.TAG_APP_DATE) + " " + cc.getColumnIndex(Resources.TAG_APP_TIME) + " " + cc.getColumnIndex(Resources.TAG_APP_ONAME) + " " + cc.getColumnIndex(Resources.TAG_APP_OPHONE) + " " + cc.getColumnIndex(Resources.TAG_APP_DESCRIPTION) + " " + cc.getColumnIndex("time_stamp") + " " + cc.getColumnIndex("checkBox"), Toast.LENGTH_LONG).show();

        } else {
//            save();
        }
        Log.d("counter", " "+counter);
        cc.close();
        return true;
    }

    private void restartNotify(String title, long stamp) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        intent.putExtra("title", reqSpecName);
//        intent.putExtra("time_stamp", timeStamp);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) timeStamp, intent, PendingIntent.FLAG_CANCEL_CURRENT );
//        // На случай, если мы ранее запускали активити, а потом поменяли время,
//        // откажемся от уведомления
//        //am.cancel(pendingIntent);
//        // Устанавливаем разовое напоминание
//        Log.d("myLogs", "TimeStamp main" + stamp);
//        Log.d("myLogs", "TimeStamp main" + System.currentTimeMillis());
//        am.set(AlarmManager.RTC_WAKEUP, stamp, pendingIntent);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("time_stamp", timeStamp);
        //intent.putExtra("color", modelTask.getPriorityColor());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), (int) timeStamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, timeStamp, pendingIntent);



    }
}