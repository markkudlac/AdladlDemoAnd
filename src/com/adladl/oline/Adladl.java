package com.adladl.oline;


import com.demo.adladl.MainActivity;
import com.demo.adladl.Prefs;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;



public class Adladl {
	
	private  WebView adwebv = null;
	private  String app_reg;
	private  Activity activity;
	
	private int success_cnt = 0;
//	private String prize = "a";
	
	public Adladl(Activity activity, String app_reg, WebView adwebv){
		this.adwebv = adwebv;
		this.app_reg = app_reg;
		this.activity = activity;
		
		Intent intent = new Intent();
		intent.setClassName("com.adserv.adladl", "com.adserv.adladl.HttpdService");
		activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		activity.startService(intent);
		
		WebSettings webSettings = adwebv.getSettings();
		webSettings.setJavaScriptEnabled(true);		
		adwebv.addJavascriptInterface(this.new JsInterface(), "jsinterface");

	}

	public void onDestroy(){
		
		activity.unbindService(mConnection);
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	System.out.println("onServiceConnected in adladl");
        	
//    		System.out.println("Android ID : " + droidId);
    		
    		loadAds();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	System.out.println("onServiceDisC in adladl");
        }
    };
   
    
    public void loadAds() {
		
		if (adwebv == null) { 
			System.out.println("webarg null"); 
			return;
		}

		String xurl = "http://localhost:8080/AdlHtml/adunit.html?prize="+Prefs.getPrizeMode(activity)+
				"&app_reg="+app_reg;
		
//		System.out.println("Enter loadAds : "+xurl);
		adwebv.loadUrl(xurl);	
	}
    


	public void prizeWon(){
		adwebv.loadUrl("javascript:prizewon()");
	}
	
	
	private final class JsInterface {
		
		@JavascriptInterface		
	      public void vault() {
	        	 
	        activity.runOnUiThread(new Runnable() {
	            
	        	 @Override
	            public void run() {

	            	Intent intent = new Intent();
	        		intent.setClassName("com.adserv.adladl", "com.adserv.adladl.CouponActivity");
	        		activity.startActivity(intent);
	            }
	         });
	      }
		
		
		@JavascriptInterface
		public boolean localHref(){
			return(!isWifiConected(activity));
		}
	   }
	
	
	public void online(View vw){
		
		if (success_cnt == 2) {
			vw.setVisibility(View.VISIBLE);
			adwebv.setVisibility(View.GONE);
		} else if (success_cnt <= 2){
			++success_cnt;
		}
	}
	
	
	public void offline(View vw){

		success_cnt = 0;
		adwebv.setVisibility(View.VISIBLE);
		vw.setVisibility(View.GONE);
	}
	
	
	//This is only for testing sand should be removed
	public void clearAds() {

		adwebv.loadUrl("javascript:clearads()");	
	}
	
	
	//This is only for testing sand should be removed
	public void instructOn() {

		adwebv.loadUrl("javascript:instructSet(0)");	
	}
	
	
    public static boolean isWifiConected(Context context) {
     	
   	 WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
   	 
   	 if (wifiMgr != null && wifiMgr.isWifiEnabled()){
   		 
   		 ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

   		 if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
//   			 System.out.println("WiFi is connected.");
	             return(true);
   		 }
   	 }
//   	 System.out.println("WiFi NOT connected.");
    	return(false);
    }
}
