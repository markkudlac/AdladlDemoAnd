package com.demo.adladl;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.*;


public class Prefs extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
	}
	
	 @Override
     public void onResume() {
         super.onResume();
         getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	 }
	 
     @Override
     public void onPause() {
         super.onPause();
         getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
     }
     
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	
    	System.out.println("In SettingsActivity PrefChanged 1 : "+key);
    	
        if (key.equals(getResources().getString(R.string.prizemode))) {
//            Preference connectionPref = findPreference(key);
        	
        	System.out.println("In SettingsActivity PrefChanged 2 : "+key);
  
            boolean pmode = sharedPreferences.getBoolean(key, false);
            System.out.println("In SettingsActivity Prizemode : "+pmode);
            MainActivity.mnact.setPrizeMode(pmode);
        }
    }
}