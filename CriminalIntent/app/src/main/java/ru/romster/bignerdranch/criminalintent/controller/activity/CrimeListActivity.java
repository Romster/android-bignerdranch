package ru.romster.bignerdranch.criminalintent.controller.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.controller.fragment.CrimeFragment;
import ru.romster.bignerdranch.criminalintent.controller.fragment.CrimeListFragment;
import ru.romster.bignerdranch.criminalintent.model.Crime;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}

	@Override
	public void onCrimeSelected(Crime crime) {
		boolean isMasterDetailView = findViewById(R.id.detail_fragment_container) != null;

		if (isMasterDetailView) {
			Fragment newFragment = CrimeFragment.newInstance(crime.getId());
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.detail_fragment_container, newFragment)
					.commit();
		} else {
			Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
			startActivity(intent);
		}
	}

	@Override
	public void onCrimeUpdate(Crime crime) {
		CrimeListFragment listFragment= (CrimeListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);
		listFragment.updateUI();
	}
}
