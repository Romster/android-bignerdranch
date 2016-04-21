package ru.romster.bignerdranch.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

import ru.romster.bignerdranch.criminalintent.controller.fragment.DatePickerFragment;

/**
 * Created by romster on 21/04/16.
 */
public class DatePickerActivity extends SingleFragmentActivity {

	public static final String EXTRA_CRIME_TIMESTAMP = "ru.romster.bignerdranch.criminalintent.crime_timestamp";

	public static Intent newIntent(Context context, Date crimeTimestamp) {
		Intent intent = new Intent(context, DatePickerActivity.class);
		intent.putExtra(EXTRA_CRIME_TIMESTAMP, crimeTimestamp);
		return intent;
	}

	@Override
	protected Fragment createFragment() {
		Date date = (Date) getIntent().getSerializableExtra(EXTRA_CRIME_TIMESTAMP);
		return DatePickerFragment.newInstance(date);
	}
}
