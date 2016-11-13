package pl.maslanka.lottery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Artur on 11.10.2016.
 */

public class Lotto implements Serializable {

    private List<Integer> numbers;
    private List<Integer> sorted;
    private int[] hits06;
    private int hits = 0;
    private int loopNumber = 0;


    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public List<Integer> getSorted() {
        return sorted;
    }

    public void setSorted(List<Integer> sorted) {
        this.sorted = sorted;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getLoopNumber() {
        return loopNumber;
    }

    public int[] getHits06() {
        return hits06;
    }

    public void setHits06(int[] hits06) {
        this.hits06 = hits06;
    }

    public void setLoopNumber(int loopNumber) {
        this.loopNumber = loopNumber;
    }


    public Lotto() {
        numbers = new ArrayList<>(49);
        sorted = new ArrayList<>(6);
        hits06 = new int[7];
        fillInHitsArray();
    }

    public void fillInHitsArray() {
        for(int i=0; i<7; i++) {
            hits06[i] = 0;
        }
    }

    public void generate() {
        for(int i=1; i<50; i++) {
            numbers.add(i);
        }
    }

    public void randomize() {
        Collections.shuffle(numbers);

    }

    public int checkResult(List<Integer> numbers) {

        if (loopNumber < 6) {
            for (int i = 0; i < 6; i++) {
                if (this.numbers.get(loopNumber) == numbers.get(i)) {
                    hits++;
                }
            }
            loopNumber++;
            checkResult(numbers);
        }

        return hits;
    }



    public void sortResult() {

        for(int i=0; i<6; i++) {
            sorted.add(numbers.get(i));
        }

        Collections.sort(sorted);
    }

    public void switchHits(Wallet wallet) {

        switch(hits) {
            case 0:
                hits06[0]++;
                wallet.getHits06()[0]++;
                break;
            case 1:
                hits06[1]++;
                wallet.getHits06()[1]++;
                break;
            case 2:
                hits06[2]++;
                wallet.getHits06()[2]++;
                break;
            case 3:
                hits06[3]++;
                wallet.getHits06()[3]++;
                break;
            case 4:
                hits06[4]++;
                wallet.getHits06()[4]++;
                break;
            case 5:
                hits06[5]++;
                wallet.getHits06()[5]++;
                break;
            case 6:
                hits06[6]++;
                wallet.getHits06()[6]++;
                break;
        }

    }

}
