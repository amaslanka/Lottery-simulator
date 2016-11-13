package pl.maslanka.lottery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class AllNumbers implements Serializable {

    private int drawNumber;
    private List<Integer> yourNumbers;
    private int hits;
    private List<Integer> drawnNumbers;

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public List<Integer> getYourNumbers() {
        return yourNumbers;
    }

    public void setYourNumbers(List<Integer> yourNumbers) {
        this.yourNumbers = yourNumbers;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public List<Integer> getDrawnNumbers() {
        return drawnNumbers;
    }

    public void setDrawnNumbers(List<Integer> drawnNumbers) {
        this.drawnNumbers = drawnNumbers;
    }

    public AllNumbers() {
        drawNumber = 0;
        yourNumbers = new ArrayList<>();
        hits = 0;
        drawnNumbers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "AllNumbers{" +
                "yourNumbers=" + yourNumbers +
                ", hits=" + hits +
                ", drawnNumbers=" + drawnNumbers +
                '}';
    }


    public static class AllNumbersHitsComparator implements Comparator<AllNumbers> {

        @Override
        public int compare(AllNumbers lhs, AllNumbers rhs) {
            if (lhs.getHits() < rhs.getHits()) {
                return 1;
            } else if (lhs.getHits() > rhs.getHits()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static class AllNumbersDrawComparator implements Comparator<AllNumbers> {

        @Override
        public int compare(AllNumbers lhs, AllNumbers rhs) {
            if (lhs.getDrawNumber() < rhs.getDrawNumber()) {
                return -1;
            } else if (lhs.getDrawNumber() > rhs.getDrawNumber()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
