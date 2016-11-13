package pl.maslanka.lottery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Artur on 11.10.2016.
 */

public class ShowWallet extends AppCompatActivity {

    private TextView walletStateAmount;
    private TextView[] hitsTvNumbers;
    private TextView[] hitsTvDescriptions;
    private TableRow[] hitsTrNumbers;
    private TableLayout hitsTableLayout;
    private Button clearWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hitsTvNumbers = new TextView[Logic.NUMBERS_AMOUNT + 1];
        hitsTvDescriptions = new TextView[Logic.NUMBERS_AMOUNT + 1];
        hitsTrNumbers = new TableRow[Logic.NUMBERS_AMOUNT + 1];

        findViews();
        showWalletStateAmount();
        createAndShowHitsNumbers();

        Logic.setButtonBlueBackground(findViewById(R.id.clear_wallet));

        clearWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearWallet();
            }
        });


    }

    protected void findViews() {
        walletStateAmount = (TextView) findViewById(R.id.wallet_state);
        hitsTableLayout = (TableLayout) findViewById(R.id.hits_table_layout);
        clearWallet = (Button) findViewById(R.id.clear_wallet);
    }

    protected void showWalletStateAmount() {
        walletStateAmount.setText(Logic.numbersFormatter((int) MainActivity.logic.getWallet().getValue()) + getResources().getString(R.string.wallet_state_2));
    }

    protected void createAndShowHitsNumbers() {
        for (int i = 0; i < Logic.NUMBERS_AMOUNT + 1; i++) {
            hitsTrNumbers[i] = (TableRow) getLayoutInflater().inflate(R.layout.hits_table_row_template, null);
            hitsTableLayout.addView(hitsTrNumbers[i]);

            hitsTvNumbers[i] = (TextView) getLayoutInflater().inflate(R.layout.hits_numb_template, null);
            hitsTvDescriptions[i] = (TextView) getLayoutInflater().inflate(R.layout.hits_desc_template, null);

            hitsTrNumbers[i].addView(hitsTvNumbers[i]);
            hitsTvNumbers[i].setText(Logic.numbersFormatter(MainActivity.logic.getWallet().getHits06()[i]));

            hitsTrNumbers[i].addView(hitsTvDescriptions[i]);
            hitsTvDescriptions[i].setText(MainActivity.logic.getHitsDescriptions()[i]);

        }
    }

    protected void clearWallet() {
        MainActivity.logic.getWallet().setValue(0);
        showWalletStateAmount();

        for (int i=0; i < Logic.NUMBERS_AMOUNT + 1; i++) {
            MainActivity.logic.getWallet().getHits06()[i] = 0;
            hitsTvNumbers[i].setText(Logic.numbersFormatter(MainActivity.logic.getWallet().getHits06()[i]));
            hitsTvDescriptions[i].setText(MainActivity.logic.getHitsDescriptions()[i]);
        }

        TastyToast.makeText(getApplicationContext(), getResources().getString(R.string.wallet_cleared), Toast.LENGTH_LONG, TastyToast.DEFAULT);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
