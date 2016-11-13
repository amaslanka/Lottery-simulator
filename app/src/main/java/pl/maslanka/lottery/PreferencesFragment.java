package pl.maslanka.lottery;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Created by Artur on 27.10.2016.
 */

public class PreferencesFragment extends com.github.machinarius.preferencefragment.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener,  NumberPickerDialogFragmentEdited.NumberPickerDialogHandlerV2 {

    public static final String KEY_MAX_LIST_SIZE = "max_list_size";
    public static final String KEY_PREF_USER_VALUES = "pref_user_values";
    public static final String KEY_ANIMATION_PREF = "animation_pref";
    public static final String KEY_BACKUP_MAX_LIST_SIZE = "backup_max_list_size";
    private Preference maxNumber;
    private ListPreference animationType;
    private boolean userValues;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        preferences = PreferenceManager.getDefaultSharedPreferences(Logic.getStaticContext());
        maxNumber = findPreference(KEY_MAX_LIST_SIZE);
        animationType = (ListPreference) findPreference(KEY_ANIMATION_PREF);


        if (Build.VERSION.SDK_INT < 21) {
            animationType.setEnabled(false);
            animationType.setSummary(getString(R.string.list_animation_summary_error));
        } else {
            animationType.setEnabled(true);
        }

        editor = preferences.edit();


        maxNumber.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NumberPickerBuilderEdited npb = new NumberPickerBuilderEdited();
                npb.setFragmentManager(getFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light_withColourText)
                        .setMinNumber(Logic.MIN_LIMIT_MAX_ALL_NUMBERS_SIZE)
                        .setMaxNumber(Logic.MAX_LIMIT_MAX_ALL_NUMBERS_SIZE)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .setDecimalVisibility(View.INVISIBLE);

                npb.show();

                return true;
            }
        });

        setValuesAndUpdateSummary();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

        if (key.equals(KEY_PREF_USER_VALUES)) {
            userValues = preferences.getBoolean(PreferencesFragment.KEY_PREF_USER_VALUES, false);
            //If user values has been switched on again - restore a backup value of max_list_size
            if (userValues) {
                editor.putInt(KEY_MAX_LIST_SIZE, (preferences.getInt(KEY_BACKUP_MAX_LIST_SIZE, Logic.DEFAULT_MAX_ALL_NUMBERS_SIZE)));
                editor.commit();
            }

            setValuesAndUpdateSummary();

        } else if (key.equals(KEY_MAX_LIST_SIZE)) {
            userValues = preferences.getBoolean(PreferencesFragment.KEY_PREF_USER_VALUES, false);
            //Create a backup only if max_list_size has been changed and user values are on
            if (userValues) {
                editor.putInt(KEY_BACKUP_MAX_LIST_SIZE, Logic.getMaxAllNumbersPref());
                editor.commit();
            }
            setValuesAndUpdateSummary();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setValuesAndUpdateSummary() {
        userValues = preferences.getBoolean(PreferencesFragment.KEY_PREF_USER_VALUES, false);
        Log.d("userValues?", Boolean.toString(userValues));

        if (userValues) {
            editor.putInt(KEY_MAX_LIST_SIZE, Logic.getMaxAllNumbersPref());
            editor.commit();
        } else {
            editor.putInt(KEY_MAX_LIST_SIZE, Logic.DEFAULT_MAX_ALL_NUMBERS_SIZE);
            editor.commit();
        }

        maxNumber.setSummary(String.format(getString(R.string.max_drawing_number_for_list_desc), Logic.numbersFormatter(Logic.getMaxAllNumbersPref())));
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        //This method does nothing because this library doesn't support PreferenceFragments
        //Edited library class - NumberPickerDialogFragmentEdited (it extends NumberPickerDialogFragment from a library)
        //Edited library class - NumberPickerBuilderEdited (it extends NumberPickerBuilder from a library)
    }
}
