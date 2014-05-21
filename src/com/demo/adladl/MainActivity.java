package com.demo.adladl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;



public class MainActivity extends Activity {
	
	private int score = 0;
	public static String droidId;
	public static WebView adwebv;
	public static MainActivity mnact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mnact = this;
		setContentView(R.layout.activity_main);
		
		
		adwebv = (WebView)findViewById(R.id.adViewer);
		droidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//		System.out.println("Android ID : " + droidId);
		
		loadAds(adwebv, droidId);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			toSettings(item);
			return true;
		} else if (id == R.id.clearads) {
			Toast.makeText(getBaseContext(), "Clear Ads",
					Toast.LENGTH_LONG).show();
			
			clearAds(adwebv, droidId);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}


	
	public void click(View view) {
		int id = view.getId();

		switch (id) {
		case R.id.gamebut:
//			Toast.makeText(getBaseContext(), "Button press",
//					Toast.LENGTH_LONG).show();
			
			++score;
			
			TextView textv = (TextView)findViewById(R.id.gamecount);
			textv.setText(Integer.toString(score));
			
			if (score % 5 == 0) {
				textv.setTextColor(Color.rgb(255,100,100));

				adwebv.loadUrl("javascript:prizewon()");
				
				ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);;
			        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
			} else {
				textv.setTextColor(Color.rgb(0,0,0));
			}
			
			return;
		}
	}
	
	
	public static void loadAds(WebView webarg, String devicetag) {
//		System.out.println("Enter loadAds");
		String prize;
		
		if (webarg == null) { System.out.println("webarg null"); 
		return;}
		
		WebSettings webSettings = webarg.getSettings();
		webSettings.setJavaScriptEnabled(true);		
		
		if (Prefs.getPrizeMode(mnact)) {
//			if (false) {
			prize = "g";
		} else {
			prize = "a";
		}
		webarg.loadUrl("http://192.168.1.126:3000/adunit/"+prize+"/"+devicetag);		
	}
	
	
	public static void clearAds(WebView webarg, String devicetag) {

		System.out.println("Enter clearAds");
		
		webarg.loadUrl("javascript:clearads()");	
	}
	
	
	public void toSettings(MenuItem item) {
		
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	
	public static void changePrizeMode(){
		
		loadAds(adwebv, droidId);
	}
}
