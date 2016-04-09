package ru.romster.bignerdranch.criminalintent.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.romster.bignerdranch.criminalintent.model.Crime;

/**
 * Created by romster on 09/04/16.
 */
public class CrimeLab {

	private static CrimeLab instance;
	private final Context appContext;
	private final List<Crime> crimeList;

	public static CrimeLab getInstance(Context context) {
		if (instance == null) {
			instance = new CrimeLab(context);
		}
		return instance;
	}


	public List<Crime> getCrimeList() {
		return crimeList;
	}

	public Crime getCrime(UUID id) {
		for (Crime c : crimeList) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}

	private CrimeLab(Context appContext) {
		this.appContext = appContext;
		this.crimeList = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			Crime crime = new Crime();
			crime.setTitle("Crime #" + i);
			crime.setSolved(i % 2 == 0);
			crimeList.add(crime);
		}
	}
}
