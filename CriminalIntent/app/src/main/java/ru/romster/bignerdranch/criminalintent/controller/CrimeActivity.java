package ru.romster.bignerdranch.criminalintent.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import ru.romster.bignerdranch.criminalintent.R;

public class CrimeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crime);

		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

		if(fragment == null) {
			fragment = new CrimeFragment();
			fragmentManager.beginTransaction()
					.add(R.id.fragmentContainer, fragment)
					.commit();
		}
	}
}
