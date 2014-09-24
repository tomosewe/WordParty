package com.fshtank.wordparty;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class ResultsActivity extends ActionBarActivity {

	ArrayList<String> correctWords = new ArrayList<String>();
	ArrayList<String> wrongWords = new ArrayList<String>();
	String score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// start of full screen view code
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// end of full screen view code
		setContentView(R.layout.activity_results);

		Intent intent = getIntent();
		correctWords = intent.getStringArrayListExtra("correct");
		wrongWords = intent.getStringArrayListExtra("wrong");
		score = intent.getStringExtra("score");
		
		Typeface mainWordType = Typeface.createFromAsset(getAssets(), "fonts/123Marker.ttf");

		final TextView correctTextView = (TextView) findViewById(R.id.correctAnswersTV);
		correctTextView.setTypeface(mainWordType);
		final TextView wrongTextView = (TextView) findViewById(R.id.wrongAnswersTV);
		wrongTextView.setTypeface(mainWordType);
		final TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
		scoreTextView.setTypeface(mainWordType);
		
		scoreTextView.setText("You scored: " + score);		
		
		StringBuilder corrrectWordsBuilder = new StringBuilder();
		for (String words : correctWords) {
			corrrectWordsBuilder.append(words + "\n\n");
		}
		correctTextView.setText(corrrectWordsBuilder.toString());
		
		StringBuilder wrongWordsBuilder = new StringBuilder();
		for (String words : wrongWords) {
			wrongWordsBuilder.append(words + "\n\n");
		}
		wrongTextView.setText(wrongWordsBuilder.toString());		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
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

}
