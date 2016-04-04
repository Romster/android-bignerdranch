package com.bignerdranch.romster.geoquiz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.romster.geoquiz.R;

public class CheatActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.romster.geoquiz.answer_is_true";
	private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.romster.geoquiz.answer_shown";
	private static final String EXTRA_ANSWER_WAS_CHEATED = "com.bignerdranch.romster.geoquiz.answer_was_cheated";

	private static final String KEY_FLAG = "flag";

	private boolean isAnswerTrue;

	private TextView answerTextView;
	private Button showAnswer;
	private boolean isAnswerShown;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		if (savedInstanceState != null) {
			isAnswerShown = savedInstanceState.getBoolean(KEY_FLAG);
		} else {
			isAnswerShown = getIntent().getBooleanExtra(EXTRA_ANSWER_WAS_CHEATED, false);
		}

		isAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

		answerTextView = (TextView) findViewById(R.id.answer_text_view);
		showAnswer = (Button) findViewById(R.id.show_answer_button);

		showAnswer.setOnClickListener(this);
		updateText();
	}

	@Override
	public void onClick(View v) {
		isAnswerShown = true;
		setAnswerShownResult(true);
		updateText();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_FLAG, isAnswerTrue);
	}

	private void setAnswerShownResult(boolean isAnswerShown) {
		Intent i = new Intent();
		i.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
		setResult(RESULT_OK, i);
	}

	private void updateText() {
		if (isAnswerShown) {
			if (isAnswerTrue) {
				answerTextView.setText(R.string.true_button);
			} else {
				answerTextView.setText(R.string.false_button);
			}
		}
	}


	public static Intent newIntent(Context packageContext, boolean isAnswerTrue, boolean wasCheated) {
		Intent i = new Intent(packageContext, CheatActivity.class);
		i.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue);
		i.putExtra(EXTRA_ANSWER_WAS_CHEATED, wasCheated);
		return i;
	}

	public static boolean wasAnswerShown(Intent data) {
		return data.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
	}
}
