package scarecrow.beta.geeks4geeks;

import java.net.URLEncoder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import scarecrow.beta.geeks4geeks.R;
import scarecrow.beta.geeks4geeks.DataBaseHelper;

public class Activity_Webviewer extends Activity {

	WebView myWebView;
	DataBaseHelper dbHelper;
	String[] details;

	@SuppressWarnings("unused")
	private String Topic, subtopic, link, code;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__webviewer);
		
		dbHelper = new DataBaseHelper(this);

		myWebView = (WebView) findViewById(R.id.webView1);
		myWebView.getSettings().setBuiltInZoomControls(true);

		Intent i = getIntent();

		String topic = i.getStringExtra("topic");
		String category = i.getStringExtra("category");

		setTitle(topic);
		if (category.charAt(0) == 'a') {
			details = dbHelper.search_algo(topic);
		} else {
			details = dbHelper.search_ds(topic);
		}

		dbHelper.close();
		if (details[3].length() <= 2) {
			code = "An Error Occurred. Please Try Again.";
			myWebView.loadData(code, "text/html", "UTF-8");
		} else {
			Topic = details[0];
			subtopic = details[1];
			link = details[2];
			code = details[3];

			code = code.replaceAll("[^\\p{ASCII}]", "");
			if (code.contains("DOCTYPE")) {
				myWebView.loadUrl(link);
			} else {
				code = code + "<br><br>To see full page, <a href=\"" + link
						+ "\">Click Here</a>";
				myWebView.loadData(
						URLEncoder.encode(code).replaceAll("\\+", " "),
						"text/HTML", "UTF-8");
			}
		}

	}
	
	public void share(MenuItem item) {
		Intent sharing = new Intent(android.content.Intent.ACTION_SEND);
		sharing.setType("text/plain");
		String sharebody = "Geeks For Geeks Topic: " + Topic + ", Url: " + link;
		sharing.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shared stuff from Geeks4Geeks");
		sharing.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
		startActivity(Intent.createChooser(sharing, "Share via"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity__webviewer, menu);
		return true;
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}*/
}
