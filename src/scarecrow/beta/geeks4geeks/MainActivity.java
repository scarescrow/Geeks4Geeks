package scarecrow.beta.geeks4geeks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scarecrow.beta.geeks4geeks.R;
import scarecrow.beta.geeks4geeks.DataBaseHelper;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

	Spinner s1, s2, s3;
	String category, subtopic, topic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DataBaseHelper myDbHelper;
		myDbHelper = new DataBaseHelper(this);

		s1 = (Spinner) findViewById(R.id.spinner1);
		s2 = (Spinner) findViewById(R.id.spinner2);
		s3 = (Spinner) findViewById(R.id.spinner3);

		category = "algorithms";
		subtopic = "Analysis of Algorithms";
		topic = "Asymptotic Analysis";

		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		
		myDbHelper.close();

		loadCategories();
		loadSubtopics();
		loadTopics();

		s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(
					@SuppressWarnings("rawtypes") AdapterView adapter, View v,
					int i, long lng) {
				if (s1.getSelectedItem().toString() == "Algorithms")
					category = "algorithms";
				else
					category = "data_structures";
				loadSubtopics();
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onNothingSelected(AdapterView arg0) {

			}
		});

		s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(
					@SuppressWarnings("rawtypes") AdapterView adapter, View v,
					int i, long lng) {
				subtopic = s2.getSelectedItem().toString();
				loadTopics();
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onNothingSelected(AdapterView arg0) {

			}
		});

		s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onItemSelected(AdapterView adapter, View v, int i,
					long lng) {
				topic = s3.getSelectedItem().toString();
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void onNothingSelected(AdapterView arg0) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void load(View view) {
		
		
		Intent i = new Intent(MainActivity.this, Activity_Webviewer.class);
		i.putExtra("topic", topic);
		i.putExtra("category", category);
		startActivity(i);
	}

	private void loadCategories() {
		List<String> categories = new ArrayList<String>();
		categories.add("Algorithms");
		categories.add("Data Structure");

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);

		// attaching data adapter to spinner
		s1.setAdapter(dataAdapter);
	}

	private void loadSubtopics() {
		// database handler
		DataBaseHelper db = new DataBaseHelper(getApplicationContext());

		// Spinner Drop down elements
		List<String> subtopics = db.getAllSubtopics(category);

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, subtopics);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);

		// attaching data adapter to spinner
		s2.setAdapter(dataAdapter);
		db.close();
	}

	private void loadTopics() {
		// database handler
		DataBaseHelper db = new DataBaseHelper(getApplicationContext());

		// Spinner Drop down elements
		List<String> subtopics = db.getAllTopics(category, subtopic);

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.multiline_spinner_dropdown_item, subtopics);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);

		// attaching data adapter to spinner
		s3.setAdapter(dataAdapter);
		
		db.close();
	}

}
