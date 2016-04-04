package com.bignerdranch.romster.geoquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.romster.geoquiz.R;
import com.bignerdranch.romster.geoquiz.model.Question;

public class QuizActivity extends AppCompatActivity {


	private static final String TAG = QuizActivity.class.getName();

	private static final String KEY_INDEX = "index";
	private static final String KEY_CHEATED_ARRAY = "cheated_array";
	private static final int REQUEST_CODE_CHEAT = 0x0;

	private final View.OnClickListener onClickListener;

	private Button trueButton;
	private Button falseButton;
	private Button cheatButton;
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

	private boolean[] cheatedQuestions = new boolean[questionBank.length];

	private int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		if(savedInstanceState != null) {
			currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
			cheatedQuestions = savedInstanceState.getBooleanArray(KEY_CHEATED_ARRAY);
		}
		setContentView(R.layout.activity_quiz);

		trueButton = (Button) findViewById(R.id.true_button);
		falseButton = (Button) findViewById(R.id.false_button);
		cheatButton = (Button) findViewById(R.id.cheat_button);
		nextButton = (ImageButton) findViewById(R.id.next_button);
		prevButton = (ImageButton) findViewById(R.id.prev_button);
		questionTextView = (TextView) findViewById(R.id.question_text_view);

		trueButton.setOnClickListener(onClickListener);
		falseButton.setOnClickListener(onClickListener);
		nextButton.setOnClickListener(onClickListener);
		prevButton.setOnClickListener(onClickListener);
		cheatButton.setOnClickListener(onClickListener);

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
		outState.putBooleanArray(KEY_CHEATED_ARRAY, cheatedQuestions);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHEAT) {
			if(data != null) {
				boolean isCheater = CheatActivity.wasAnswerShown(data);
				cheatedQuestions[currentIndex] = isCheater;
			}
		}
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
		if(cheatedQuestions[currentIndex]) {
			messageId = R.string.judgment_toast;
		} else {
			if (answer == correctAnswer) {
				messageId = R.string.correct_toast;
			} else {
				messageId = R.string.incorrect_toast;
			}
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
					case R.id.cheat_button: {
						boolean isAnswerTrue = questionBank[currentIndex].isAnswerTrue();
						boolean wasCheated = cheatedQuestions[currentIndex];
						Intent i = CheatActivity.newIntent(QuizActivity.this, isAnswerTrue, wasCheated);
						startActivityForResult(i, REQUEST_CODE_CHEAT);
						break;
					}
					case R.id.prev_button: {
						currentIndex = currentIndex > 0? currentIndex - 1 : questionBank.length - 1;
						updateQuestion();
						break;
					}
					case R.id.next_button:
					case R.id.question_text_view:{
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
