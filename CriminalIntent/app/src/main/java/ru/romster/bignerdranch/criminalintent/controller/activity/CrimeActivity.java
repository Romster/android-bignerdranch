package ru.romster.bignerdranch.criminalintent.controller.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.controller.fragment.CrimeFragment;

public class CrimeActivity extends SingleActivityFragment {

	@Override
	protected Fragment createFragment() {
		return new CrimeFragment();
	}
}
