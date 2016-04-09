package ru.romster.bignerdranch.criminalintent.controller.activity;

import android.support.v4.app.Fragment;

import ru.romster.bignerdranch.criminalintent.controller.fragment.CrimeFragment;
import ru.romster.bignerdranch.criminalintent.controller.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleActivityFragment {

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}
}
