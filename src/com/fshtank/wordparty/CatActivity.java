package com.fshtank.wordparty;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

public class CatActivity extends ActionBarActivity {
	
	public static String sharedFilename = "savedValues";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//start of full screen view code
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    //end of full screen view code
		setContentView(R.layout.activity_cat);
		
		//gets mode from StartScreenActivity
		Intent catIntent = getIntent();
		final boolean isTeamPlay = catIntent.getBooleanExtra("mode", false);
		

		final MediaPlayer buttonSound = MediaPlayer.create(CatActivity.this, R.raw.button_sound);
		
		Typeface mainWordType = Typeface.createFromAsset(getAssets(), "fonts/123Marker.ttf");
		
		final TextView categories = (TextView) findViewById(R.id.categoriesTextView);
		categories.setTypeface(mainWordType);
		
		Button moviesButton = (Button) findViewById(R.id.movies_button);
		moviesButton.setTypeface(mainWordType);
		
		Button tvButton = (Button) findViewById(R.id.tv_button);
		tvButton.setTypeface(mainWordType);
		
		Button wordsButton = (Button) findViewById(R.id.words_button);
		wordsButton.setTypeface(mainWordType);
		
		Button kidsButton = (Button) findViewById(R.id.kids_button);
		kidsButton.setTypeface(mainWordType);
		
		Button peopleButton = (Button) findViewById(R.id.people_button);
		peopleButton.setTypeface(mainWordType);
		
		Button nounsButton = (Button) findViewById(R.id.nouns_button);
		nounsButton.setTypeface(mainWordType);

		moviesButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Start" button is pressed
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// Start the next activity. 
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);		
					buttonSound.start();
				} else {
					buttonSound.start();
				}	
				Intent intent = new Intent(CatActivity.this, MainActivity.class);
				intent.putExtra("category", "movies.txt");
				intent.putExtra("counter", "moviesCounter");
				intent.putExtra("mode", isTeamPlay);
				startActivity(intent);
			}
		});
		
		tvButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Start" button is pressed
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// Start the next activity. 
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);		
					buttonSound.start();
				} else {
					buttonSound.start();
				}	
				Intent intent = new Intent(CatActivity.this, MainActivity.class);
				intent.putExtra("category", "tvshows.txt");
				intent.putExtra("counter", "tvCounter");	
				intent.putExtra("mode", isTeamPlay);
				startActivity(intent);
			}
		});
		
		wordsButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Start" button is pressed
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// Start the next activity. 
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);	
					buttonSound.start();
				} else {
					buttonSound.start();
				}	
				Intent intent = new Intent(CatActivity.this, MainActivity.class);
				intent.putExtra("category", "justwords.txt");
				intent.putExtra("counter", "wordsCounter");	
				intent.putExtra("mode", isTeamPlay);
				startActivity(intent);
			}
		});
	
	
		kidsButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Start" button is pressed
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// Start the next activity. 
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);					
				} else {
					buttonSound.start();
				}	
				Intent intent = new Intent(CatActivity.this, MainActivity.class);
				intent.putExtra("category", "kids.txt");
				intent.putExtra("counter", "kidsCounter");	
				intent.putExtra("mode", isTeamPlay);
				startActivity(intent);
			}
		});
		
		peopleButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Start" button is pressed
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// Start the next activity. 
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);					
				} else {
					buttonSound.start();
				}	
				Intent intent = new Intent(CatActivity.this, MainActivity.class);
				intent.putExtra("category", "people.txt");
				intent.putExtra("counter", "peopleCounter");
				intent.putExtra("mode", isTeamPlay);
				startActivity(intent);
			}
		});
		
		nounsButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Start" button is pressed
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// Start the next activity. 
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);					
				} else {
					buttonSound.start();
				}	
				Intent intent = new Intent(CatActivity.this, MainActivity.class);
				intent.putExtra("category", "nouns.txt");
				intent.putExtra("counter", "nounsCounter");
				intent.putExtra("mode", isTeamPlay);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		int zero = 0;
		super.onBackPressed();	
		SharedPreferences sharedPrefs = getSharedPreferences(sharedFilename, 0);
		Editor editor = sharedPrefs.edit();
		editor.putInt("aScore", zero);
		editor.putInt("bScore", zero);	
		editor.putBoolean("isTeamA", true);
		editor.commit();
		finish();	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cat, menu);
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
