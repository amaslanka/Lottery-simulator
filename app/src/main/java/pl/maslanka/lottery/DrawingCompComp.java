package pl.maslanka.lottery;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artur on 31.10.2016.
 */

public class DrawingCompComp extends AsyncTask<Integer,TaskProgress,List<Integer>> {

    private List<Integer> numbersToPass;
    private List<Integer> drawnNumbersToPass;
    private Wallet backupWallet;
    private long howMuchToWin;
    private int howMuchHits;
    private int whatYouWin;
    private int maxAllNumbersSize;
    private int[] hits06;
    private boolean win;
    private double sleepTime;
    private Activity activity;

    public DrawingCompComp(Activity activity) {
        onAttach(activity);
    }

    public void onAttach(Activity activity) {
        this.activity = activity;
    }

    public void onDetach() {
        activity = null;
    }

    @Override
    protected void onPreExecute() {
        if (activity != null) {
            ((CompComp) activity).showProgressDialog();
        }
    }

    @Override
    protected List<Integer> doInBackground(Integer... ints) {

        win = false;
        maxAllNumbersSize = Logic.getMaxAllNumbersPref();
        whatYouWin = ints[0];
        backupWallet = MainActivity.logic.getWallet().deepCopy();
        Logic.setAllNumbersArray(new ArrayList<AllNumbers>());
        numbersToPass = new ArrayList<>();
        drawnNumbersToPass = new ArrayList<>();
        hits06 = new int[Logic.NUMBERS_AMOUNT + 1];

        howMuchToWin = 0;
        howMuchHits = 0;

        setSleepTime();

        //Draw number until win
        while (!win) {

            //Check if task is not cancelled
            if (!this.isCancelled()) {

                //Run drawing and check results
                MainActivity.logic.randomNumbers();
                MainActivity.logic.runTote();

                //Store results in AllNumbersList only if the limit preference value has not been exceeded
                storeResultsInAllNumbersList();

                //Increase ancillary variable
                howMuchToWin++;

                if (howMuchToWin == maxAllNumbersSize + 1)
                    Logic.PREF_ALL_NUMBERS_ENABLED = false;

                if (activity != null) {
                    publishProgress(new TaskProgress(howMuchToWin, activity));
                }


                //Thread sleep depending on estimated drawing time (for user experience purposes)
                try {
                    if(sleepTime < 1) {
                        Thread.sleep(0, (int) (sleepTime * 1000000));
                    } else {
                        Thread.sleep((int) sleepTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("Action stopped", "Drawing process stopped!");
                }


                //Check if this draw wins and if yes - add results to local Arrays and variables
                checkIfWinsAndCopyResults();

                //Clear Logic arrays and values
                MainActivity.logic.clearListAndHits();

            } else {
                //Restore wallet, clear arrays and cancel task if process was interrupted
                drawingStopped();
                break;
            }

        }

        //Clear lottery hits arrays
        MainActivity.logic.clearHitsAmounts();


        return numbersToPass;
    }

    @Override
    protected void onProgressUpdate(TaskProgress... progress) {
        if (activity == null) {
            Log.i("Drawing", "Skipping progress update");
        } else {
            ((CompComp) activity).updateProgress(progress[0].message);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("Method executed", "onCancelled()!");
    }


    @Override
    protected void onPostExecute(List<Integer> numbersToPass) {
        if (activity != null) {
            ((CompComp) activity).hideProgressDialog();
        }

        if (numbersToPass != null && activity != null) {
            ((CompComp) activity).showToastOverLimit(maxAllNumbersSize);
            ((CompComp) activity).startResultActivity(numbersToPass, drawnNumbersToPass, whatYouWin, howMuchToWin, howMuchHits, hits06);
        }

    }

    private void checkIfWinsAndCopyResults() {
        if (MainActivity.logic.getLotto().getHits() >= whatYouWin) {

            win = true;
            numbersToPass.addAll(MainActivity.logic.getNumbers());
            drawnNumbersToPass.addAll(MainActivity.logic.getLotto().getSorted());
            howMuchHits = MainActivity.logic.getLotto().getHits();

            System.arraycopy(MainActivity.logic.getLotto().getHits06(), 0, hits06, 0, hits06.length);

        }
    }

    private void storeResultsInAllNumbersList() {
        if (Logic.PREF_ALL_NUMBERS_ENABLED) {
            Logic.getAllNumbersArray().add(new AllNumbers());
            Logic.getAllNumbersArray().get((int) howMuchToWin).setDrawNumber((int) howMuchToWin);
            Logic.getAllNumbersArray().get((int) howMuchToWin).getYourNumbers().addAll(MainActivity.logic.getNumbers());
            Logic.getAllNumbersArray().get((int) howMuchToWin).setHits(MainActivity.logic.getLotto().getHits());
            Logic.getAllNumbersArray().get((int) howMuchToWin).getDrawnNumbers().addAll(MainActivity.logic.getLotto().getSorted());
        } else {
            Logic.setAllNumbersArray(null);
        }
    }

    private void setSleepTime() {
        if (whatYouWin > 4) {
            sleepTime = 0.08;
        } else if (whatYouWin == 4) {
            sleepTime = 1;
        } else if (whatYouWin == 3) {
            sleepTime = 20;
        }
    }

    private void drawingStopped() {
        MainActivity.logic.setWallet(backupWallet);
        Log.i("Loop break", "Backup wallet restored!");
        Logic.setAllNumbersArray(null);
        Logic.PREF_ALL_NUMBERS_ENABLED = true;
        numbersToPass = null;
        if (!this.isCancelled())
            this.cancel(true);
    }

}
