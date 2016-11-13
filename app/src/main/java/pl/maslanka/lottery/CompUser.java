package pl.maslanka.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artur on 11.10.2016.
 */

public class CompUser extends AppCompatActivity {

    private List<Integer> numbersToPass;
    private List<Integer> drawnNumbersToPass;
    private int howMuchHits;

    private Button oneShot;
    private Button betAmount;
    private Button cashAmount;
    private Button getUserNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comp_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViews();
        setButtonBackground();


        oneShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneShot();
            }
        });

        betAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BetAmount.class);
                startActivity(intent);
            }
        });

        cashAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), CashAmount.class);
                startActivity(intent);
            }
        });

        getUserNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), UserNumbers.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        numbersToPass = null;
        drawnNumbersToPass = null;
    }

    protected void findViews() {
        oneShot = (Button) findViewById(R.id.one_shot);
        betAmount = (Button) findViewById(R.id.bet_amount);
        cashAmount = (Button) findViewById(R.id.cash_amount);
        getUserNumbers = (Button) findViewById(R.id.get_user_numbers);
    }

    protected void setButtonBackground() {
        Logic.setButtonBlueBackground(findViewById(R.id.one_shot));
        Logic.setButtonBlueBackground(findViewById(R.id.bet_amount));
        Logic.setButtonBlueBackground(findViewById(R.id.cash_amount));
        Logic.setButtonBlueBackground(findViewById(R.id.get_user_numbers));
    }

    protected void oneShot() {

        numbersToPass = new ArrayList<>();
        drawnNumbersToPass = new ArrayList<>();
        howMuchHits = 0;

        MainActivity.logic.randomNumbers();
        MainActivity.logic.runTote();

        numbersToPass.addAll(MainActivity.logic.getNumbers());
        drawnNumbersToPass.addAll(MainActivity.logic.getLotto().getSorted());
        howMuchHits = MainActivity.logic.getLotto().getHits();

        MainActivity.logic.clearListAndHits();
        MainActivity.logic.clearHitsAmounts();

        if (numbersToPass != null) {
            Intent i = new Intent(getApplicationContext(), OneShot.class);
            i.putExtra("numbers_to_pass", (Serializable) numbersToPass);
            i.putExtra("drawn_numbers_to_pass", (Serializable) drawnNumbersToPass);
            i.putExtra("how_much_hits", howMuchHits);
            startActivity(i);
        }
    }

}
