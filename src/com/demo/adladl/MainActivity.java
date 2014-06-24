package com.demo.adladl;

import com.adladl.oline.Adladl;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private int score = 0;

	public static MainActivity mnact;
	public static Adladl adl;
	public static IMBanner banner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mnact = this;
		setContentView(R.layout.activity_main);
		
		adl = new Adladl(this, "xxx", (WebView)findViewById(R.id.adViewer));
		adl.offline(findViewById(R.id.banner));
		
		if (Prefs.getUseAdNet(this)){
			turnInMobiOn(this);
		}
		
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
			
			adl.clearAds();
			return true;
		} else if (id == R.id.instruct_on) {
			Toast.makeText(getBaseContext(), "Instructions On",
					Toast.LENGTH_LONG).show();
			
			adl.instructOn();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();

		System.out.println("In DESTROY");
		adl.onDestroy();
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

				adl.prizeWon();
				
				ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);;
			        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
			} else {
				textv.setTextColor(Color.rgb(0,0,0));
			}
			return;
		}
	}
	
	
	public void toSettings(MenuItem item) {
		
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
    
	public static String serverUrl() {
		
		String httpserver;
		
		httpserver = Prefs.getHttpServer(mnact);
		if (httpserver.length() > 14) {
			return httpserver;
		} else {
			return "www.adladl.com";
		}
	}
	
	
// This may not be needed (Thread) but it seems to load faster - must be on UiThread for Javascript Interface
	public  void turnInMobiOn(final Context ctx) {

		runOnUiThread(new Runnable() {
			public void run() {
				try {
					Toast.makeText(getBaseContext(), "AdMobi network started",
	    					Toast.LENGTH_LONG).show();
		        	System.out.println("AdMobi network started");
					
					InMobi.initialize(ctx, "feb4c5de148f49dd888099f874fb455f");
					banner = (IMBanner) findViewById(R.id.banner);
					banner.setRefreshInterval(10);
					banner.loadBanner();
					InMobi.setLogLevel(LOG_LEVEL.DEBUG);
					
					
					
					banner.setIMBannerListener(new IMBannerListener() {
				        @Override
				        public void onShowBannerScreen(IMBanner arg0) {
				                }
				            @Override
				        public void onLeaveApplication(IMBanner arg0) {
				        }
				        @Override
				        public void onDismissBannerScreen(IMBanner arg0) {
				        }
				                @Override
				        public void onBannerRequestFailed(IMBanner banner, IMErrorCode errorCode) {
//				                	Toast.makeText(getBaseContext(), "Failed : "+errorCode.toString(),
//				        					Toast.LENGTH_LONG).show();
				                	System.out.println("onBannerRequestFailed : "+ errorCode.toString());
//				                	adl.offline(findViewById(R.id.banner));
				        }
				        @Override
				        public void onBannerRequestSucceeded(IMBanner arg0) {
//				        	Toast.makeText(getBaseContext(), "onBannerRequestSucceeded",
//			    					Toast.LENGTH_LONG).show();
//				        	adl.online(findViewById(R.id.banner));
				        	System.out.println("onBannerRequestSucceeded");
				                }
				                @Override
				        public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
				        }
				    });
					
				} catch (Exception ex) {
					System.out.println("Select thread exception : " + ex);
				}
			}
		});
}



}
