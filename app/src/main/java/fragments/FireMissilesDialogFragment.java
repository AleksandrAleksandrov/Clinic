package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.aleksandr.aleksandrov.clinic.R;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class FireMissilesDialogFragment extends DialogFragment {

    String title = "";
    String message = "";
    public void setMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

//    public void onDestroyView() {
//        if (getDialog() != null && getRetainInstance())
//            getDialog().setOnDismissListener(null);
//
//        super.onDestroyView();
//    }
}