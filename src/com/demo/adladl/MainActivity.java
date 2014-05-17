package com.demo.adladl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import android.provider.Settings.Secure;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		WebView webv = (WebView)findViewById(R.id.adViewer);
		String droidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		System.out.println("Android ID : " + droidId);
		
		loadAds(webv, droidId);
		
		
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 *
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
*/
	
	public void click(View view) {
		int id = view.getId();

		switch (id) {
		case R.id.gamebut:
			Toast.makeText(getBaseContext(), "Button press",
					Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	
	public static void loadAds(WebView webarg, String droidId) {

		System.out.println("Enter loadAds");
		
		if (webarg == null) { System.out.println("webarg null"); 
		return;}
		
		WebSettings webSettings = webarg.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		
		webarg.loadUrl("http://192.168.1.126:3000/adunit/"+droidId);
		
//		 String summary = "<html><body>You scored <b>192</b> points.</body></html>";
//		 webarg.loadData(summary, "text/html", null);
		 
		
	}
	
}
