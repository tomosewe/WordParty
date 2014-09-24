package com.fshtank.wordparty;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Build;

public class StartScreenActivity extends ActionBarActivity {
	
	public static String sharedFilename = "savedValues";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// start of full screen view code
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// end of full screen view code
		setContentView(R.layout.activity_start);
		

		// if app is running for first time, copy .txt files from assets folder
		// into local storage
		boolean appHasRunBefore = false;
		SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
		appHasRunBefore = settings.getBoolean("FIRST_RUN", false);
		if (!appHasRunBefore) {
			copyAssets();
			settings = getSharedPreferences("PREFS_NAME", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("FIRST_RUN", true);
			editor.commit();
		}
		// end of asset copy
		
		int zero = 0;
		SharedPreferences sharedPrefs = getSharedPreferences(sharedFilename, 0);
		Editor editor = sharedPrefs.edit();
		editor.putInt("aScore", zero);
		editor.putInt("bScore", zero);		
		editor.putBoolean("isTeamA", true);
		editor.commit();

		
		//start of dialog builder	
		
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	    builder.setTitle("How to Play");
//	    LayoutInflater inflater = this.getLayoutInflater();
//	    builder.setView(inflater.inflate(R.layout.howtodialog, null));	    
//	    
//	    final AlertDialog alert = builder.create();
	    
	    
	    //end of dialog builder
	    
	    final MediaPlayer buttonSound = MediaPlayer.create(
				StartScreenActivity.this, R.raw.button_sound);
	    
	    Typeface mainWordType = Typeface.createFromAsset(getAssets(), "fonts/123Marker.ttf");
	    		
		Button pushSoloPlayButton = (Button) findViewById(R.id.soloPlayButton);
		pushSoloPlayButton.setTypeface(mainWordType);
		Button pushTeamPlayButton = (Button) findViewById(R.id.teamPlayButton);
		pushTeamPlayButton.setTypeface(mainWordType);
		Button pushHowToButton = (Button) findViewById(R.id.howToButton);
		pushHowToButton.setTypeface(mainWordType);

		pushSoloPlayButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Start" button is pressed
			@Override
			public void onClick(View v) {
				// Start the next activity.
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);
					buttonSound.start();
				} else {
					buttonSound.start();
				}
				Intent intent = new Intent(StartScreenActivity.this,
						CatActivity.class);
				intent.putExtra("mode", false);
				startActivity(intent);
			}
		});

		pushTeamPlayButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Team Play" button is pressed
			@Override
			public void onClick(View v) {
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);
					buttonSound.start();
				} else {
					buttonSound.start();
				}
				Intent intent = new Intent(StartScreenActivity.this,
						CatActivity.class);
				intent.putExtra("mode", true);
				startActivity(intent);
			}
		});

		pushHowToButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "How to Play" button is pressed
			@Override
			public void onClick(View v) {
				if (buttonSound.isPlaying()) {
					buttonSound.pause();
					buttonSound.seekTo(0);
					buttonSound.start();
				} else {
					buttonSound.start();
				}
				Intent intent = new Intent(StartScreenActivity.this,
						HowtoActivity.class);
				startActivity(intent);
				
				
//				alert.show();

			}
		});
	}

	private void copyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				File outFile = this.getFileStreamPath(filename);
				out = new FileOutputStream(outFile);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (IOException e) {
				Log.e("tag", "Failed to copy asset file: " + filename, e);
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
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
