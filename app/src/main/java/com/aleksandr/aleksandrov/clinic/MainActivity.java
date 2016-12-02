package com.aleksandr.aleksandrov.clinic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import adapters.RemindObject;
import adapters.ReminderAdapter;
import alarm.AlarmHelper;
import database.DBHelper;
import android.app.DialogFragment;
import fragments.DatePickerFragment;
import fragments.DatePickerFragmentFromReminder;
import fragments.DateTimePickerDialog;
import fragments.EditDialogFragment;
import fragments.MyDialog;
import fragments.TimePickerFragment;
import fragments.TimePickerFragmentFromReminder;
import fragments.Utils;
import services.LoadSpecializationsService;
import services.SendAppealService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DateTimePickerDialog.DateTimeListener {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_AND_CALL_PHONE = 0;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    EditDialogFragment custom;
    Calendar calendar = Calendar.getInstance();
    Dialog dialog;

    FloatingActionButton floatingActionButton;

    BroadcastReceiver br;
    public final static String BROADCAST_ACTION = "ru.startandroid.develop.p0961servicebackbroadcast";

    ExpandableListView expandableListView;
    String[] groups = new String[]{"Уважаемые пациенты!"};
    String[] phonesHTC = new String[]{"Заполните форму и наш администратор свяжется с Вами в ближайшее время!", "*В случае, если Вы оставили запись в субботу, воскресенье или праздничный день, Вам перезвонят в ближайший рабочий день!", "**Время записи Вы выбираете приблизительно, в случае, если окажется, что на выбранное Вами время уже есть посетитель, Вам будет предложены другие, наиболее подходящие для Вас варианты.", "***Заполнением данной формы, Вы даёте согласие ООО «Клиника Мотор-Сич» на обработку своих персональных данных, в соответствии с Законом Украины «О защите персональных данных»."};
    // коллекция для групп
    ArrayList<Map<String, String>> groupData;

    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> childDataItem;

    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> childData;
    // в итоге получится childData = ArrayList<childDataItem>

    // список аттрибутов группы или элемента
    Map<String, String> m;

    AlarmHelper alarmHelper;

    ListView remindList;


    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

    //Cursor calendar;
    public static List<RemindObject> items;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_appeal);
        builder.setMessage(getString(R.string.delete_appeal));
        builder.setIcon(R.drawable.logo);
        builder.setCancelable(false);




        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabCall);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    makeCallToClinic();
                }

            }
        });

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("done", 0)  == 1) {
                    build();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        remindList = (ListView) findViewById(R.id.remindList);
        // заполняем коллекцию групп из массива с названиями групп
        groupData = new ArrayList<Map<String, String>>();
        for (String group : groups) {
            // заполняем список аттрибутов для каждой группы
            m = new HashMap<String, String>();
            m.put("groupName", group); // имя компании
            groupData.add(m);
        }

        // список аттрибутов групп для чтения
        String groupFrom[] = new String[] {"groupName"};
        // список ID view-элементов, в которые будет помещены аттрибуты групп
        int groupTo[] = new int[] {android.R.id.text1};


        // создаем коллекцию для коллекций элементов
        childData = new ArrayList<ArrayList<Map<String, String>>>();

        // создаем коллекцию элементов для первой группы
        childDataItem = new ArrayList<Map<String, String>>();
        // заполняем список аттрибутов для каждого элемента
        for (String phone : phonesHTC) {
            m = new HashMap<String, String>();
            m.put("phoneName", phone); // название телефона
            childDataItem.add(m);
        }
        // добавляем в коллекцию коллекций
        childData.add(childDataItem);
        // список аттрибутов элементов для чтения
        String childFrom[] = new String[] {"phoneName"};
        // список ID view-элементов, в которые будет помещены аттрибуты элементов
        int childTo[] = new int[] {android.R.id.text1};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo, childData, android.R.layout.simple_list_item_1, childFrom, childTo);

//        expandableListView = (ExpandableListView) findViewById(R.id.patientInfo);
//        expandableListView.setAdapter(adapter);

        build();

        if (!LoadSpecializationsService.serviceState) startService(new Intent(MainActivity.this, LoadSpecializationsService.class));
        if (!SendAppealService.serviceState) startService(new Intent(MainActivity.this, SendAppealService.class));

        AlarmHelper.getInstance().init(getApplication());
        alarmHelper = AlarmHelper.getInstance();




    }

    private void makeCallToClinic() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Random random = new Random();
        int i = random.nextInt(2);
        ArrayList<String> phones = new ArrayList<>();
        phones.add(Resources.PHONE_1);
        phones.add(Resources.PHONE_2);

        intent.setData(Uri.parse("tel:" + phones.get(i)));

        startActivity(intent);
    }


    public void build() {
        dbHelper = new DBHelper(this.getApplicationContext());
        db = dbHelper.getWritableDatabase();


        items = new ArrayList<>();
        Cursor c = db.query(dbHelper.REMIND_TABLE, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int sp = c.getColumnIndex(dbHelper.SPECIALIZATION_COLUMN);
            int dt = c.getColumnIndex(dbHelper.DATE_COLUMN);
            int id = c.getColumnIndex(Resources._ID);
            do {
                if (c.getLong(dt) + Resources.TIME < System.currentTimeMillis()) {
                    db.delete(dbHelper.REMIND_TABLE, Resources._ID + " = " + c.getInt(id), null);
                } else {
                    items.add(new RemindObject(c.getString(sp), c.getLong(dt), c.getInt(id)));
                }
            } while (c.moveToNext());

            c.close();
            doo();
        } else {
            doo();
        }
    }

    public void doo() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        //remindList.getLayoutParams().height = 0;
        if (items.size() > 4 ) {
            remindList.getLayoutParams().height = display.getHeight()/2;
        }

        ReminderAdapter ss = new ReminderAdapter(MainActivity.this, items);


        remindList.setAdapter(ss);
        remindList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                builder.setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alarmHelper.removeAlarm(items.get(position).getTime());
                        //db.update(DBHelper.REMIND_TABLE, cv, DBHelper.DATE_COLUMN + " = " + 1, null);
                        db.delete("reminds", "_ID = ?", new String[]{Integer.toString(items.get(position).getId())});
                        //btnTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                        db.close();


                        build();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        remindList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogFragment datePickerFragment = new DatePickerFragmentFromReminder() {


                    @Override
                    public void onCreate(Bundle savedInstanceState) {

                        super.onCreate(savedInstanceState);
                        DatePickerFragment.c.setTimeInMillis(items.get(position).getTime());
                    }

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        DialogFragment timePickerFragment = new TimePickerFragmentFromReminder() {
                            @Override
                            public void onCreate(Bundle savedInstanceState) {
                                super.onCreate(savedInstanceState);
                                TimePickerFragment.c.setTimeInMillis(items.get(position).getTime());
                            }
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                alarmHelper.removeAlarm(items.get(position).getTime());
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                ContentValues cv = new ContentValues();
                                cv.put("date", calendar.getTimeInMillis());
                                db.update("reminds", cv, "_ID = ?", new String[]{Integer.toString(items.get(position).getId())});
                                db.close();
                                build();
                                alarmHelper.setAlarm(items.get(position).getDoctor(), items.get(position).getTime());
                            }
                        };
                        timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
                    }

                };
                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
            }
        });




    }



    public void onClickAppeal(View v) {

        //Intent i = new Intent(getApplicationContext(), AppealActivity.class);
        startActivity(new Intent(MainActivity.this, AppealActivity.class));
    }

    public void onClickCoordinates(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Resources.CLINIC_LATITUDE + "," + Resources.CLINIC_LONGITUDE);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public void onClickInfo(View view) {
        DialogFragment dialogFragment = new MyDialog();
        dialogFragment.show(getFragmentManager(), "info");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCallToClinic();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(br);
    }

    private void showDateTimeDialog() {
        DateTimePickerDialog pickerDialog = new DateTimePickerDialog(this, false, this);
        pickerDialog.show();
    }

//    @Override
//    public void onDateTimeSelected(int year, int month, int day, int hour, int min, int am_pm) {
//        String text = day + "/" + month + "/" + year + " - " + hour + ":" + min;
//        if (am_pm != -1)
//            text = text + (am_pm == Calendar.AM ? "AM" : "PM");
//        ((TextView) findViewById(R.id.Date)).setText(text);
//    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateTimeSelected(int year, int month, int day, int hour, int min, int am_pm) {

    }
}
