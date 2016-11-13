package pl.maslanka.lottery;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

/**
 * Created by Artur on 18.10.2016.
 */

public class OneShot extends AppCompatActivity {

    private TextView winMessage;
    private int loopNumber;
    private int howMuchHits;
    private List<Integer> yourNumbersToGet;
    private TextView[] yourNumbersTvArray;
    private TableRow yourNumbersTableRow;
    private List<Integer> drawnNumbersToGet;
    private TextView[] drawnNumbersTvArray;
    private TableRow drawnNumbersTableRow;
    private TextView[] prizeTvNumbers;
    private TableRow[] prizeTrNumbers;
    private TableLayout prizeTableLayout;
    private TextView prizeAmount;
    private Button showWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_results_user_one_draw);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loopNumber = 0;
        yourNumbersTvArray = new TextView[Logic.NUMBERS_AMOUNT];
        drawnNumbersTvArray = new TextView[Logic.NUMBERS_AMOUNT];
        prizeTvNumbers = new TextView[Logic.NUMBERS_AMOUNT - 2];
        prizeTrNumbers = new TableRow[Logic.NUMBERS_AMOUNT - 2];

        findViews();
        getExtras();

        Logic.setButtonBlueBackground(findViewById(R.id.show_wallet));

//        Log.i("yourNumbersToGet", yourNumbersToGet.toString());
//        Log.i("drawnNumbersToGet", drawnNumbersToGet.toString());
//        Log.i("howMuchHits", Integer.toString(howMuchHits));

        showWinMessage();
        createAndShowYourNumbers();
        createAndShowDrawnNumbers();
        boldHits();
        createAndShowPrizeNumbers();
        showPrizeAmount();
        setWalletValue();
        loopNumber = 0;

        showWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShowWallet.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_one_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.redraw:
                redraw();
                TastyToast.makeText(this, getResources().getString(R.string.redrawed), Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                return true;
            case R.id.share:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.i_won));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.share_message_1), Integer.toString(howMuchHits))
                        + "\n\n" + getResources().getString(R.string.share_message_2) + yourNumbersToGet.toString()
                        + "\n" + getResources().getString(R.string.share_message_3) + drawnNumbersToGet.toString()
                        + "\n\n" + getResources().getString(R.string.share_message_4)
                        + "\n\n" + getResources().getString(R.string.link_to_app));
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void findViews() {
        winMessage = (TextView) findViewById(R.id.win_message);
        yourNumbersTableRow = (TableRow) findViewById(R.id.your_numbers_table_row);
        drawnNumbersTableRow = (TableRow) findViewById(R.id.drawn_numbers_table_row);
        prizeTableLayout = (TableLayout) findViewById(R.id.prize_table_layout);
        prizeAmount = (TextView) findViewById(R.id.prize_amount);
        showWallet = (Button) findViewById(R.id.show_wallet);
    }

    protected void getExtras() {
        Intent intent =  getIntent();
        howMuchHits = intent.getIntExtra("how_much_hits", howMuchHits);
        yourNumbersToGet = (List<Integer>) intent.getSerializableExtra("numbers_to_pass");
        drawnNumbersToGet = (List<Integer>) intent.getSerializableExtra("drawn_numbers_to_pass");
    }

    protected void showWinMessage() {

        switch (howMuchHits) {
            case 0:
                winMessage.setText(getResources().getString(R.string.win_message_zero));
                break;
            case 1:
                winMessage.setText(getResources().getString(R.string.win_message_one));
                break;
            case 2:
                winMessage.setText(getResources().getString(R.string.win_message_two));
                break;
            case 3:
                winMessage.setText(getResources().getString(R.string.win_message) + getResources().getString(R.string.win_message_three));
                break;
            case 4:
                winMessage.setText(getResources().getString(R.string.win_message) + getResources().getString(R.string.win_message_four));
                break;
            case 5:
                winMessage.setText(getResources().getString(R.string.win_message) + getResources().getString(R.string.win_message_five));
                break;
            case 6:
                winMessage.setText(getResources().getString(R.string.win_message) + getResources().getString(R.string.win_message_six));
                break;
        }

    }

    protected void createAndShowYourNumbers() {
        for(int i = 0; i < Logic.NUMBERS_AMOUNT; i++) {
            yourNumbersTvArray[i] = (TextView) getLayoutInflater().inflate(R.layout.numbers_template, null);
            yourNumbersTableRow.addView(yourNumbersTvArray[i]);
            yourNumbersTvArray[i].setText(yourNumbersToGet.get(i).toString());

        }
    }

    protected void createAndShowDrawnNumbers() {
        for(int i = 0; i < Logic.NUMBERS_AMOUNT; i++) {
            drawnNumbersTvArray[i] = (TextView) getLayoutInflater().inflate(R.layout.numbers_template, null);
            drawnNumbersTableRow.addView(drawnNumbersTvArray[i]);
            drawnNumbersTvArray[i].setText(drawnNumbersToGet.get(i).toString());
        }
    }

    protected void boldHits() {
        if (loopNumber < 6) {
            for (int i = 0; i < 6; i++) {
                if (drawnNumbersToGet.get(loopNumber) == yourNumbersToGet.get(i)) {
                    drawnNumbersTvArray[loopNumber].setTypeface(null, Typeface.BOLD);
                    yourNumbersTvArray[i].setTypeface(null, Typeface.BOLD);
                }
            }
            loopNumber++;
            boldHits();
        }
    }

    protected void createAndShowPrizeNumbers() {
        for(int i=0; i < Logic.NUMBERS_AMOUNT - 2; i++) {
            prizeTrNumbers[i] = (TableRow) getLayoutInflater().inflate(R.layout.hits_table_row_template, null);
            prizeTableLayout.addView(prizeTrNumbers[i]);

            prizeTvNumbers[i] = (TextView) getLayoutInflater().inflate(R.layout.prize_textview_template, null);
            prizeTrNumbers[i].addView(prizeTvNumbers[i]);
        }

        prizeTvNumbers[0].setText(Logic.numbersFormatter(Logic.prizeAmounts[3]) + getResources().getString(R.string.three_prize));
        prizeTvNumbers[1].setText(Logic.numbersFormatter(Logic.prizeAmounts[4]) + getResources().getString(R.string.four_prize));
        prizeTvNumbers[2].setText(Logic.numbersFormatter(Logic.prizeAmounts[5]) + getResources().getString(R.string.five_prize));
        prizeTvNumbers[3].setText(Logic.numbersFormatter(Logic.prizeAmounts[6]) + getResources().getString(R.string.six_prize));

    }

    protected void showPrizeAmount() {
        String prizeAmountResultText = Logic.numbersFormatter(Logic.prizeAmounts[howMuchHits]);
        prizeAmount.setText(prizeAmountResultText + getResources().getString(R.string.prize_amount));
    }

    protected void setWalletValue() {
        MainActivity.logic.getWallet().setValue(MainActivity.logic.getWallet().getValue() + (Logic.prizeAmounts[howMuchHits] - Logic.BET_COST));
    }

    protected void redraw() {
        //Clear Arrays and variables
        yourNumbersToGet.clear();
        drawnNumbersToGet.clear();
        howMuchHits = 0;

        //Run drawing and check results
        MainActivity.logic.randomNumbers();
        MainActivity.logic.runTote();

        //Add results to local Arrays and variables
        yourNumbersToGet.addAll(MainActivity.logic.getNumbers());
        drawnNumbersToGet.addAll(MainActivity.logic.getLotto().getSorted());
        howMuchHits = MainActivity.logic.getLotto().getHits();

        //Clear Logic arrays and values
        MainActivity.logic.clearListAndHits();
        MainActivity.logic.clearHitsAmounts();

        //Refresh view
        showWinMessage();

        for(int i = 0; i < Logic.NUMBERS_AMOUNT; i++) {
            yourNumbersTvArray[i].setText(yourNumbersToGet.get(i).toString());
        }

        for(int i = 0; i < Logic.NUMBERS_AMOUNT; i++) {
            drawnNumbersTvArray[i].setText(drawnNumbersToGet.get(i).toString());
        }

        for (int i = 0; i < 6; i++) {
            drawnNumbersTvArray[i].setTypeface(null, Typeface.NORMAL);
            yourNumbersTvArray[i].setTypeface(null, Typeface.NORMAL);
            }

        boldHits();
        showPrizeAmount();
        setWalletValue();

        //Clear variable
        loopNumber = 0;
    }
}
