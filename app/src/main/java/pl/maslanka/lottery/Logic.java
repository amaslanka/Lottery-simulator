package pl.maslanka.lottery;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Artur on 11.10.2016.
 */

public class Logic implements Serializable {

    protected final static int MIN_SATISFACTION_NUMBERS_AMOUNT = 3;
    protected final static int NUMBERS_AMOUNT = 6;
    protected final static int BET_COST = 3;
    protected final static int MAX_BET_AMOUNT = 100000000;
    protected final static BigDecimal MIN_LIMIT_MAX_ALL_NUMBERS_SIZE = new BigDecimal("10");
    protected final static BigDecimal MAX_LIMIT_MAX_ALL_NUMBERS_SIZE = new BigDecimal("200000");
    protected final static int DEFAULT_MAX_ALL_NUMBERS_SIZE = 50000;
    protected final static int DEFAULT_ANIMATION_SLIDE_IN = 14;
    protected static boolean PREF_ALL_NUMBERS_ENABLED = true;
    private static Context staticContext;
    private static List<AllNumbers> allNumbersArray;
    private Context context;
    private List<Integer> numbers;
    private Lotto lotto;
    private Wallet wallet;
    private static transient PorterDuffColorFilter filter;
    protected static int[] prizeAmounts;
    private String[] hitsDescriptions;

    protected static List<AllNumbers> getAllNumbersArray() {
        return allNumbersArray;
    }

    protected static void setStaticContext(Context context) {
        staticContext = context;
    }

    protected static Context getStaticContext() {
        return staticContext;
    }

    public static void setAllNumbersArray(List<AllNumbers> allNumbersArray) {
        Logic.allNumbersArray = allNumbersArray;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public Lotto getLotto() {
        return lotto;
    }

    public String[] getHitsDescriptions() {
        return hitsDescriptions;
    }

    public void setHitsDescriptions(String[] hitsDescriptions) {
        this.hitsDescriptions = hitsDescriptions;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Logic(Context current) {
        this.context = current;
        numbers = new ArrayList<>();
        lotto = new Lotto();
        filter = new PorterDuffColorFilter(Color.rgb(63, 81, 181), PorterDuff.Mode.SRC_ATOP);
        hitsDescriptions = new String[Logic.NUMBERS_AMOUNT + 1];
    }

    public void newWallet() {
        wallet = new Wallet();
    }

    public void randomNumbers() {

        Random generator = new Random();
        int howMuch = 0;
        int number = 0;

        while(howMuch < 6) {
            number = generator.nextInt(50);

            if (number > 0 && number < 50) {

                Collections.sort(numbers);
                int index = Collections.binarySearch(numbers, number);

                if (howMuch == 0 || index < 0) {
                    numbers.add(number);
                    howMuch++;
                }
            }
        }
    }

    public void sortYourNumbers() {
        Collections.sort(numbers);
    }

    public void runTote() {
        lotto.generate();
        lotto.randomize();
        lotto.checkResult(numbers);
        sortYourNumbers();
        lotto.sortResult();
        lotto.switchHits(wallet);
    }

    public void clearListAndHits() {
        numbers.clear();
        lotto.getNumbers().clear();
        lotto.getSorted().clear();
        lotto.setHits(0);
        lotto.setLoopNumber(0);
    }

    public void clearHitsAmounts() {

        for(int i=0; i<7; i++) {
            lotto.getHits06()[i] = 0;
        }

    }

    protected void setValuesToHitsDescriptionsArray() {
        hitsDescriptions[0] = context.getResources().getString(R.string.no_hits);
        hitsDescriptions[1] = context.getResources().getString(R.string.one_hit);
        hitsDescriptions[2] = context.getResources().getString(R.string.two_hits);
        hitsDescriptions[3] = context.getResources().getString(R.string.three_hits);
        hitsDescriptions[4] = context.getResources().getString(R.string.four_hits);
        hitsDescriptions[5] = context.getResources().getString(R.string.five_hits);
        hitsDescriptions[6] = context.getResources().getString(R.string.six_hits);
    }

    public static void setButtonBlueBackground(View v) {
        v.setBackgroundResource(R.drawable.ripple_effect);
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable a = v.getBackground();
            a.setColorFilter(Logic.filter);
        }
    }

    public static void setButtonGreyBackgroundApiBelow21(View v) {
        if (Build.VERSION.SDK_INT < 21) {
            v.setBackgroundResource(R.drawable.ripple_effect_disable);
        }
    }

    public static String numbersFormatter(int number) {
        return (String.format("%,d", number)).replace(',', ' ');
    }

    public static void setPrizeAmounts(int prize3, int prize4, int prize5, int prize6) {
        prizeAmounts = new int[Logic.NUMBERS_AMOUNT + 1];

        for(int i=0; i<3; i++) {
            prizeAmounts[i] = 0;
        }
        prizeAmounts[3] = prize3;
        prizeAmounts[4] = prize4;
        prizeAmounts[5] = prize5;
        prizeAmounts[6] = prize6;
    }

    public static int getMaxAllNumbersPref() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(Logic.getStaticContext());
        int value = prefs.getInt(PreferencesFragment.KEY_MAX_LIST_SIZE, -1);
        return value == -1 ? DEFAULT_MAX_ALL_NUMBERS_SIZE : value;

    }

    public static int getAnimationPref() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(Logic.getStaticContext());
        int value = Integer.parseInt(prefs.getString(PreferencesFragment.KEY_ANIMATION_PREF, "-1"));
        return value == -1 ? DEFAULT_ANIMATION_SLIDE_IN : value;

    }


}
