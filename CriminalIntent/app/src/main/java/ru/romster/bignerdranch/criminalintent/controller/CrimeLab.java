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

	private volatile CrimeLock crimeLock;
	
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

	/**
	 * lock crime for editing
	 *
	 * @param crimeId
	 */
	public synchronized void lockCrime(UUID crimeId) {
		if (crimeLock != null) {
			throw new IllegalStateException("CrimeLab already has lock: " + crimeLock.crimeUUID);
		}
		crimeLock = new CrimeLock(crimeId);
	}

	/**
	 * @return null if no crime was locked
	 */
	public synchronized CrimeLock freeCrimeLock() {
		CrimeLock result = crimeLock;
		crimeLock = null;
		return result;
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


	public class CrimeLock {
		public final UUID crimeUUID;
		public final int crimePosition;

		private CrimeLock(UUID crimeUUID) {
			this.crimeUUID = crimeUUID;
			for (int i = 0; i < crimeList.size(); i++) {
				if (crimeList.get(i).getId().equals(crimeUUID)) {
					crimePosition = i;
					return;
				}
			}
			throw new IllegalArgumentException("Can not lock crime with id: " + crimeUUID);
		}

	}
}
