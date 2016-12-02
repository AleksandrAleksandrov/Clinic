package com.aleksandr.aleksandrov.clinic;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.MyAdapter;
import adapters.StringWithTag;
import database.DBHelper;

import fragments.DatePickerFragmentFromReminder;
import fragments.FireMissilesDialogFragment;
import fragments.ProgressDialogFragment;
import fragments.TimePickerFragmentFromReminder;
import fragments.Utils;
import services.LoadSpecializationsService;
import services.SendAppealService;

/**
 * Created by Aleksandr on 9/5/2016.
 */

public class AppealActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    final int MY_PERMISSIONS_REQUEST_ACCOUNT_MANAGER = 1;
    final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;
    final int MY_PERMISSIONS_REQUEST_GET_ACCOUNT = 3;

    //Timetable
    TableLayout first, second, third, fourth, fifth, sixth, seventh, eighth, nineth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth, seventeenth, eighteenth, nineteenth;
    int t = 0;
    LoadSpecializations loadSpecializations;
    //Variable for mobile phone number.
    String mPhoneNumber = null;
    public static boolean appealActivityState = false;
    String fullNameFromVK = null;
    boolean dataPicker = false, timePicker = false;

    List<StringWithTag> itemList = new ArrayList<StringWithTag>();
    // Specializations specializations;
    // SendAppeal sendAppeal;
    String answer = null, error = null;

    ArrayAdapter<String> stringAdapterForAutoCompleteTextView;

    ContentValues cv2;
    DBHelper dbHelper = new DBHelper(this);
    public SQLiteDatabase db;
    public final static String BROADCAST_ACTION = "com.aleksandr.aleksandrov.clinic";
    BroadcastReceiver br;

    Spinner spinner;
    Button btnDate, btnTime;
    AutoCompleteTextView autoCompleteTextViewForEmail;
    EditText editName, editPhone, editDescription;
    CheckBox checkBox;
    public static boolean checked;

    String reqTag = null, reqSpecName = null, reqDate = null, reqTime = null, reqName = null, reqEmail = null, reqPhone = null, reqDescription = null;

    SharedPreferences sharedPreferences;
    // Tags for savable fields.
    String FULL_NAME = "full_name", PHONE_NUMBER = "phone_number";
    FireMissilesDialogFragment fireMissilesDialogFragment;
    ProgressDialogFragment progressDialogFragment1 = new ProgressDialogFragment();

    final Calendar calendar = Calendar.getInstance();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appealActivityState = true;


        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

        setContentView(R.layout.appeal_layout);

        editName = (EditText) findViewById(R.id.editFullName);
        autoCompleteTextViewForEmail = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        editPhone = (EditText) findViewById(R.id.editPhoneNumber);
        editDescription = (EditText) findViewById(R.id.editExtraInfo);
        spinner = (Spinner) findViewById(R.id.spinner);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppealActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, MY_PERMISSIONS_REQUEST_GET_ACCOUNT);
        } else {
            getEmailAddress();
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAppeal);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
////        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//        actionBar.setDisplayHomeAsUpEnabled(true);


        //Timetable
        timeTable();
        btnDate = (Button) findViewById(R.id.date);
        final DialogFragment datePickerFragment = new DatePickerFragmentFromReminder() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                btnDate.setText(Utils.getDate(calendar.getTimeInMillis()));
                dataPicker = true;
            }



        };
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerFragment.show(getFragmentManager(), "DatePickerFragment");
//        DialogFragment dataPickerFragment = new DatePickerFragment();
//        dataPickerFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });


        btnTime = (Button) findViewById(R.id.time);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment = new TimePickerFragmentFromReminder() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        btnTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                        timePicker = true;
                    }

                };
                timePickerFragment.show(getFragmentManager(), "TimePickerFragment");
//        DialogFragment timePickerFragment = new TimePickerFragment();
//        timePickerFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });



        sharedPreferences = getSharedPreferences("MySaves", MODE_PRIVATE);
        String savedName = sharedPreferences.getString(FULL_NAME, "");
        if (savedName.equals("")) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AppealActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, MY_PERMISSIONS_REQUEST_ACCOUNT_MANAGER);
            } else {
                getFullNameFromVKontakte();
            }

        } else {
            editName.setText(savedName);
        }

        String savedPhoneNumber = sharedPreferences.getString(PHONE_NUMBER, "");


        if (savedPhoneNumber.equals("")) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AppealActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                getPhoneNumber();
            }


        } else {
            editPhone.setText(savedPhoneNumber);
        }


        loadSpecializations = new LoadSpecializations();
        loadSpecializations.execute();

        br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("myLogs", "broadcast");
                if (1 == intent.getIntExtra("task", 0)) {
                    Log.d("myLogs", "broadcast inside");
                    loadSpecializations = new LoadSpecializations();
                    loadSpecializations.execute();

                }
                if (0 == intent.getIntExtra("task", 0)) {
                    progressDialogFragment1.dismiss();
                    fireMissilesDialogFragment = new FireMissilesDialogFragment();
                    fireMissilesDialogFragment.setMessage(getBaseContext().getString(R.string.error), getBaseContext().getString(R.string.can_not_connect_to_server));
                    fireMissilesDialogFragment.show(getFragmentManager(), "f");
                }
            }
        };
        //Create filter for BroadcastReceiver.
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        //Регестрируем (включаем) BroadcastReceiver.
        registerReceiver(br, intFilt);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void timeTable() {
        first = (TableLayout) findViewById(R.id.first);
        second = (TableLayout) findViewById(R.id.second);
        third = (TableLayout) findViewById(R.id.third);
        fourth = (TableLayout) findViewById(R.id.fourth);
        fifth = (TableLayout) findViewById(R.id.fifth);
        sixth = (TableLayout) findViewById(R.id.sixth);
        seventh = (TableLayout) findViewById(R.id.seventh);
        eighth = (TableLayout) findViewById(R.id.eighth);
        nineth = (TableLayout) findViewById(R.id.ninth);
        tenth = (TableLayout) findViewById(R.id.tenth);
        eleventh = (TableLayout) findViewById(R.id.eleventh);
        twelfth = (TableLayout) findViewById(R.id.twelfth);
        thirteenth = (TableLayout) findViewById(R.id.thirteenth);
        fourteenth = (TableLayout) findViewById(R.id.fourteenth);
        fifteenth = (TableLayout) findViewById(R.id.fifteenth);
        sixteenth = (TableLayout) findViewById(R.id.sixteenth);
        seventeenth = (TableLayout) findViewById(R.id.seventeenth);
        eighteenth = (TableLayout) findViewById(R.id.eighteenth);
        nineteenth = (TableLayout) findViewById(R.id.nineteenth);
    }

    public void saveFields() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!editName.equals("")) {
            editor.putString(FULL_NAME, editName.getText().toString());
            editor.commit();
        }
        if (!editPhone.equals("")) {
            editor.putString(PHONE_NUMBER, editPhone.getText().toString());
            editor.commit();
        }
    }


    protected void onDestroy() {
        saveFields();
        unregisterReceiver(br);
        super.onDestroy();
        appealActivityState = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPhoneNumber();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getEmailAddress();
                    getFullNameFromVKontakte();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getFullNameFromVKontakte();
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


    public void onClickSendAppeal(View v) {
        reqDate = btnDate.getText().toString();
        reqTime = btnTime.getText().toString();
        reqName = editName.getText().toString();
        reqEmail = autoCompleteTextViewForEmail.getText().toString();
        reqPhone = editPhone.getText().toString();
        reqDescription = editDescription.getText().toString();
        checked = checkBox.isChecked();
        boolean show = false;
        String message = "";

        if (reqTag == null) {
            if (message.equals("")) {
                message += getBaseContext().getString(R.string.specialization_of_doctor);
            } else {
                message += "\n" + getBaseContext().getString(R.string.specialization_of_doctor);
            }
            show = true;
        }
        if (!dataPicker) {
            if (message.equals("")) {
                message += getBaseContext().getString(R.string.data_of_visit_the_doctor);
            } else {
                message += "\n" + getBaseContext().getString(R.string.data_of_visit_the_doctor);
            }
            show = true;
        }
        if (!timePicker) {
            if (message.equals("")) {
                message += getBaseContext().getString(R.string.time_of_visit_the_doctor);
            } else {
                message += "\n" + getBaseContext().getString(R.string.time_of_visit_the_doctor);
            }
            show = true;
        }
        if (reqName.equals("")) {
            if (message.equals("")) {
                message += getBaseContext().getString(R.string.their_name);
            } else {
                message += "\n" + getBaseContext().getString(R.string.their_name);
            }
            show = true;
        }
        if (reqPhone.equals("")) {
            if (message.equals("")) {
                message += getBaseContext().getString(R.string.phone_number);
            } else {
                message += "\n" + getBaseContext().getString(R.string.phone_number);
            }
            show = true;
        }
        if (reqPhone.length() < 6) {
            if (message.equals("")) {
                message += getBaseContext().getString(R.string.phone_number_uncorrect);
            } else {
                message += "\n" + getBaseContext().getString(R.string.phone_number_uncorrect);
            }
            show = true;
        }
        if (show) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AppealActivity.this);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            builder.setMessage(message).setTitle(R.string.fill_in_fields);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            new Send().execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Appeal Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.aleksandr.aleksandrov.clinic/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Appeal Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.aleksandr.aleksandrov.clinic/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    class LoadSpecializations extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            if (!progressDialogFragment1.isAdded()) {
                progressDialogFragment1.show(getFragmentManager(), "ff");
            }



            spinner.dispatchSetActivated(false);
            //spinner.setAdapter(null, R.layout.contact_spinner_row_nothing_selected, getApplicationContext());
        }

        @Override
        protected Void doInBackground(Void... params) {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            itemList.clear();
            //specializations = new Specializations();
            db = dbHelper.getWritableDatabase();

            //Делаем запрос всех данных из таблицы mytable, получаем Cursor.
            Cursor c = db.query(dbHelper.SPECIALIZATIONS_TABLE, null, null, null, null, null, null);

            if (c.getCount() == 0) {

                startService(new Intent(AppealActivity.this, LoadSpecializationsService.class));
            }
            //Ставим позицию курсора на первую строку выборки если в выборке нет строк, вернется false.
            if (c.moveToFirst()) {
                //Определяем номера столбцов по имени в выборке.
                //int idColIndex = calendar.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex(Resources.TAG_APPSPECIALIZATION_NAME);
                int emailColIndex = c.getColumnIndex(Resources.TAG_APPSPECIALIZATION_ID);

                do {
                    //Получаем значения по номерам столбцов и пишум все в лог.
                    //Log.d(LOG_TAG, "ID = " + calendar.getInt(idColIndex)
                    //      + ", name = " + calendar.getString(nameColIndex)
                    //    + ", email = " + calendar.getString(emailColIndex));

                    itemList.add(new StringWithTag(c.getString(nameColIndex), c.getString(emailColIndex)));
                    //Переход на следующую строку, а если слудующей нет (текущая - последняя), то false - выходим из цикла.
                } while (c.moveToNext());
            } else  //progressDialogFragment1.show(getFragmentManager(), "ff");
                c.close();
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!itemList.isEmpty()) progressDialogFragment1.dismiss();


//            if (specializations.answer() == null) {
//                error = specializations.error();
//                fireMissilesDialogFragment = new FireMissilesDialogFragment(getBaseContext().getString(R.string.error), getBaseContext().getString(R.string.can_not_connect_to_server));
//                fireMissilesDialogFragment.show(getFragmentManager(), "ff");
//            } else {
//                itemList = specializations.answer();
//            }


            MyAdapter myAdapter = new MyAdapter(AppealActivity.this, itemList);
            spinner.setAdapter(myAdapter);
//            ArrayAdapter<StringWithTag> adapter = new ArrayAdapter<StringWithTag>(AppealActivity.this, R.layout.sp, itemList);
//            adapter.setDropDownViewResource(R.layout.sd);
            spinner.setAdapter(new NothingSelectedSpinnerAdapter(myAdapter, R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parant, View v, int pos, long id) {
                    StringWithTag s = (StringWithTag) parant.getItemAtPosition(pos);
                    if (s != null) {
                        reqTag = s.tag;
                        Log.d("myLogs", "specName" + s.string);
                        reqSpecName = s.string;
                    }
                    //Timetable
                    if (t != 0) {
                        if (t == 1) {
                            first.setVisibility(View.GONE);
                        } else if (t == 3) {
                            second.setVisibility(View.GONE);
                        } else if (t == 4) {
                            third.setVisibility(View.GONE);
                        } else if (t == 5) {
                            fourth.setVisibility(View.GONE);
                        } else if (t == 6) {
                            fifth.setVisibility(View.GONE);
                        } else if (t == 7) {
                            sixth.setVisibility(View.GONE);
                        } else if (t == 8) {
                            seventh.setVisibility(View.GONE);
                        } else if (t == 9) {
                            eighth.setVisibility(View.GONE);
                        } else if (t == 10) {
                            nineth.setVisibility(View.GONE);
                        } else if (t == 11) {
                            tenth.setVisibility(View.GONE);
                        } else if (t == 12) {
                            eleventh.setVisibility(View.GONE);
                        } else if (t == 13) {
                            twelfth.setVisibility(View.GONE);
                        } else if (t == 14) {
                            thirteenth.setVisibility(View.GONE);
                        } else if (t == 15) {
                            fourteenth.setVisibility(View.GONE);
                        } else if (t == 16) {
                            fifteenth.setVisibility(View.GONE);
                        } else if (t == 17) {
                            sixteenth.setVisibility(View.GONE);
                        } else if (t == 18) {
                            seventeenth.setVisibility(View.GONE);
                        } else if (t == 19) {
                            eighteenth.setVisibility(View.GONE);
                        } else if (t == 20) {
                            nineteenth.setVisibility(View.GONE);
                        }
                    }
                    if (s != null) {
                        if (s.tag.equals("1")) {
                            first.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("3")) {
                            second.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("4")) {
                            third.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("5")) {
                            fourth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("6")) {
                            fifth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("7")) {
                            sixth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("8")) {
                            seventh.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("9")) {
                            eighth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("10")) {
                            nineth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("11")) {
                            tenth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("12")) {
                            eleventh.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("13")) {
                            twelfth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("14")) {
                            thirteenth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("15")) {
                            fourteenth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("16")) {
                            fifteenth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("17")) {
                            sixteenth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("18")) {
                            seventeenth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("19")) {
                            eighteenth.setVisibility(View.VISIBLE);
                        } else if (s.tag.equals("20")) {
                            nineteenth.setVisibility(View.VISIBLE);
                        }

                        t = Integer.parseInt(s.tag);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

        }
    }


    class Send extends AsyncTask<Void, Void, Void> {
        ProgressDialogFragment progressDialogFragment;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialogFragment = new ProgressDialogFragment();
            progressDialogFragment.show(getFragmentManager(), "show");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //sendAppeal = new SendAppeal(reqTag, reqDate, reqTime, reqName, reqEmail, reqPhone, reqDescription);

//            startService(new Intent(AppealActivity.this, SendAppealService.class)
//                    .putExtra(Resources.TAG_ID_SPEC, reqTag)
//                    .putExtra(Resources.TAG_APPSPECIALIZATION_NAME, reqSpecName)
//                    .putExtra(Resources.TAG_APP_DATE, reqDate)
//                    .putExtra(Resources.TAG_APP_TIME, reqTime)
//                    .putExtra(Resources.TAG_APP_ONAME, reqName)
//                    .putExtra(Resources.TAG_APP_OEMAIL, reqEmail)
//                    .putExtra(Resources.TAG_APP_OPHONE, reqPhone)
//                    .putExtra(Resources.TAG_APP_DESCRIPTION, reqDescription)
//                    .putExtra("time_stamp", calendar.getTimeInMillis())
//                    .putExtra("checkBox", checked));


            cv2 = new ContentValues();
            cv2.put(Resources.TAG_ID_SPEC, reqTag);
            cv2.put(Resources.TAG_APPSPECIALIZATION_NAME, reqSpecName);
            cv2.put(Resources.TAG_APP_DATE, reqDate);
            cv2.put(Resources.TAG_APP_TIME, reqTime);
            cv2.put(Resources.TAG_APP_ONAME, reqName);
            cv2.put(Resources.TAG_APP_OEMAIL, reqEmail);
            cv2.put(Resources.TAG_APP_OPHONE, reqPhone);
            cv2.put(Resources.TAG_APP_DESCRIPTION, reqDescription);
            cv2.put("time_stamp", calendar.getTimeInMillis());
            db = dbHelper.getWritableDatabase();
            db.insert(dbHelper.SEND_APPEAL_AGAIN, null, cv2);
            if (!SendAppealService.serviceState)
                startService(new Intent(AppealActivity.this, SendAppealService.class));
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialogFragment.dismiss();
            finish();
//            AlertDialog.Builder builder = new AlertDialog.Builder(AppealActivity.this);
//            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.dismiss();
//
//                }
//            });
//            builder.setMessage("Ваша заявка принята. В ближайшее время с Вами свяжется наш администратор.");
//            android.app.AlertDialog dialog = builder.create();
//            dialog.show();


//            if (sendAppeal.answer() == null) {
//                error = sendAppeal.error();
//                AlertDialog.Builder builder = new AlertDialog.Builder(AppealActivity.this);
//                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
//                builder.setMessage(error).setTitle(R.string.error);
//                android.app.AlertDialog dialog = builder.create();
//                dialog.show();
//            } else {
//                answer = sendAppeal.answer();
//                fireMissilesDialogFragment = new FireMissilesDialogFragment("Вы записались к " + spinner.getSelectedItem(), answer);
//                fireMissilesDialogFragment.show(getFragmentManager(), "ff");
//            }

        }
    }


    private void getEmailAddress() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppealActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, MY_PERMISSIONS_REQUEST_GET_ACCOUNT);
        } else {
            AccountManager accountManager = AccountManager.get(getApplicationContext());
            Account[] accounts = accountManager.getAccounts();
            ArrayList<String> arrayEmail = new ArrayList<String>();
            List<String> stringArrayList = new ArrayList<>();
            for (Account account : accounts) {
                if (account.name.contains("@")) {
                    stringArrayList.add(account.name);
                }
            }
            int x = 0;
            for (int i = 0; i < stringArrayList.size();i++) {
                if (i==0) {
                    autoCompleteTextViewForEmail.setText(stringArrayList.get(i));
                }
                arrayEmail.add(stringArrayList.get(i));
            }
//            for (String s : stringArrayList) {
//                if (s != null) {
//                    if (x == 0) autoCompleteTextViewForEmail.setText(s);
//                    arrayEmail.add(s);
//                } else {
//                    autoCompleteTextViewForEmail.setText("");
//                }
//
//                x++;
//            }
            stringAdapterForAutoCompleteTextView = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayEmail);
            autoCompleteTextViewForEmail.setThreshold(1);

            autoCompleteTextViewForEmail.setAdapter(stringAdapterForAutoCompleteTextView);
            autoCompleteTextViewForEmail.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    autoCompleteTextViewForEmail.showDropDown();
                    return false;
                }
            });
        }

    }

    /**
     * Getting full name via VK.com
     */
    private void getFullNameFromVKontakte() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppealActivity.this, new String[] {Manifest.permission.GET_ACCOUNTS}, MY_PERMISSIONS_REQUEST_ACCOUNT_MANAGER);
        } else {
            AccountManager accountManager = AccountManager.get(getApplicationContext());
            Account[] accounts = accountManager.getAccountsByType("com.vkontakte.account");
            for (Account account : accounts) {
                fullNameFromVK = account.name;
            }
            editName.setText(fullNameFromVK);
        }



    }

    /**
     * Getting phone number.
     */
    private void getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();
        //Log.d("myLogs","telephone"+ mPhoneNumber +"@");
        if (mPhoneNumber != null) {
            new CutPhoneNumber().execute();
        }
    }

    /**
     * Cutting first three characters and fill in phone number field.
     */
    class CutPhoneNumber extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String s = null;
            for (int i = 0; i < mPhoneNumber.length(); i++) {
                s = mPhoneNumber.substring(3, mPhoneNumber.length());
            }
            mPhoneNumber = s;
            Log.d("myLogs", s);
            return s;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("myLogs", result);
            editPhone.setText(result);
        }
    }
}