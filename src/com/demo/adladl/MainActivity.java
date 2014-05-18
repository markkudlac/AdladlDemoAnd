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
	public static MainActivity mnact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		WebView webv = (WebView)findViewById(R.id.adViewer);
		String droidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//		System.out.println("Android ID : " + droidId);
		
		loadAds(webv, droidId);
		mnact = this;
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
				WebView webv = (WebView)findViewById(R.id.adViewer);
				webv.loadUrl("javascript:prizewon()");
				
				ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);;
			        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
			} else {
				textv.setTextColor(Color.rgb(0,0,0));
			}
			
			return;
		}
	}
	
	
	public static void loadAds(WebView webarg, String droidId) {
//		System.out.println("Enter loadAds");
		
		if (webarg == null) { System.out.println("webarg null"); 
		return;}
		
		WebSettings webSettings = webarg.getSettings();
		webSettings.setJavaScriptEnabled(true);		
		
		webarg.loadUrl("http://192.168.1.126:3000/adunit/"+droidId);		
	}
	
	public void toSettings(MenuItem item) {
		
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	
	public void setPrizeMode(boolean pmode){
		
		WebView webv = (WebView)findViewById(R.id.adViewer);
		webv.loadUrl("javascript:prizemode("+pmode+")");
	}
}
