package pl.maslanka.lottery;

import android.content.Intent;
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

import java.util.List;

/**
 * Created by Artur on 18.10.2016.
 */

public class GameResultsUserCash extends AppCompatActivity {

    private List<Integer> drawnNumbersToGet;
    private TextView[] drawnNumbersTvArray;
    private TableRow drawnNumbersTableRow;
    private TextView howManyBetsFromAmount;
    private int betAmount;
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
    private String prizeAmountResultText;
    private int settlementResult;
    private Button showDraws;
    private Button showWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_results_user_amount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawnNumbersTvArray = new TextView[Logic.NUMBERS_AMOUNT];
        hitsTvNumbers = new TextView[Logic.NUMBERS_AMOUNT + 1];
        hitsTvDescriptions = new TextView[Logic.NUMBERS_AMOUNT + 1];
        hitsTrNumbers = new TableRow[Logic.NUMBERS_AMOUNT + 1];
        prizeTvNumbers = new TextView[Logic.NUMBERS_AMOUNT - 2];
        prizeTrNumbers = new TableRow[Logic.NUMBERS_AMOUNT - 2];

        findViews();
        getExtras();


        Logic.setButtonBlueBackground(findViewById(R.id.show_wallet));

//        Log.i("log", drawnNumbersToGet.toString());

        createAndShowDrawnNumbers();
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
                String text1 = String.format(getResources().getString(R.string.share_message_bet), Logic.numbersFormatter(betAmount), Logic.numbersFormatter(betAmount * Logic.BET_COST), prizeAmountResultText);
                StringBuilder betAmountText = new StringBuilder();
                for(int i = 0; i < Logic.NUMBERS_AMOUNT+1; i++) {
                    betAmountText.append(Logic.numbersFormatter(hitsNumbers[i]));
                    betAmountText.append(MainActivity.logic.getHitsDescriptions()[i]);
                    betAmountText.append("\n");
                }

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.i_won));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, text1
                        + "\n\n" + getResources().getString(R.string.share_message_bet_2) + drawnNumbersToGet.toString()
                        + "\n\n" + getResources().getString(R.string.share_message_bet_3)
                        + "\n\n" + betAmountText.toString()
                        + "\n" + getResources().getString(R.string.share_message_bet_4)
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
        drawnNumbersTableRow = (TableRow) findViewById(R.id.drawn_numbers_table_row);
        howManyBetsFromAmount = (TextView) findViewById(R.id.how_many_bets_from_amount);
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
        betAmount = intent.getIntExtra("bet_amount", betAmount);
        drawnNumbersToGet = (List<Integer>) intent.getSerializableExtra("drawn_numbers_to_pass");
        hitsNumbers = intent.getExtras().getIntArray("hits_06");
    }

    protected void createAndShowDrawnNumbers() {
        for(int i = 0; i < Logic.NUMBERS_AMOUNT; i++) {
            drawnNumbersTvArray[i] = (TextView) getLayoutInflater().inflate(R.layout.numbers_template, null);
            drawnNumbersTableRow.addView(drawnNumbersTvArray[i]);
            drawnNumbersTvArray[i].setText(drawnNumbersToGet.get(i).toString());
        }
    }

    protected void showGamesNumber() {
        howManyBetsFromAmount.setText(Logic.numbersFormatter(betAmount) + getResources().getString(R.string.how_many_bets_from_amount));
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
        costAmount.setText(Logic.numbersFormatter(betAmount * Logic.BET_COST) + getResources().getString(R.string.cost_amount));
    }

    protected void showPrizeAmount() {
        prizeAmountResult = (hitsNumbers[3]*Logic.prizeAmounts[3])
                + (hitsNumbers[4]*Logic.prizeAmounts[4])
                + (hitsNumbers[5]*Logic.prizeAmounts[5])
                + (hitsNumbers[6]*Logic.prizeAmounts[6]);
        prizeAmountResultText = Logic.numbersFormatter(prizeAmountResult);
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
        settlementResult = prizeAmountResult - (betAmount * Logic.BET_COST);
        settlement.setText(Logic.numbersFormatter(settlementResult) + getResources().getString(R.string.settlement));
    }

    protected void setWalletValue() {
        MainActivity.logic.getWallet().setValue(MainActivity.logic.getWallet().getValue() + settlementResult);
    }
}
