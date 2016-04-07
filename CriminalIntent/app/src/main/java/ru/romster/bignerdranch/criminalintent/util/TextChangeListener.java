package ru.romster.bignerdranch.criminalintent.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by romster on 07/04/16.
 */
public abstract class TextChangeListener implements TextWatcher {
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		//Do nothing
	}

	@Override
	public void afterTextChanged(Editable s) {
		//Do nothing
	}
}
