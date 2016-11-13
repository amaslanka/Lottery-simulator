package pl.maslanka.lottery;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Artur on 31.10.2016.
 */

public class BetDrawingFragment extends android.support.v4.app.Fragment {

    DrawingBetAmount drawingBetAmount;
    private Activity activity;

    public BetDrawingFragment() {
        super();
    }

    public void beginTask(Integer... ints) {
        drawingBetAmount = new DrawingBetAmount(activity);
        drawingBetAmount.execute(ints);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        if (drawingBetAmount != null) {
            drawingBetAmount.onAttach(activity);
        }
        Log.d("Fragment", "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Fragment", "onCreate()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Fragment", "onCreateView()");
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Fragment", "onActivityCreated()");
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Fragment", "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment", "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment", "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment", "onStop()");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Fragment", "onSavInstanceState()");

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("Fragment", "onViewStateRestored()");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Fragment", "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment", "onDestroy()");

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (drawingBetAmount != null) {
            drawingBetAmount.onDetach();
        }
        Log.d("Fragment", "onDetach()");

    }

}
