package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.aleksandr.aleksandrov.clinic.R;

import java.util.Calendar;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class DatePickerFragmentFromReminder extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Calendar c = Calendar.getInstance();
    //Button btnDate;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //setCancelable(false);
        //btnDate = (Button) findViewById(R.id.date);
        //c.setTimeInMillis(12341234);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePickerDialog;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        btnDate.setText(Utils.getDate(c.getTimeInMillis()));
    }
}
