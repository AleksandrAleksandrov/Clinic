package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.aleksandr.aleksandrov.clinic.R;

import java.util.Calendar;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static Calendar c = Calendar.getInstance();


    Button btnDate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //setCancelable(false);
//        btnDate = (Button) findViewById(R.id.date);
        //c.setTimeInMillis(12341234);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Picker", "Correct behavior!");
                        onOk();
                    }
                });
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Picker", "Cancel!");
                        onCancel();
                    }
                });
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;
    }

    public void onOk() {

    }

    public void onCancel() {
        return;
    }

    public Calendar getC() {
        return c;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, monthOfYear);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        btnDate.setText(Utils.getDate(c.getTimeInMillis()));
    }
//    Button date;
//    final Calendar c = Calendar.getInstance();
//    int year = c.get(Calendar.YEAR);
//    int month = c.get(Calendar.MONTH);
//    int day = c.get(Calendar.DAY_OF_MONTH);
//    DatePickerDialog datePickerDialog;
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setRetainInstance(true);
//        setCancelable(false);
//        date = (Button) getActivity().findViewById(R.id.date);
//        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        return datePickerDialog;
//    }
//
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//        this.year = year;
//        this.month = month;
//        this.day = day;
//        String serviceState = Integer.toString(this.year) + "-";
//        if (this.month + 1 < 10){
//            serviceState += "0"+ Integer.toString(this.month+1) + "-";
//        } else serviceState+= Integer.toString(this.month+1) + "-";
//
//        if (day < 10) {
//            serviceState += "0" + Integer.toString(this.day);
//        } else serviceState += Integer.toString(this.day);
//        date.setText(serviceState);
//    }
//    public void onDestroyView() {
//        if (getDialog() != null && getRetainInstance())
//            getDialog().setOnDismissListener(null);
//
//        super.onDestroyView();
//    }
}