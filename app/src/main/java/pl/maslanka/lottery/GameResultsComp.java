package pl.maslanka.lottery;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class GameResultsComp extends AppCompatActivity implements Serializable {

    private TextView extraWinMessage;
    private TextView winMessage;
    private int loopNumber;
    private int howMuchHits;
    private int whatYouWin;
    private List<Integer> yourNumbersToGet;
    private TextView[] yourNumbersTvArray;
    private TableRow yourNumbersTableRow;
    private List<Integer> drawnNumbersToGet;
    private TextView[] drawnNumbersTvArray;
    private TableRow drawnNumbersTableRow;
    private TextView howManyGamesNumber;
    private long howMuchToWin;
    private int[] hitsNumbers;
    private TextView[] hitsTvNumbers;
    private TextView[] hitsTvDescriptions;
    private TableRow[] hitsTrNumbers;
    private TableLayout hitsTableLayout;
    private TextView costAmount;
    private TextView prizeAmount;
    private TextView[] prizeTvNumbers;
    private TableRow[] prizeTrNumbers;
    private TableLayout prizeTableLayout;
    private TextView settlement;
    private int prizeAmountResult;
    private int settlementResult;
    private Button showDraws;
    private Button showWallet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_results_comp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loopNumber = 0;
        yourNumbersTvArray = new TextView[Logic.NUMBERS_AMOUNT];
        drawnNumbersTvArray = new TextView[Logic.NUMBERS_AMOUNT];
        hitsTvNumbers = new TextView[Logic.NUMBERS_AMOUNT + 1];
        hitsTvDescriptions = new TextView[Logic.NUMBERS_AMOUNT + 1];
        hitsTrNumbers = new TableRow[Logic.NUMBERS_AMOUNT + 1];
        prizeTvNumbers = new TextView[Logic.NUMBERS_AMOUNT - 2];
        prizeTrNumbers = new TableRow[Logic.NUMBERS_AMOUNT - 2];

        findViews();
        getExtras();


        Logic.setButtonBlueBackground(findViewById(R.id.show_wallet));

//        Log.i("what_you_win", Long.toString(whatYouWin));
//        Log.i("how_much_to_win_result", Long.toString(howMuchToWin));
//        Log.i("yourNumbersToGet", yourNumbersToGet.toString());
//        Log.i("drawnNumbersToGet", drawnNumbersToGet.toString());
//        for (int i=0; i<7; i++) {
//            Log.i("hits_numbers", Integer.toString(hitsNumbers[i]));
//        }

        showWinMessage();
        createAndShowYourNumbers();
        createAndShowDrawnNumbers();
        boldHits();
        showGamesNumber();
        createAndShowHitsNumbers();
        showCostAmount();
        showPrizeAmount();
        createAndShowPrizeNumbers();
        showSettlement();
        setWalletValue();

        if (Logic.PREF_ALL_NUMBERS_ENABLED) {
            showDraws.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), AllNumbersList.class);
                    startActivity(i);
                }
            });

            Logic.setButtonBlueBackground(findViewById(R.id.show_draws));
        } else {
            showDraws.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar changeLimitSnack = Snackbar.make(v, getString(R.string.all_numbers_list_unavailable), Snackbar.LENGTH_LONG).setAction(getString(R.string.change_limit), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent p = new Intent(getApplicationContext(), Prefs.class);
                            startActivity(p);
                        }
                    });
                    changeLimitSnack.setActionTextColor(getResources().getColor(R.color.colorAccent));
                    changeLimitSnack.show();
                }
            });
            Logic.setButtonGreyBackgroundApiBelow21(findViewById(R.id.show_draws));
        }


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
        inflater.inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.i_won));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.share_message_1), Integer.toString(whatYouWin))
                        + "\n\n" + getResources().getString(R.string.share_message_2) + yourNumbersToGet.toString()
                        + "\n" + getResources().getString(R.string.share_message_3) + drawnNumbersToGet.toString()
                        + "\n\n" + getResources().getString(R.string.share_message_4)
                        + "\n\n" + getResources().getString(R.string.link_to_app));
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void findViews() {
        extraWinMessage = (TextView) findViewById(R.id.extra_win_message);
        winMessage = (TextView) findViewById(R.id.win_message);
        yourNumbersTableRow = (TableRow) findViewById(R.id.your_numbers_table_row);
        drawnNumbersTableRow = (TableRow) findViewById(R.id.drawn_numbers_table_row);
        howManyGamesNumber = (TextView) findViewById(R.id.how_many_games_number);
        hitsTableLayout = (TableLayout) findViewById(R.id.hits_table_layout);
        costAmount = (TextView) findViewById(R.id.cost_amount);
        prizeAmount = (TextView) findViewById(R.id.prize_amount);
        prizeTableLayout = (TableLayout) findViewById(R.id.prize_table_layout);
        settlement = (TextView) findViewById(R.id.settlement);
        showDraws = (Button) findViewById(R.id.show_draws);
        showWallet = (Button) findViewById(R.id.show_wallet);
    }

    protected void getExtras() {
        Intent intent =  getIntent();
        whatYouWin = intent.getIntExtra("what_you_win", whatYouWin);
        howMuchHits = intent.getIntExtra("how_much_hits", howMuchHits);
        yourNumbersToGet = (List<Integer>) intent.getSerializableExtra("numbers_to_pass");
        drawnNumbersToGet = (List<Integer>) intent.getSerializableExtra("drawn_numbers_to_pass");
        howMuchToWin = intent.getLongExtra("how_much_to_win", howMuchToWin);
        hitsNumbers = intent.getExtras().getIntArray("hits_06");
    }

    protected void showWinMessage() {

        if(howMuchHits > whatYouWin) {

            extraWinMessage.setText(getResources().getString(R.string.extra_win_message_1) + whatYouWin
                    + getResources().getString(R.string.extra_win_message_2) + howMuchHits
                    + getResources().getString(R.string.extra_win_message_3));

            winMessage.setVisibility(View.GONE);

        } else {

            extraWinMessage.setVisibility(View.GONE);

            switch (howMuchHits) {
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

    public void boldHits() {
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

    protected void showGamesNumber() {
        howManyGamesNumber.setText(Logic.numbersFormatter((int) howMuchToWin) + getResources().getString(R.string.how_many_games_number));
    }

    protected void createAndShowHitsNumbers() {
        for(int i = 0; i < Logic.NUMBERS_AMOUNT+1; i++) {
            hitsTrNumbers[i] = (TableRow) getLayoutInflater().inflate(R.layout.hits_table_row_template, null);
            hitsTableLayout.addView(hitsTrNumbers[i]);

                hitsTvNumbers[i] = (TextView) getLayoutInflater().inflate(R.layout.hits_numb_template, null);
                hitsTvDescriptions[i] = (TextView) getLayoutInflater().inflate(R.layout.hits_desc_template, null);

                hitsTrNumbers[i].addView(hitsTvNumbers[i]);
                hitsTvNumbers[i].setText(Logic.numbersFormatter(hitsNumbers[i]));

                hitsTrNumbers[i].addView(hitsTvDescriptions[i]);
                hitsTvDescriptions[i].setText(MainActivity.logic.getHitsDescriptions()[i]);

        }

    }

    protected void showCostAmount() {
        costAmount.setText(Logic.numbersFormatter((int)howMuchToWin * Logic.BET_COST) + " " + getResources().getString(R.string.cost_amount));
    }

    protected void showPrizeAmount() {
        prizeAmountResult = (hitsNumbers[3]*Logic.prizeAmounts[3])
                + (hitsNumbers[4]*Logic.prizeAmounts[4])
                + (hitsNumbers[5]*Logic.prizeAmounts[5])
                + (hitsNumbers[6]*Logic.prizeAmounts[6]);
        String prizeAmountResultText = Logic.numbersFormatter(prizeAmountResult);
        prizeAmount.setText(prizeAmountResultText + getResources().getString(R.string.prize_amount));
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

    protected void showSettlement() {
        settlementResult = prizeAmountResult - ((int)howMuchToWin * Logic.BET_COST);
        settlement.setText(Logic.numbersFormatter(settlementResult) + getResources().getString(R.string.settlement));
    }

    protected void setWalletValue() {
        MainActivity.logic.getWallet().setValue(MainActivity.logic.getWallet().getValue() + settlementResult);
    }



}
