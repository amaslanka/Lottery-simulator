package pl.maslanka.lottery;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artur on 01.11.2016.
 */

public class DrawingBetAmount extends AsyncTask<Integer,Integer,List<Integer>> {

    private List<Integer> drawnNumbersToPass;
    private int[] hits06;
    private int maxAllNumbersSize;
    private int betNumber;
    private  double sleepTime;
    private Wallet backupWallet;
    private Activity activity;

    public DrawingBetAmount(Activity activity) {
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
            ((BetAmount) activity).showProgressDialog();

            maxAllNumbersSize = Logic.getMaxAllNumbersPref();
            ((BetAmount) activity).showToastOverLimit(maxAllNumbersSize);
        }

    }

    @Override
    protected List<Integer> doInBackground(Integer... ints) {

        betNumber = ints[0];
        sleepTime = 0;
        backupWallet = MainActivity.logic.getWallet().deepCopy();
        Logic.setAllNumbersArray(new ArrayList<AllNumbers>());
        drawnNumbersToPass = new ArrayList<>();
        hits06 = new int[Logic.NUMBERS_AMOUNT + 1];

        //Set sleep time depending on bet number (for user experience purposes)
        setSleepTime();

        //Generate Drawn Numbers
        generateDrawnNumbers();


        for(int i=0; i<betNumber; i++) {

            //Check if task is not cancelled
            if (!this.isCancelled()) {

                if (activity != null) {
                    publishProgress(i);
                }

                // long msBefore = System.currentTimeMillis();

                try {
                    if(sleepTime < 1) {
                        Thread.sleep(0, (int) (sleepTime * 1000000));
                    } else {
                        Thread.sleep((int) Math.round(sleepTime));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("Action stopped", "Drawing process stopped!");
                }

                // Log.i("time", "Time: " + Long.toString(System.currentTimeMillis() - msBefore));

                //Run drawing, check results and clear only your numbers (lottery numbers stays constant - program does not clear it)
                randomizeYourNumbersAndCheckResults();

                storeResultsInAllNumbersList(i);

                clearYourNumbers();


            }  else {
                //Restore wallet, clear arrays and cancel task if process was interrupted
                drawingStopped();
                break;
            }
        }


        //Get drawn numbers and hits amount and store in local arrays
        drawnNumbersToPass.addAll(MainActivity.logic.getLotto().getSorted());
        System.arraycopy(MainActivity.logic.getLotto().getHits06(), 0, hits06, 0, hits06.length);


        //Clear Logic arrays and values
        MainActivity.logic.clearListAndHits();
        MainActivity.logic.clearHitsAmounts();


        return drawnNumbersToPass;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (activity == null) {
            Log.i("Drawing", "Skipping progress update");
        } else {
            ((BetAmount) activity).updateProgress(progress[0]);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i("Method executed", "onCancelled()!");
    }


    @Override
    protected void onPostExecute(List<Integer> drawnNumbersToPass) {
        if (activity != null) {
            ((BetAmount) activity).hideProgressDialog();
        }

        if (drawnNumbersToPass != null && activity != null) {
            ((BetAmount) activity).startResultActivity(betNumber, drawnNumbersToPass, hits06);
        }

    }

    private void generateDrawnNumbers() {
        MainActivity.logic.getLotto().generate();
        MainActivity.logic.getLotto().randomize();
        MainActivity.logic.getLotto().sortResult();
    }


    private void setSleepTime() {
        if (betNumber < 30) {
            sleepTime = 96;
        } else if (betNumber < 100000){
            sleepTime = 400 * Math.pow(Math.E, (-2.996*((Math.log(betNumber/10))/2.3026)));
        } else if (betNumber >= 100000){
            sleepTime = 0.002497;
        }
    }

    public void randomizeYourNumbersAndCheckResults() {
        MainActivity.logic.randomNumbers();
        MainActivity.logic.getLotto().checkResult(MainActivity.logic.getNumbers());
        MainActivity.logic.sortYourNumbers();
        MainActivity.logic.getLotto().switchHits(MainActivity.logic.getWallet());
    }

    private void storeResultsInAllNumbersList(int i) {
        if (Logic.PREF_ALL_NUMBERS_ENABLED) {
            Logic.getAllNumbersArray().add(new AllNumbers());
            Logic.getAllNumbersArray().get(i).setDrawNumber(i);
            Logic.getAllNumbersArray().get(i).getYourNumbers().addAll(MainActivity.logic.getNumbers());
            Logic.getAllNumbersArray().get(i).setHits(MainActivity.logic.getLotto().getHits());
            Logic.getAllNumbersArray().get(i).getDrawnNumbers().addAll(MainActivity.logic.getLotto().getSorted());
        } else {
            Logic.setAllNumbersArray(null);
        }
    }

    private void clearYourNumbers() {
        MainActivity.logic.getNumbers().clear();
        MainActivity.logic.getLotto().setHits(0);
        MainActivity.logic.getLotto().setLoopNumber(0);
    }

    private void drawingStopped() {
        MainActivity.logic.setWallet(backupWallet);
        Logic.setAllNumbersArray(null);
        Logic.PREF_ALL_NUMBERS_ENABLED = true;
        Log.i("Loop break", "Backup wallet restored!");
        if (!this.isCancelled())
            this.cancel(true);
    }
}
