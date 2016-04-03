package com.bignerdranch.romster.geoquiz;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.romster.geoquiz.model.Question;

public class QuizActivity extends AppCompatActivity {

	private static final String TAG = QuizActivity.class.getName();

	private static final String KEY_INDEX = "index";

	private final View.OnClickListener onClickListener;

	private Button trueButton;
	private Button falseButton;
	private ImageButton nextButton;
	private ImageButton prevButton;
	private TextView questionTextView;


	private Question[] questionBank = new Question[] {
			new Question(R.string.question_ocens, true),
			new Question(R.string.question_mideast, false),
			new Question(R.string.question_africa, false),
			new Question(R.string.question_americas, true),
			new Question(R.string.question_asia, true)
	};
	private int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		if(savedInstanceState != null) {
			currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
		}
		setContentView(R.layout.activity_quiz);

		trueButton = (Button) findViewById(R.id.true_button);
		falseButton = (Button) findViewById(R.id.false_button);
		nextButton = (ImageButton) findViewById(R.id.next_button);
		prevButton = (ImageButton) findViewById(R.id.prev_button);
		questionTextView = (TextView) findViewById(R.id.question_text_view);

		trueButton.setOnClickListener(onClickListener);
		falseButton.setOnClickListener(onClickListener);
		nextButton.setOnClickListener(onClickListener);
		prevButton.setOnClickListener(onClickListener);

		updateQuestion();
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
		outState.putInt(KEY_INDEX, currentIndex);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart called");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause called");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume called");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop called");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy called");
	}

	private void updateQuestion() {
		int question = questionBank[currentIndex].getTextResId();
		questionTextView.setText(question);
	}

	private void checkAnswer(boolean answer) {
		boolean correctAnswer = questionBank[currentIndex].isAnswerTrue();
		int messageId;
		if(answer == correctAnswer) {
			messageId = R.string.correct_toast;
		} else {
			messageId = R.string.incorrect_toast;
		}
		Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
	}


	////////////////////////////////////////////////
	///////////////Initialization block/////////////
	////////////////////////////////////////////////
	{
		onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.true_button: {
						checkAnswer(true);
						break;
					}
					case R.id.false_button: {
						checkAnswer(false);
						break;
					}
					case R.id.prev_button: {
						currentIndex = currentIndex > 0? currentIndex - 1 : questionBank.length - 1;
						updateQuestion();
						break;
					}
					case R.id.question_text_view:
					case R.id.next_button: {
						currentIndex = (currentIndex + 1) % questionBank.length;
						updateQuestion();
						break;
					}
					default:
						Log.w("OnClick", "Unsupported");
				}
			}
		};
	}


}
