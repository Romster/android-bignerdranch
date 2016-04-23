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

	private volatile static CrimeLab instance;
	private final Context appContext;
	private final List<Crime> crimeList;

	public static CrimeLab getInstance(Context context) {
		if (instance == null) {
			synchronized (CrimeLab.class) {
				if (instance == null) {
					instance = new CrimeLab(context);
				}
			}
		}
		return instance;
	}


	public void addCrime(Crime c) {
		crimeList.add(c);
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
	}
}
