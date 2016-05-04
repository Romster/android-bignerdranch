package ru.romster.bignerdranch.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import ru.romster.bignerdranch.criminalintent.R;
import ru.romster.bignerdranch.criminalintent.database.CrimeLab;
import ru.romster.bignerdranch.criminalintent.controller.fragment.CrimeFragment;
import ru.romster.bignerdranch.criminalintent.model.Crime;

/**
 * Created by romster on 19/04/16.
 */
public class CrimePagerActivity extends AppCompatActivity {

	public static final String EXTRA_CRIME_ID = "ru.romster.bignerdranch.criminalintent.crime_id";

	private ViewPager viewPager;
	private List<Crime> crimeList;

	public static Intent newIntent(Context context, UUID crimeId) {
		Intent intent = new Intent(context, CrimePagerActivity.class);
		intent.putExtra(EXTRA_CRIME_ID, crimeId);
		return intent;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crime_pager);
		viewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
		crimeList = CrimeLab.getInstance(this).getCrimeList();

		FragmentManager fragmentManager = getSupportFragmentManager();

		UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

		viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
			@Override
			public Fragment getItem(int position) {
				Crime crime = crimeList.get(position);
				return CrimeFragment.newInstance(crime.getId());
			}

			@Override
			public int getCount() {
				return crimeList.size();
			}
		});

		for(int i =0; i < crimeList.size(); i++) {
			if(crimeList.get(i).getId().equals(crimeId)) {
				viewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
