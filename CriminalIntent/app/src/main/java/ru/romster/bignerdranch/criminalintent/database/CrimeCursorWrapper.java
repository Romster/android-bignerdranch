package ru.romster.bignerdranch.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import ru.romster.bignerdranch.criminalintent.database.CrimeDbSchema.CrimeTable;
import ru.romster.bignerdranch.criminalintent.model.Crime;

/**
 * Created by romster on 24/04/16.
 */
public class CrimeCursorWrapper extends CursorWrapper {
	public CrimeCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	public Crime getCrime() {
		String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
		String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
		long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
		int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
		String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
		Integer suspectId = isNull(getColumnIndex(CrimeTable.Cols.SUSPECT_ID))
				? null
				: getInt(getColumnIndex(CrimeTable.Cols.SUSPECT_ID));

		Crime crime = new Crime(UUID.fromString(uuidString));
		crime.setTitle(title);
		crime.setDate(new Date(date));
		crime.setSolved(isSolved != 0);
		crime.setSuspect(suspect);
		crime.setSuspectId(suspectId);

		return crime;
	}
}
