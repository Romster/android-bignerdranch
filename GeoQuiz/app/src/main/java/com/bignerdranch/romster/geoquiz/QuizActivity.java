package com.bignerdranch.romster.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

	private final View.OnClickListener onClickListener;

	private Button trueButton;
	private Button falseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		trueButton = (Button) findViewById(R.id.true_button);
		falseButton = (Button) findViewById(R.id.false_button);
		trueButton.setOnClickListener(onClickListener);
		falseButton.setOnClickListener(onClickListener);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	//
	////////////////////////////Initialization block
	//
	{
		onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.true_button: {
						Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
						break;
					}
					case R.id.false_button: {
						Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
						break;
					}
					default:
						Log.w("OnClick", "Unsupported");
				}
			}
		};
	}


}
