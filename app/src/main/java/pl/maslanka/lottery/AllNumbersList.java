package pl.maslanka.lottery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.twotoasters.jazzylistview.JazzyListView;

import java.util.Collections;
import java.util.TreeSet;

/**
 * Created by Artur on 21.10.2016.
 */

public class AllNumbersList extends AppCompatActivity {

    private static final String FIRST_VISIBLE_POSITION_KEY = "first_visible_position";
    private static final String TOP_POSITION_KEY = "top_position";
    private static final String FIRST_RUN_KEY = "first_run";
    private static final String SORT_BY_HITS_KEY = "sort_by_hits";
    private int firstVisible;
    private int top;
    private boolean firstRun;
    private boolean sortByHits;
    private ListView allNumbersList;
    private JazzyListView allNumbersJazzyList;
    private AllNumbersListAdapter adapter;
    private TreeSet<Integer> hitsSet;
    private TreeSet<Integer> elementIndexSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setViewAndRefresh();

        if(savedInstanceState == null) {
            firstRun = true;
        } else {
            firstRun = savedInstanceState.getBoolean(FIRST_RUN_KEY);
            sortByHits = savedInstanceState.getBoolean(SORT_BY_HITS_KEY);
            firstVisible = savedInstanceState.getInt(FIRST_VISIBLE_POSITION_KEY);
            top = savedInstanceState.getInt(TOP_POSITION_KEY);
        }

        beginTask();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all_numbers_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.sort:
                sortByHits = !sortByHits;
                setListViewParameters();
                setViewAndRefresh();
                beginTask();
                return true;
            case R.id.preferences:
                setListViewParameters();
                Intent p = new Intent(getApplicationContext(), Prefs.class);
                startActivity(p);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("onRestart", "executed");
        setViewAndRefresh();
        beginTask();
    }

    protected void setViewAndRefresh() {
        setContentView(R.layout.all_numbers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            allNumbersJazzyList = (JazzyListView) findViewById(R.id.all_numbers_jazzy_list);
            Log.d("anim_pref", Integer.toString(Logic.getAnimationPref()));
            allNumbersJazzyList.setTransitionEffect(Logic.getAnimationPref());
            allNumbersJazzyList.setFastScrollEnabled(true);
        } else {
            allNumbersList = (ListView) findViewById(R.id.all_numbers_list);
            allNumbersList.setFastScrollEnabled(true);
        }

    }

    protected void beginTask() {
        Listing listing = new Listing();
        listing.execute(this);
    }

    protected void restoreView() {
        if (Build.VERSION.SDK_INT >= 21) {
            allNumbersJazzyList.setSelectionFromTop(firstVisible, top);
        } else {
            allNumbersList.setSelectionFromTop(firstVisible, top);
        }
    }

    protected void setListViewParameters() {
        if (Build.VERSION.SDK_INT >= 21) {
            firstVisible = allNumbersJazzyList.getFirstVisiblePosition();
            View v = allNumbersJazzyList.getChildAt(0);
            top = (v == null) ? 0 : (v.getTop() - allNumbersJazzyList.getPaddingTop());
        } else {
            firstVisible = allNumbersList.getFirstVisiblePosition();
            View v = allNumbersList.getChildAt(0);
            top = (v == null) ? 0 : (v.getTop() - allNumbersList.getPaddingTop());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        setListViewParameters();
        savedInstanceState.putInt(FIRST_VISIBLE_POSITION_KEY, firstVisible);
        savedInstanceState.putInt(TOP_POSITION_KEY, top);
        savedInstanceState.putBoolean(FIRST_RUN_KEY, firstRun);
        savedInstanceState.putBoolean(SORT_BY_HITS_KEY, sortByHits);
        super.onSaveInstanceState(savedInstanceState);
    }


    private class Listing extends AsyncTask<Activity, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AllNumbersList.this);
            dialog.setTitle(getResources().getString(R.string.creating_list));
            dialog.setMessage(getResources().getString(R.string.creating_list_message));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }


        @Override
        protected Void doInBackground(Activity... params) {
            Activity activity = params[0];
            hitsSet = new TreeSet<>();
            elementIndexSet = new TreeSet<>();
            int hitsSetSizeToCompare;


            if (firstRun) {
                Log.d("AllNumbList_first_run", Boolean.toString(firstRun));
                Collections.sort(Logic.getAllNumbersArray(), new AllNumbers.AllNumbersDrawComparator());
                adapter = new AllNumbersListAdapter(Logic.getAllNumbersArray(), activity);
                sortByHits = false;
                firstRun = false;
                return null;
            }

            Log.d("AllNumList_first_run", Boolean.toString(firstRun));


            if (sortByHits) {

                Collections.sort(Logic.getAllNumbersArray(), new AllNumbers.AllNumbersHitsComparator());

                for (int i=0; i<Logic.getAllNumbersArray().size(); i++) {
                    hitsSetSizeToCompare = hitsSet.size();
                    hitsSet.add(Logic.getAllNumbersArray().get(i).getHits());

                    if (hitsSetSizeToCompare != hitsSet.size()) {
                        elementIndexSet.add(i);
                    }
                }
                adapter = new AllNumbersListAdapter(Logic.getAllNumbersArray(), activity, hitsSet, elementIndexSet);


            } else {

                Collections.sort(Logic.getAllNumbersArray(), new AllNumbers.AllNumbersDrawComparator());
                adapter = new AllNumbersListAdapter(Logic.getAllNumbersArray(), activity);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void parameter) {
            if (Build.VERSION.SDK_INT >= 21) {
                allNumbersJazzyList.setAdapter(adapter);
            } else {
                allNumbersList.setAdapter(adapter);
            }

            dialog.dismiss();
            restoreView();
        }

    }


}

