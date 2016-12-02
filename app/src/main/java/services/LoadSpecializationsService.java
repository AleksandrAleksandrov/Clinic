package services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.aleksandr.aleksandrov.clinic.AppealActivity;
import com.aleksandr.aleksandrov.clinic.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import adapters.StringWithTag;
import database.DBHelper;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class LoadSpecializationsService extends Service {

    DBHelper dbHelper;
    public static boolean serviceState;
    HttpURLConnection urlConnection;

    //    JSONArray appspecializations = null;
//    JSONObject jsonObj = null;
    List<StringWithTag> itemList = null;
    private String error = null;

    public void onCreate() {
        super.onCreate();
        serviceState = true;
        Log.d("myLogs", "load service starts");
        //Создаем объект для создания и управления БД.
        dbHelper = new DBHelper(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        MyRun mr = new MyRun();
        //es.execute(mr);
        new Thread(mr).start();
        return START_REDELIVER_INTENT;
    }

    public void onDestroy() {
        serviceState = false;
        Log.d("myLogs", "service destroyed");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyRun implements Runnable {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c;
        Intent intent;
        @Override
        public void run() {
            intent = new Intent(AppealActivity.BROADCAST_ACTION);
            loadSpecialization();

        }

        public void loadSpecialization() {
//            while (true) {

//                try {
//                    Log.d("get", "1"+jsonObj);
            URL url = null;
            try {
                url = new URL(Resources.GET_ALL_SPECIALIZATIONS);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
//                jsonObj = new JSONObject(result.toString());
//                String s = result.toString();
//                Log.d("get", jsonObj.);
//                Log.d("get", " "+jsonObj.toString());
//                int success = jsonObj.getInt(Resources.TAG_SUCCESS);
//                    if (success == 1) {
//                appspecializations = jsonObj.getJSONArray("");
                JSONObject obj = new JSONObject(result.toString());
                String sss = result.toString();
                JSONArray appspecializations = new JSONArray();
//                appspecializations = obj.getJSONArray(sss);

                appspecializations = obj.toJSONArray(obj.names());

                int size = appspecializations.length();
                Log.d("get", "3"+appspecializations.toString());


                itemList = new ArrayList<StringWithTag>();
                db.delete(dbHelper.SPECIALIZATIONS_TABLE, null, null);
                Log.d("get", "1"+appspecializations.length());
//                Log.d("get", "array length"+array.length());
//                for (JSONObject jd : obj) {
//
//                }
                Log.d("get", "array length "+obj.toString());
                for (int i = 0; i < size; i++) {

//                    JSONObject c = obj.getJSONObject(i);
                    Log.d("get", "2"+obj.get("19").toString());
//                    int id = obj.getInt(Resources.TAG_APPSPECIALIZATION_ID);
//                    String name = obj.getString(Resources.TAG_APPSPECIALIZATION_NAME);

                    cv.put(Resources.TAG_APPSPECIALIZATION_ID, i);
                    cv.put(Resources.TAG_APPSPECIALIZATION_NAME, appspecializations.getString(i).toString());
//                    cv.put(Resources.TAG_APPSPECIALIZATION_ID, id);
//                    cv.put(Resources.TAG_APPSPECIALIZATION_NAME, name);
                    db.insert(dbHelper.SPECIALIZATIONS_TABLE, null, cv);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

//                    }


//                } catch (MalformedURLException e) {
//                    error = "Что-то пошло не так";
//                    e.printStackTrace();
//                } catch (IOException e) {
//
//                    error = "Не удалось подкулючится к серверу";
//                    Log.d("myLogs", error);
//                    c = db.query(dbHelper.SPECIALIZATIONS_TABLE, null, null, null, null, null, null);
//                    if (c.getCount() == 0) {
//                        intent.putExtra("task", 0);
//                        sendBroadcast(intent);
//                        try {
//                            TimeUnit.SECONDS.sleep(2);
//                        } catch (InterruptedException ex) {
//                            ex.printStackTrace();
//                        }
////                        run();
//                    } else {
//                        try {
//                            TimeUnit.MINUTES.sleep(30);
//                        } catch (InterruptedException ex) {
//                            ex.printStackTrace();
//                        }
////                        run();
//                    }
//                    e.printStackTrace();
//                } catch (JSONException e) {
//
//                    error = "Что-то пошло не так";
//                    e.printStackTrace();
//                } finally {
//                    urlConnection.disconnect();
//                }
            Log.d("myLogs", "finish");

            intent.putExtra("task", 1);
            sendBroadcast(intent);
            try {
                TimeUnit.DAYS.sleep(Resources.CHECK_FOR_SPECIALIZATIONS_UPDATES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            }
        }
    }
}
