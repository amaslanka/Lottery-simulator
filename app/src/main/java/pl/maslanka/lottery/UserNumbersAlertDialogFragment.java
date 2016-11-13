package pl.maslanka.lottery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Artur on 20.10.2016.
 */

public class UserNumbersAlertDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.wrong_numbers));
        builder.setMessage(getResources().getString(R.string.remember_not_to_repeat));


        builder.setPositiveButton(getResources().getString(R.string.ok), mListener);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogInterface.OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DialogInterface.OnClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
