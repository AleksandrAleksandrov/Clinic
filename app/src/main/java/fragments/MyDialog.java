package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.aleksandr.aleksandrov.clinic.R;

/**
 * Created by Aleksandr on 11/15/2016.
 */

public class MyDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.info1) + "\n" + "\n" + getString(R.string.info2) + "\n" + "\n" + getString(R.string.info3) + "\n" + "\n" + getString(R.string.info4))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return adb.create();
    }
}
