package com.bignerdranch.romster.geoquiz.model;

/**
 * Created by romster on 03/04/16.
 */
public class Question {
	private final int textResId;
	private final boolean answerTrue;


	public Question(int textResId, boolean answerTrue) {
		this.textResId = textResId;
		this.answerTrue = answerTrue;
	}

	public int getTextResId() {
		return textResId;
	}

	public boolean isAnswerTrue() {
		return answerTrue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Question question = (Question) o;

		if (textResId != question.textResId) return false;
		return answerTrue == question.answerTrue;

	}

	@Override
	public int hashCode() {
		int result = textResId;
		result = 31 * result + (answerTrue ? 1 : 0);
		return result;
	}
}
