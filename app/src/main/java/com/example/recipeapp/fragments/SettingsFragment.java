package com.example.recipeapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.example.recipeapp.R;

public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference languagePreference = findPreference("language");
        Preference notificationPreference = findPreference("allowNotifications");
        Preference darkModePreference = findPreference("darkMode");
        Preference notificationTimePreference = findPreference("notTime");

        languagePreference.setOnPreferenceChangeListener(this);
        notificationPreference.setOnPreferenceChangeListener(this);
        darkModePreference.setOnPreferenceChangeListener(this);
        notificationPreference.setOnPreferenceChangeListener(this);

        SharedPreferences lang_prefs =  PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        onPreferenceChange(languagePreference, lang_prefs.getString(languagePreference.getKey(),""));

        SharedPreferences not_prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        onPreferenceChange(notificationPreference, not_prefs.getBoolean(notificationPreference.getKey(),true));

        SharedPreferences dark_prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        onPreferenceChange(darkModePreference, dark_prefs.getBoolean(darkModePreference.getKey(),true));

        SharedPreferences ntf_prefs = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        onPreferenceChange(notificationTimePreference, ntf_prefs.getString(notificationTimePreference.getKey(), ""));

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }


}
