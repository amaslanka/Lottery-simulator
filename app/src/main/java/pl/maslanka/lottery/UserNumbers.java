package pl.maslanka.lottery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TableRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Artur on 18.10.2016.
 */

public class UserNumbers extends AppCompatActivity implements DialogInterface.OnClickListener {

    private TableRow numberPickerTableRow;
    private NumberPicker[] numberPickerArray;
    private List<Integer> numberPickerValues;
    private List<Integer> drawnNumbersToPass;
    private int howMuchHits;
    private boolean repeated;
    private Drawing drawing;
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_numbers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        numberPickerArray = new NumberPicker[Logic.NUMBERS_AMOUNT];

        findViews();
        Logic.setButtonBlueBackground(findViewById(R.id.play));
        createAndShowNumberPickers();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateNumbersAndAddToArray();

                if (repeated == false && numberPickerValues.size() == 6) {
                    drawing = new Drawing();
                    drawing.execute(numberPickerValues);
                } else {
                    infoDialog(v);
                    numberPickerValues = null;
                }


            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        numberPickerValues = null;
        drawnNumbersToPass = null;
    }

    protected void findViews() {
        numberPickerTableRow = (TableRow) findViewById(R.id.number_picker_table_row);
        play = (Button) findViewById(R.id.play);
    }

    protected void createAndShowNumberPickers() {
        for(int i = 0; i < Logic.NUMBERS_AMOUNT; i++) {
            numberPickerArray[i] = (NumberPicker) getLayoutInflater().inflate(R.layout.number_picker_template, null);
            numberPickerTableRow.addView(numberPickerArray[i]);
            numberPickerArray[i].setMinValue(1);
            numberPickerArray[i].setMaxValue(49);
        }
    }

    protected void validateNumbersAndAddToArray() {
        numberPickerValues = new ArrayList<>();
        repeated = false;

        for(int i = 0; i < Logic.NUMBERS_AMOUNT; i++) {

            Collections.sort(numberPickerValues);
            int index = Collections.binarySearch(numberPickerValues, numberPickerArray[i].getValue());

            if (index < 0) {
                numberPickerValues.add(numberPickerArray[i].getValue());
                numberPickerArray[i].setBackgroundColor(Color.alpha(0));
            } else {
                numberPickerArray[i].setBackgroundResource(R.drawable.num_pick_warn);
                repeated = true;
            }
        }
    }

    protected void infoDialog (View view) {
        UserNumbersAlertDialogFragment alertDialog = new UserNumbersAlertDialogFragment();
        alertDialog.show(getFragmentManager(), null);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }


    private class Drawing extends AsyncTask<List<Integer>,Integer,List<Integer>> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(UserNumbers.this);
            dialog.setTitle(getResources().getString(R.string.drawing_process));
            dialog.setMessage(getResources().getString(R.string.simulating_lottery));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawing.cancel(true);
                }
            });
            dialog.show();

        }

        @Override
        protected List<Integer> doInBackground(List<Integer>... ints) {

            howMuchHits = 0;
            double sleepTime = 1500;
            drawnNumbersToPass = new ArrayList<>();

            Collections.sort(numberPickerValues);
            MainActivity.logic.getNumbers().addAll(numberPickerValues);
            MainActivity.logic.runTote();

            drawnNumbersToPass.addAll(MainActivity.logic.getLotto().getSorted());
            howMuchHits = MainActivity.logic.getLotto().getHits();

            MainActivity.logic.clearListAndHits();
            MainActivity.logic.clearHitsAmounts();

            try {
                Thread.sleep((int) sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            return drawnNumbersToPass;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            dialog.setProgress(progress[0]);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected void onPostExecute(List<Integer> drawnNumbersToPass) {
            dialog.dismiss();
            if (drawnNumbersToPass != null) {
                Intent i = new Intent(getApplicationContext(), GameResultsUserNumbers.class);
                i.putExtra("numbers_to_pass", (Serializable) numberPickerValues);
                i.putExtra("drawn_numbers_to_pass", (Serializable) drawnNumbersToPass);
                i.putExtra("how_much_hits", howMuchHits);
                startActivity(i);
            }
        }
    }


}
