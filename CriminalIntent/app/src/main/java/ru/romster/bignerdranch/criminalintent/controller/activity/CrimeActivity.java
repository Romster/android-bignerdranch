package ru.romster.bignerdranch.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import ru.romster.bignerdranch.criminalintent.controller.fragment.CrimeFragment;

public class CrimeActivity extends SingleFragmentActivity {

	public static final String EXTRA_CRIME_ID = "ru.romster.bignerdranch.criminalintent.crime_id";

	public static Intent newIntent(Context context, UUID crimeId) {
		Intent intent = new Intent(context, CrimeActivity.class);
		intent.putExtra(EXTRA_CRIME_ID, crimeId);
		return intent;
	}

	@Override
	protected Fragment createFragment() {
		UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
		CrimeFragment fragment = CrimeFragment.newInstance(crimeId);
		return fragment;
	}
}
