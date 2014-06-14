package com.demo.adladl;


import com.demo.adladl.R;

import android.content.Context;
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
    	
        if (key.equals(getResources().getString(R.string.prizemode))) {
        	MainActivity.adl.setPrize();
        }
    }
    
    public static boolean getPrizeMode(Context context) {

		boolean pzmode = PreferenceManager.getDefaultSharedPreferences(
				context).getBoolean(
				context.getString(R.string.prizemode), false);
//		System.out.println("In getPrizeMode : "+pzmode);
		return (pzmode);
	}

    
    public static String getHttpServer(Context context) {

		String xstr = PreferenceManager
				.getDefaultSharedPreferences(context).getString(
						context.getString(R.string.httpserver), "");

		return (xstr);
	}
}