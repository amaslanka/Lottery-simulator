package pl.maslanka.lottery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Artur on 11.10.2016.
 */

public class CompComp extends AppCompatActivity implements Serializable {

    private static final String STATE_NUM_PICKER = "state_num_picker";
    private static final String TAG_TASK_FRAGMENT = "drawing_comp_comp";
    private ProgressDialog dialog;
    private NumberPicker numberPicker;
    private CompDrawingFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comp_comp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button play = (Button) findViewById(R.id.play);
        Logic.setButtonBlueBackground(findViewById(R.id.play));

        dialog = new ProgressDialog(CompComp.this);
        dialog.setTitle(getResources().getString(R.string.drawing_process));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setMaxValue(Logic.NUMBERS_AMOUNT);
        numberPicker.setMinValue(Logic.MIN_SATISFACTION_NUMBERS_AMOUNT);

        if(savedInstanceState == null) {
            fragment = new CompDrawingFragment();
            getSupportFragmentManager().beginTransaction().add(fragment, TAG_TASK_FRAGMENT).commit();
        } else {
            fragment = (CompDrawingFragment) getSupportFragmentManager().findFragmentByTag(TAG_TASK_FRAGMENT);
            numberPicker.setValue(savedInstanceState.getInt(STATE_NUM_PICKER));
        }

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawingCancel();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                drawingCancel();
            }
        });


        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fragment != null) {
                    fragment.beginTask(numberPicker.getValue());
                    if (fragment.drawingCompComp != null && fragment.drawingCompComp.getStatus()== AsyncTask.Status.RUNNING) {
                        dialog.show();
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fragment != null) {
            if (fragment.drawingCompComp != null) {
                if (fragment.drawingCompComp.getStatus()== AsyncTask.Status.RUNNING) {
                    dialog.show();
                }
                Log.d("OnStart Draw Finished", Boolean.toString(fragment.drawingCompComp.getStatus() == AsyncTask.Status.FINISHED));
                if (fragment.drawingCompComp.getStatus() == AsyncTask.Status.FINISHED) {
                    Logic.PREF_ALL_NUMBERS_ENABLED = true;
                    Logic.setAllNumbersArray(null);
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fragment != null) {
            hideProgressDialog();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_NUM_PICKER, numberPicker.getValue());
        super.onSaveInstanceState(savedInstanceState);
    }


    public void showProgressDialog() {
        if (fragment.drawingCompComp != null) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public void hideProgressDialog() {
        if (fragment.drawingCompComp != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void updateProgress(String message) {
        dialog.setMessage(message);
    }

    public void startResultActivity(List<Integer> numbersToPass, List<Integer> drawnNumbersToPass, int whatYouWin,
                                    long howMuchToWin, int howMuchHits, int[] hits06) {

        Intent i = new Intent(getApplicationContext(), GameResultsComp.class);
        i.putExtra("numbers_to_pass", (Serializable) numbersToPass);
        i.putExtra("drawn_numbers_to_pass", (Serializable) drawnNumbersToPass);
        i.putExtra("what_you_win", whatYouWin);
        i.putExtra("how_much_to_win", howMuchToWin);
        i.putExtra("how_much_hits", howMuchHits);
        i.putExtra("hits_06", hits06);
        startActivity(i);

    }

    public void showToastOverLimit(int maxAllNumbersSize) {
        if (!Logic.PREF_ALL_NUMBERS_ENABLED) {
            TastyToast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.too_much_draws), Logic.numbersFormatter(maxAllNumbersSize)), Toast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    public void drawingCancel() {
        if (fragment != null) {
            if (fragment.drawingCompComp != null && fragment.drawingCompComp.getStatus()== AsyncTask.Status.RUNNING) {
                fragment.drawingCompComp.cancel(true);
            }
        }
    }

}
