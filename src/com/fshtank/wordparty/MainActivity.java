package com.fshtank.wordparty;

/*
 * TODO - More Words and Categories
 * 		- how to screen - with viewpager
 * 		- settings menu 
 * 		- stupid timer onPause
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	ArrayList<String> words = new ArrayList<String>();
	ArrayList<String> correctWords = new ArrayList<String>();
	ArrayList<String> wrongWords = new ArrayList<String>();
	int wordCounter = 0;
	String catCounter;
	int aScore = 0;
	int savedAScore = 0;
	int bScore = 0;
	int savedBScore = 0;
	int soloScore = 0;
	int highScore = 0;
	boolean isTeamA = true;
	boolean gameIsPlaying = false;
	boolean isTeamPlay;
	String inputFile;
	public static String sharedFilename = "savedValues";
	Editor editor;
	CountDownTimer cdTimer;
	long total = 45000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// start of full screen view code
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// end of full screen view code

		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		inputFile = intent.getStringExtra("category");
		catCounter = intent.getStringExtra("counter");
		isTeamPlay = intent.getBooleanExtra("mode", false);

		// shared preferences restores counter
		SharedPreferences sharedPrefs = getSharedPreferences(sharedFilename, 0);
		editor = sharedPrefs.edit();
		wordCounter = sharedPrefs.getInt(catCounter, 0);
		savedAScore = sharedPrefs.getInt("aScore", 0);
		savedBScore = sharedPrefs.getInt("bScore", 0);
		aScore = savedAScore;
		bScore = savedBScore;

		isTeamA = sharedPrefs.getBoolean("isTeamA", true);
		// end of shared preferences

		try {
			File wordFile = this.getFileStreamPath(inputFile);
			InputStream inputStream = new FileInputStream(wordFile);

			if (inputStream != null) {
				InputStreamReader streamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(streamReader);

				String line;

				while ((line = bufferedReader.readLine()) != null) {
					words.add(line);
				}
			}

			inputStream.close(); // close the file
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean shuffledList = false;
		SharedPreferences settings = getSharedPreferences("shuffledState", 0);
		shuffledList = settings.getBoolean(catCounter, false);
		if (!shuffledList) {
			Collections.shuffle(words);
			settings = getSharedPreferences("shuffledState", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("FIRST_RUN", true);
			editor.commit();
		}

		final MediaPlayer correctSound = MediaPlayer.create(MainActivity.this,
				R.raw.correct_sound);
		final MediaPlayer passSound = MediaPlayer.create(MainActivity.this,
				R.raw.wrong_sound);
		final MediaPlayer timesUpSound = MediaPlayer.create(MainActivity.this,
				R.raw.timesup);
		final MediaPlayer timerClickSound = MediaPlayer.create(
				MainActivity.this, R.raw.timerclick);

		Typeface buttonType = Typeface.createFromAsset(getAssets(),
				"fonts/123Marker.ttf");
		Typeface mainWordType = Typeface.createFromAsset(getAssets(),
				"fonts/123Marker.ttf");

		// Text views
		final TextView mainText = (TextView) findViewById(R.id.mainTextView1);
		mainText.setTypeface(mainWordType);
		final TextView teamAScore = (TextView) findViewById(R.id.teamAScoreTextView);
		teamAScore.setTypeface(mainWordType);
		final TextView teamBScore = (TextView) findViewById(R.id.teamBScoreTextView);
		teamBScore.setTypeface(mainWordType);
		final TextView soloScoreTv = (TextView) findViewById(R.id.soloScoreTextView);
		soloScoreTv.setTypeface(mainWordType);
		final TextView highScoreTv = (TextView) findViewById(R.id.highScoreTextView);
		highScoreTv.setTypeface(mainWordType);
		final TextView countdown = (TextView) findViewById(R.id.countdownTextView);
		countdown.setTypeface(mainWordType);

		if (isTeamPlay) {
			teamAScore.setVisibility(View.VISIBLE);
			teamBScore.setVisibility(View.VISIBLE);
			soloScoreTv.setVisibility(View.GONE);
			highScoreTv.setVisibility(View.GONE);
		} else {
			teamAScore.setVisibility(View.GONE);
			teamBScore.setVisibility(View.GONE);
			soloScoreTv.setVisibility(View.VISIBLE);
			highScoreTv.setVisibility(View.VISIBLE);
			mainText.setText("Press start!");
		}
		if (isTeamPlay) {
			if (isTeamA) {
				mainText.setText("Team A\nPress Start!");
			} else {
				mainText.setText("Team B\nPress Start!");
			}
		}
		teamAScore.setText("Team A: " + aScore);
		teamBScore.setText("Team B: " + bScore);
		// end Text Views

		// Buttons
		final Button pushNext = (Button) findViewById(R.id.nextButton);
		pushNext.setTypeface(buttonType);
		pushNext.setTag(1);
		final Button pushPass = (Button) findViewById(R.id.passButton);
		pushPass.setTypeface(buttonType);
		pushPass.setTag(1);
		final Button pushStartButton = (Button) findViewById(R.id.startButton);
		pushStartButton.setTypeface(buttonType);
		pushStartButton.setTag(1);
		// end Buttons

		// Count down

		// total is a variable used so that the timer can restart if app loses
		// focus
		cdTimer = new CountDownTimer(total, 1000) {
			// code for count down timer
			public void onTick(long millisUntilFinished) {
				// total = millisUntilFinished;
				countdown.setText("" + millisUntilFinished / 1000);
				if (millisUntilFinished / 1000 <= 5) {
					if (timerClickSound.isPlaying()) {
						timerClickSound.pause();
						timerClickSound.seekTo(0);
						timerClickSound.start();
					} else {
						timerClickSound.start();
					}
				}
			}

			public void onFinish() {
				if (timesUpSound.isPlaying()) {
					timesUpSound.pause();
					timesUpSound.seekTo(0);
					timesUpSound.start();
				} else {
					timesUpSound.start();
				}
				countdown.setText("Times Up!");

				gameIsPlaying = false;
				pushPass.setTag(1);
				pushNext.setTag(1);
				pushStartButton.setVisibility(View.VISIBLE);
				pushStartButton.setTag(1);
				if (isTeamPlay) {
					if (isTeamA) {
						mainText.setText("You scored " + soloScore + "!"
								+ "\nTeam B\nPress Start");
					} else {
						mainText.setText("You scored " + soloScore + "!"
								+ "\nTeam A\nPress Start");
					}
					isTeamA = !isTeamA;
				} else {
					mainText.setText("You scored " + soloScore + "!"
							+ "\nPress start to try again");
				}
				if (soloScore > highScore) {
					highScore = soloScore;
					highScoreTv.setText("High Score: " + highScore);
				}
				String currentWord = words.get(wordCounter);
				wrongWords.add(currentWord);

				savedAScore = aScore;
				savedBScore = bScore;

				Intent resultsIntent = new Intent(MainActivity.this,
						ResultsActivity.class);
				resultsIntent.putStringArrayListExtra("correct", correctWords);
				resultsIntent.putStringArrayListExtra("wrong", wrongWords);
				resultsIntent.putExtra("score", "" + soloScore);

				soloScoreTv.setText("Score: " + soloScore);
				soloScore = 0;

				startActivity(resultsIntent);
			}
		};
		// end Count down

		pushStartButton.setOnClickListener(new View.OnClickListener() {
			// what to do when "Next" button is pressed
			@Override
			public void onClick(View v) {
				if (correctSound.isPlaying()) {
					correctSound.pause();
					correctSound.seekTo(0);
					correctSound.start();
				} else {
					correctSound.start();
				}
				v.setVisibility(View.GONE);
				final int status = (Integer) v.getTag();
				wordCounter++;
				if (wordCounter >= words.size()) {
					wordCounter = 0;
					Collections.shuffle(words);
					saveWords();
				}
				String currentWord = words.get(wordCounter);
				mainText.setText(currentWord);
				if (status == 1) {
					gameIsPlaying = true;
					cdTimer.start();
					pushPass.setTag(0);
					pushNext.setTag(0);
					correctWords.clear();
					wrongWords.clear();
					v.setTag(0);
					soloScore = 0;
					soloScoreTv.setText("Score: " + soloScore);
				}
			}
		});

		pushNext.setOnClickListener(new View.OnClickListener() {
			// what to do when "Next" button is pressed
			@Override
			public void onClick(View v) {
				final int status = (Integer) v.getTag();
				if (status == 0) {
					if (correctSound.isPlaying()) {
						correctSound.pause();
						correctSound.seekTo(0);
						correctSound.start();
					} else {
						correctSound.start();
					}
					String currentWord = words.get(wordCounter);
					correctWords.add(currentWord);
					wordCounter++;
					if (wordCounter >= words.size()) {
						wordCounter = 0;
						Collections.shuffle(words);
						saveWords();
					}
					currentWord = words.get(wordCounter);
					mainText.setText(currentWord);
					if (isTeamA) {
						aScore++;
						teamAScore.setText("Team A: " + aScore);
					} else {
						bScore++;
						teamBScore.setText("Team B: " + bScore);
					}
					soloScore++;
					soloScoreTv.setText("Score: " + soloScore);

				}
			}
		});

		pushPass.setOnClickListener(new View.OnClickListener() {
			// what to do when "Pass" button is pressed
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int status = (Integer) v.getTag();
				if (status == 0) {
					if (passSound.isPlaying()) {
						passSound.pause();
						passSound.seekTo(0);
						passSound.start();
					} else {
						passSound.start();
					}
					String currentWord = words.get(wordCounter);
					wrongWords.add(currentWord);
					wordCounter++;
					if (wordCounter >= words.size()) {
						wordCounter = 0;
						Collections.shuffle(words);
						saveWords();
					}
					currentWord = words.get(wordCounter);
					mainText.setText(currentWord);

				}

			}
		});

	}

	public void saveWords() {
		try {
			File f = this.getFileStreamPath(inputFile);
			FileWriter fw = new FileWriter(f);
			Writer output = new BufferedWriter(fw);
			for (int i = 0; i < words.size(); i++) {
				output.write(words.get(i).toString() + "\n");
			}
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences sharedPrefs = getSharedPreferences(sharedFilename, 0);
		editor = sharedPrefs.edit();
		editor.putInt(catCounter, wordCounter);
		editor.putInt("aScore", savedAScore);
		editor.putInt("bScore", savedBScore);
		editor.putBoolean("isTeamA", isTeamA);
		editor.commit();
		cdTimer.cancel();
		// current not sure how to stop timer from continuining.
		// finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		cdTimer.cancel();
		finish();
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

}
