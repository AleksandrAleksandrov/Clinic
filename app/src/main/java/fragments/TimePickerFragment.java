package fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static Calendar c = Calendar.getInstance();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        //setCancelable(false);

        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Picker", "Correct behavior!");
                        onOk();
                    }
                });
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Picker", "Cancel!");
                        onCancel();
                    }
                });
        return timePickerDialog;
    }

    public void onOk() {

    }

    public void onCancel() {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

//    Button time;
//    final Calendar c = Calendar.getInstance();
//    int hour = c.get(Calendar.HOUR_OF_DAY);
//    int minute = c.get(Calendar.MINUTE);
//
//
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setRetainInstance(true);
//        setCancelable(false);
//        time = (Button) getActivity().findViewById(R.id.time);
//        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
//    }
//
//    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
//        hour = hourOfDay;
//        minute = minuteOfHour;
//        if (minute == 0) {
//            time.setText(Integer.toString(hour) + ":" + Integer.toString(minute) + "0");
//        } else if (minute < 10) {
//            time.setText(Integer.toString(hour) + ":0" + Integer.toString(minute));
//        } else time.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
//    }
//    public void onDestroyView() {
//        if (getDialog() != null && getRetainInstance())
//            getDialog().setOnDismissListener(null);
//
//        super.onDestroyView();
//    }
}