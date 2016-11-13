package pl.maslanka.lottery;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Artur on 21.10.2016.
 */

public class AllNumbersListAdapter extends BaseAdapter implements SectionIndexer {

    private List<AllNumbers> allNumbersArray;
    private Activity activity;
    private Integer[] sections;
    private Integer[] elementsIndex;


    public List<AllNumbers> getAllNumbers() {
        return allNumbersArray;
    }

    public void setAllNumbers(List<AllNumbers> allNumbers) {
        this.allNumbersArray = allNumbers;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public AllNumbersListAdapter(List<AllNumbers> allNumbers, Activity activity) {
        this.allNumbersArray = allNumbers;
        this.activity = activity;

    }

    public AllNumbersListAdapter(List<AllNumbers> allNumbers, Activity activity, TreeSet<Integer> hitsSet, TreeSet<Integer> elementsIndexSet) {
        this.allNumbersArray = allNumbers;
        this.activity = activity;

        sections = hitsSet.toArray(new Integer[hitsSet.size()]);
        elementsIndex = elementsIndexSet.toArray(new Integer[elementsIndexSet.size()]);

        Arrays.sort(sections, new Comparator<Integer>() {
            @Override
            public int compare(Integer x, Integer y) {
                return y-x;
            }
        });

        for (int i=0; i<sections.length; i++) {
            Log.d("section element", Integer.toString(sections[i]));
        }

        for (int i=0; i<elementsIndex.length; i++) {
            Log.d("elementsIndex", Integer.toString(elementsIndex[i]));
        }

    }

    @Override
    public int getCount() {
        return allNumbersArray.size();
    }

    @Override
    public Object getItem(int position) {
        return allNumbersArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.all_numbers_list_item, parent, false);
        }

        TextView drawNumber = (TextView) convertView.findViewById(R.id.draw_number);
        TextView yourNumbersText = (TextView) convertView.findViewById(R.id.your_numbers_text);
        TextView yourNumbers = (TextView) convertView.findViewById(R.id.your_numbers);
        TextView drawnNumbersText = (TextView) convertView.findViewById(R.id.drawn_numbers_text);
        TextView drawnNumbers = (TextView) convertView.findViewById(R.id.drawn_numbers);
        TextView hitsText = (TextView) convertView.findViewById(R.id.hits_text);
        TextView hits = (TextView) convertView.findViewById(R.id.hits);

        StringBuilder yourNumbersString = new StringBuilder();
        for (int i=0; i<Logic.NUMBERS_AMOUNT; i++) {
            if (allNumbersArray.get(position).getYourNumbers().get(i) < 10) {
                yourNumbersString.append("  ");
                yourNumbersString.append(allNumbersArray.get(position).getYourNumbers().get(i));
                yourNumbersString.append("  ");
            } else {
                yourNumbersString.append(allNumbersArray.get(position).getYourNumbers().get(i));
                yourNumbersString.append("  ");
            }
        }

        StringBuilder drawnNumbersString = new StringBuilder();
        for (int i=0; i<Logic.NUMBERS_AMOUNT; i++) {
            if (allNumbersArray.get(position).getDrawnNumbers().get(i) < 10) {
                drawnNumbersString.append("  ");
                drawnNumbersString.append(allNumbersArray.get(position).getDrawnNumbers().get(i));
                drawnNumbersString.append("  ");
            } else {
                drawnNumbersString.append(allNumbersArray.get(position).getDrawnNumbers().get(i));
                drawnNumbersString.append("  ");
            }
        }

        drawNumber.setText(getActivity().getResources().getString(R.string.drawing_nr) + Logic.numbersFormatter(allNumbersArray.get(position).getDrawNumber()+1));
        yourNumbersText.setText(getActivity().getResources().getString(R.string.your_numbers_text2));
        yourNumbers.setText(yourNumbersString);
        drawnNumbersText.setText(getActivity().getResources().getString(R.string.drawn_numbers_text2));
        drawnNumbers.setText(drawnNumbersString);
        hitsText.setText(getActivity().getResources().getString(R.string.hits_text));
        hits.setText("  " + Integer.toString(allNumbersArray.get(position).getHits()));


        return convertView;
    }

    @Override
    public Object[] getSections() {

        if (sections != null && elementsIndex != null) {
            return sections;
        }

        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {

        if (sections != null && elementsIndex != null) {

            for(int i=0; i<sections.length; i++) {
                if (sectionIndex == i) {
                    sectionIndex = elementsIndex[i];
                    return sectionIndex;
                }
            }
        }

       return 0;
    }

    @Override
    public int getSectionForPosition(int position) {

        if (sections != null && elementsIndex != null) {
            int section;

            for(int i=0; i<elementsIndex.length; i++) {
                if (position <= elementsIndex[i]) {
                    section = i;
                    return section;
                }

                if (i==(elementsIndex.length-1)) {
                    section = i;
                    return section;
                }
            }
        }

        return 0;
    }
}
