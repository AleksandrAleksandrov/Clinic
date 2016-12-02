package fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.aleksandr.aleksandrov.clinic.R;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class ProgressDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    public void onCancel(DialogInterface dialog) {
        getActivity().finish();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        //dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }
}
