package com.demo.adladl;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
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
		
		Intent intent = new Intent();
		intent.setClassName("com.adserv.adladl", "com.adserv.adladl.HttpdService");
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		startService(intent);
		
		mnact = this;
		setContentView(R.layout.activity_main);
		
		InMobi.initialize(this, "feb4c5de148f49dd888099f874fb455f");
		IMBanner banner = (IMBanner) findViewById(R.id.banner);
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
	                	Toast.makeText(getBaseContext(), "Failed : "+errorCode.toString(),
	        					Toast.LENGTH_LONG).show();
	                	System.out.println("onBannerRequestFailed : "+ errorCode.toString());
	        }
	        @Override
	        public void onBannerRequestSucceeded(IMBanner arg0) {
	        	Toast.makeText(getBaseContext(), "onBannerRequestSucceeded",
    					Toast.LENGTH_LONG).show();
//	        	System.out.println("onBannerRequestSucceeded");
	                }
	                @Override
	        public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
	        }
	    });
	}

	
	   private ServiceConnection mConnection = new ServiceConnection() {

	        @Override
	        public void onServiceConnected(ComponentName className,
	                IBinder service) {
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	        	System.out.println("onServiceConnected in adladl");
	        	
	    		adwebv = (WebView)findViewById(R.id.adViewer);
	    		droidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
//	    		System.out.println("Android ID : " + droidId);
	    		
	    		loadAds(adwebv, droidId);
	        }

	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	        	System.out.println("onServiceDisC in adladl");
	        }
	    };
	    
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
		} else if (id == R.id.instruct_on) {
			Toast.makeText(getBaseContext(), "Instructions On",
					Toast.LENGTH_LONG).show();
			
			adwebv.loadUrl("javascript:instructSet(0)");
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
	
	public void toSettings(MenuItem item) {
		
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	
	
	public static void loadAds(WebView webarg, String devicetag) {
//		System.out.println("Enter loadAds");
		String prize;
		
		if (webarg == null) { System.out.println("webarg null"); 
		return;}
		
		WebSettings webSettings = webarg.getSettings();
		webSettings.setJavaScriptEnabled(true);		
		webarg.addJavascriptInterface(mnact.new JsInterface(), "jsinterface");
		
		if (Prefs.getPrizeMode(mnact)) {
//			if (false) {
			prize = "p";
		} else {
			prize = "a";
		}
//		webarg.loadUrl("http://" + serverUrl() + "/adunit/"+prize+"/"+devicetag);	
		webarg.loadUrl("http://localhost:8080/AdlHtml/adunit.html");	
	}
	
	
	public static void clearAds(WebView webarg, String devicetag) {

		System.out.println("Enter clearAds test");
		webarg.loadUrl("javascript:clearads()");	
	}
	
	
	
	public static void changePrizeMode(){
		
		loadAds(adwebv, droidId);
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
	
	
	private final class JsInterface {
		
		@JavascriptInterface		
	      public void vault() {
	        	 System.out.println("In JsInterface vault");
	        	 
	         runOnUiThread(new Runnable() {
	            
	        	 @Override
	            public void run() {

	            	Intent intent = new Intent();
	        		intent.setClassName("com.adserv.adladl", "com.adserv.adladl.CouponActivity");
	        		startActivity(intent);
	            }
	         });
	      }
	   }
}
